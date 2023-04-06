package luca.carcassonne.mcts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import org.javatuples.Pair;

import luca.carcassonne.Board;
import luca.carcassonne.player.Player;
import luca.carcassonne.tile.Tile;

/**
 * A Monte Carlo Tree Search implementation.
 * 
 * This class creates a Monte Carlo Tree Search object that can be used to find
 * the best move based on the current state of the game.
 *
 * @author Luca Brown
 */
public class MonteCarloTreeSearch {
    private State startingState;
    private int maxIterations = 0;
    private double explorationConstant = 0;
    private double progressiveHistoryConstant = 3;

    public MonteCarloTreeSearch(int maxIterations, double explorationConstant, Board startingBoard, int startingPlayer,
            Tile currentTile,
            ArrayList<Player> players, Stack<Tile> availableTiles) {
        startingState = new State(startingBoard, startingPlayer, currentTile, players, availableTiles);
        this.maxIterations = maxIterations;
        this.explorationConstant = explorationConstant;
    }

    /**
     * Returns the best move found by Monte Carlo Tree Search.
     * 
     * @return The best move found by Monte Carlo Tree Search.
     */
    public Move findNextMove() {
        Node rootNode = new Node(startingState);
        int iterations = 0;

        while (iterations < maxIterations) {
            iterations++;

            // Selection
            Node promisingNode = selectPromisingNode(rootNode);

            // Expansion
            if (promisingNode.getState().getCurrentTile() != null) {
                expandNode(promisingNode);
            }

            // Simulation
            Node nodeToExplore = promisingNode;

            if (promisingNode.hasChildren()) {
                nodeToExplore = promisingNode.getChildWithGreedyPolicy(); // Random??
            }

            int playoutResult = simulateRandomPlayout(nodeToExplore);

            // Backpropagation
            backPropagation(nodeToExplore, playoutResult);
        }

        Node bestNode = rootNode.getChildWithMaxScore();
        return bestNode.getState().getBoard().getLastMove();
    }

    /**
     * Returns the best move found by Monte Carlo Tree Search using progressive
     * history.
     * 
     * @param totalActionMap   The total number of times an action has been taken.
     * @param winningActionMap The total number of times an action resulted in a
     *                         win.
     * @return The best move found by Monte Carlo Tree Search using progressive
     */
    public Move findProgressiveHistoryMove(HashMap<Pair<String, Integer>, Integer> totalActionMap,
            HashMap<Pair<String, Integer>, Integer> winningActionMap) {
        Node rootNode = new Node(startingState);
        int iterations = 0;

        while (iterations < maxIterations) {
            iterations++;

            // Selection
            Node promisingNode = selectPromisingNodeWithHistoryHeuristic(rootNode, totalActionMap, winningActionMap);

            // Expansion
            if (promisingNode.getState().getCurrentTile() != null) {
                expandNode(promisingNode);
            }

            // Simulation
            Node nodeToExplore = promisingNode;

            if (promisingNode.hasChildren()) {
                nodeToExplore = promisingNode.getRandomChildNode(); // Random??
            }

            int playoutResult = simulateRandomPlayout(nodeToExplore);

            // Backpropagation
            backPropagation(nodeToExplore, playoutResult);
        }

        Node bestNode = rootNode.getChildWithMaxScore();
        return bestNode.getState().getBoard().getLastMove();
    }

    /**
     * Returns the best move found with a greedy policy.
     *
     * @return The best move found with a greedy policy.
     */
    public Move getGreedyMove() {
        Node rootNode = new Node(startingState);

        expandNode(rootNode);
        Node toReturn = rootNode.getChildWithGreedyPolicy();

        if (toReturn == null)
            return null;
        else
            return toReturn.getState().getBoard().getLastMove();
    }

    /**
     * Returns a random move.
     *
     * @return A random move.
     */
    public Move getRandomMove() {
        Node rootNode = new Node(startingState);

        expandNode(rootNode);
        Node toReturn = rootNode.getRandomChildNode();

        if (toReturn == null)
            return null;
        else
            return toReturn.getState().getBoard().getLastMove();
    }

    /**
     * Returns the best node with the UCT formula.
     * 
     * @param parentNode The parent node.
     * @return The best node with the UCT formula.
     */
    private Node selectPromisingNode(Node parentNode) {
        if (parentNode.getChildren().isEmpty())
            return parentNode;

        Node node = null;

        while (!parentNode.getChildren().isEmpty()) {
            node = findBestNodeWithUCT(parentNode);
            parentNode = node;
        }

        return node;
    }

    /**
     * Returns the best node with the progressive history UCT formula.
     * 
     * @param parentNode       The parent node.
     * @param totalActionMap   The total number of times an action has been taken.
     * @param winningActionMap The total number of times an action resulted in a
     * @return The best node with the progressive history UCT formula.
     */
    private Node selectPromisingNodeWithHistoryHeuristic(Node parentNode,
            HashMap<Pair<String, Integer>, Integer> totalActionMap,
            HashMap<Pair<String, Integer>, Integer> winningActionMap) {
        if (parentNode.getChildren().isEmpty())
            return parentNode;

        Node node = null;

        while (!parentNode.getChildren().isEmpty()) {
            node = findBestNodeWithHistoryHeuristic(parentNode, totalActionMap, winningActionMap);
            parentNode = node;
        }

        return node;
    }

    /**
     * Expands the node by creating all its children.
     * 
     * @param promisingNode The node to expand.
     */
    private void expandNode(Node promisingNode) {
        ArrayList<State> possibleStates = promisingNode.getState().getAllPossibleChildStates();

        for (State state : possibleStates) {
            Node newNode = new Node(state);

            newNode.setParent(promisingNode);
            promisingNode.getChildren().add(newNode);
        }
    }

    /**
     * Simulates a random playout.
     * 
     * @param nodeToExplore The node to explore.
     * @return The result of the playout.
     */
    private int simulateRandomPlayout(Node nodeToExplore) {
        int playoutResult = nodeToExplore.getState().randomPlay();

        nodeToExplore.getState().setFinalScoreDifference(playoutResult);

        return playoutResult;
    }

    /**
     * Backpropagates the result of the playout.
     * 
     * @param exploredNode  The node to explore.
     * @param playoutResult The result of the playout.
     */
    private void backPropagation(Node exploredNode, int playoutResult) {
        Node tempNode = exploredNode;
        while (tempNode != null) {
            tempNode.getState().incrementVisit();

            if (tempNode.getState().getFinalScoreDifference() < playoutResult) {
                tempNode.getState().setFinalScoreDifference(playoutResult);
            }

            tempNode = tempNode.getParent();
        }
    }

    /**
     * Returns the best node with the UCT formula.
     * 
     * @param parentNode The parent node.
     * @return The best node with the UCT formula.
     */
    private Node findBestNodeWithUCT(Node parentNode) {
        int parentVisit = parentNode.getState().getVisitCount();
        double bestValue = Integer.MIN_VALUE;
        Node bestNode = null;

        for (Node childNode : parentNode.getChildren()) {
            double nodeValue = uctValue(parentVisit, childNode.getState().getFinalScoreDifference(),
                    childNode.getState().getVisitCount());
            if (nodeValue > bestValue) {
                bestNode = childNode;
                bestValue = nodeValue;
            }
        }

        return bestNode;
    }

    /**
     * Returns the best node with the progressive history UCT formula.
     * 
     * @param parentNode       The parent node.
     * @param totalActionMap   The total number of times an action has been taken.
     * @param winningActionMap The total number of times an action resulted in a
     * @return The best node with the progressive history UCT formula.
     */
    private Node findBestNodeWithHistoryHeuristic(Node parentNode,
            HashMap<Pair<String, Integer>, Integer> totalActionMap,
            HashMap<Pair<String, Integer>, Integer> winningActionMap) {
        int parentVisit = parentNode.getState().getVisitCount();
        double bestValue = Integer.MIN_VALUE;
        Node bestNode = null;

        for (Node childNode : parentNode.getChildren()) {
            double nodeValue = uctValue(parentVisit, childNode.getState().getFinalScoreDifference(),
                    childNode.getState().getVisitCount())
                    + progressiveHistoryUctValue(parentVisit, bestValue, parentVisit, childNode.getState().getBoard()
                            .getLastMove(), totalActionMap, winningActionMap);
            if (nodeValue > bestValue) {
                bestNode = childNode;
                bestValue = nodeValue;
            }
        }

        return bestNode;
    }

    private double uctValue(int totalVisit, double nodeScoreDifference, int nodeVisit) {
        double uctValue = Integer.MAX_VALUE;

        if (nodeVisit == 0) {
            return uctValue;
        }

        uctValue = (nodeScoreDifference / (double) nodeVisit)
                + explorationConstant * Math.sqrt(Math.log(totalVisit) / (double) nodeVisit);

        return uctValue;
    }

    /**
     * Ca;lulates the progressive history UCT value.
     * 
     * @param totalVisit          The total number of times the node has been
     *                            visited.
     * @param nodeScoreDifference The score difference of the node.
     * @param nodeVisit           The number of times the node has been visited.
     * @param performedMove       The move that was performed to get to the node.
     * @param totalActionMap      The total number of times an action has been
     *                            taken.
     * @param winningActionMap    The total number of times an action resulted in a
     *                            win.
     * @return The progressive history UCT value.
     */
    private double progressiveHistoryUctValue(int totalVisit, double nodeScoreDifference, int nodeVisit,
            Move performedMove, HashMap<Pair<String, Integer>, Integer> totalActionMap,
            HashMap<Pair<String, Integer>, Integer> winningActionMap) {
        double uctValue = Integer.MAX_VALUE;
        double actionScore = 0;
        int timesActionPlayed = 0;
        Pair<String, Integer> action = new Pair<String, Integer>(performedMove.getTileId(),
                performedMove.getFeatureIndex());

        if (nodeVisit == 0) {
            return uctValue;
        }

        if (totalActionMap.size() == 0 || winningActionMap.size() == 0) {
            return uctValue;
        }

        actionScore = winningActionMap.getOrDefault(action, 0);
        timesActionPlayed = totalActionMap.getOrDefault(action, 0);

        uctValue = (actionScore / timesActionPlayed) * (progressiveHistoryConstant
                / (1 + (double) nodeVisit - nodeScoreDifference));

        return uctValue;
    }

    public State getStartingState() {
        return startingState;
    }
}