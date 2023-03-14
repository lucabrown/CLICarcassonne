package luca.carcassonne.mcts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import org.javatuples.Pair;

/*
* All the code in this file is from
*
*
https://github.com/eugenp/tutorials/tree/master/algorithms-modules/algorithms-searching/src/main/java/com/baeldung/algorithms/mcts
*
* unless clearly stated otherwise.
*/

import luca.carcassonne.Board;
import luca.carcassonne.player.Player;
import luca.carcassonne.tile.Tile;

public class MonteCarloTreeSearch {
    private State startingState;
    private int maxIterations = 0;
    private double explorationConstant = 0;
    private double progressiveHistoryConstant = 5;

    public MonteCarloTreeSearch(int maxIterations, double explorationConstant, Board startingBoard, int startingPlayer,
            Tile currentTile,
            ArrayList<Player> players, Stack<Tile> availableTiles) {
        startingState = new State(startingBoard, startingPlayer, currentTile, players, availableTiles);
        this.maxIterations = maxIterations;
        this.explorationConstant = explorationConstant;
    }

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
                nodeToExplore = promisingNode.getRandomChildNode(); // Random??
            }

            int playoutResult = simulateRandomPlayout(nodeToExplore);

            // Backpropagation
            backPropagation(nodeToExplore, playoutResult);
        }

        Node bestNode = rootNode.getChildWithMaxScore();
        return bestNode.getState().getBoard().getLastMove();
    }

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

    public Move getRandomMove() {
        Node rootNode = new Node(startingState);

        expandNode(rootNode);
        Node toReturn = rootNode.getRandomChildNode();

        if (toReturn == null)
            return null;
        else
            return toReturn.getState().getBoard().getLastMove();
    }

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

    private void expandNode(Node promisingNode) {
        ArrayList<State> possibleStates = promisingNode.getState().getAllPossibleChildStates();

        for (State state : possibleStates) {
            Node newNode = new Node(state);

            newNode.setParent(promisingNode);
            promisingNode.getChildren().add(newNode);
        }
    }

    private int simulateRandomPlayout(Node nodeToExplore) {
        int playoutResult = nodeToExplore.getState().randomPlay();

        nodeToExplore.getState().setFinalScoreDifference(playoutResult);

        return playoutResult;
    }

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

    private double progressiveHistoryUctValue(int totalVisit, double nodeScoreDifference, int nodeVisit,
            Move performedMove, HashMap<Pair<String, Integer>, Integer> totalActionMap,
            HashMap<Pair<String, Integer>, Integer> winningActionMap) {
        double uctValue = Integer.MAX_VALUE;
        double actionScore = 0;
        int timesActionPlayed = 0;
        Pair<String, Integer> action = new Pair<String, Integer>(performedMove.getTile().getId(),
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