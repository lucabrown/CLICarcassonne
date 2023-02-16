package luca.carcassonne.MCTS;

import java.util.ArrayList;
import java.util.Stack;

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
    private Board startingBoard;
    private int startingPlayer;
    private Tile currentTile;
    private ArrayList<Player> players;
    private Stack<Tile> availableTiles;

    public MonteCarloTreeSearch(Board startingBoard, int startingPlayer, Tile currentTile,
            ArrayList<Player> players, Stack<Tile> availableTiles) {
        this.startingBoard = startingBoard;
        this.startingPlayer = startingPlayer;
        this.currentTile = currentTile;
        this.players = players;
        this.availableTiles = availableTiles;
    }

    public Move findNextMove() throws CloneNotSupportedException {
        Tree tree = new Tree();
        Node rootNode = tree.getRoot();
        double startTime = System.currentTimeMillis();
        double timeForOneMove = 5000;

        rootNode.getState().initialise(startingBoard, startingPlayer, currentTile, players, availableTiles);

        while (System.currentTimeMillis() - startTime < timeForOneMove) {

            // Selection
            Node promisingNode = selectPromisingNode(rootNode);

            // Expansion
            if (!promisingNode.getState().getBoard().isFinished())
                expandNode(promisingNode);

            // Simulation
            Node nodeToExplore = promisingNode;

            if (promisingNode.hasChildren()) {
                nodeToExplore = promisingNode.getRandomChildNode(); // Random??
            }

            simulateRandomPlayout(nodeToExplore);

            // Backpropagation
            backPropagation(nodeToExplore);
        }

        Node bestNode = rootNode.getChildWithMaxScore();
        return bestNode.getState().getMove();
    }

    private Node selectPromisingNode(Node parentNode) {
        if (parentNode.getChildren().isEmpty())
            return parentNode;

        Node node = null;

        node = UCT.findBestNodeWithUCT(parentNode);

        return node;
    }

    private void expandNode(Node promisingNode) throws CloneNotSupportedException {
        ArrayList<State> possibleStates = promisingNode.getState().getAllPossibleChildStates();

        for (State state : possibleStates) {
            Node newNode = new Node(state);

            newNode.setParent(promisingNode);
            promisingNode.getChildren().add(newNode);
        }
    }

    private void simulateRandomPlayout(Node nodeToExplore) throws CloneNotSupportedException {
        Node tempNode = new Node(nodeToExplore);
        State tempState = tempNode.getState();

        tempState.randomPlay();
    }

    private void backPropagation(Node exploredNode) {
        Node tempNode = exploredNode;
        double playoutResult = exploredNode.getState().getFinalScoreDifference();

        while (tempNode != null) {
            tempNode.getState().incrementVisit();
            if (tempNode.getState().getCurrentPlayer() == startingPlayer) {
                tempNode.getState().setFinalScoreDifference((int) playoutResult);
            }

            tempNode = tempNode.getParent();
        }
    }
}