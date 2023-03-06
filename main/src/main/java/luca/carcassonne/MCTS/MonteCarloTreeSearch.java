package luca.carcassonne.mcts;

import java.io.FileWriter;
import java.io.IOException;
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
    private State startingState;
    private int maxIterations = 0;

    public MonteCarloTreeSearch(int maxIterations, Board startingBoard, int startingPlayer, Tile currentTile,
            ArrayList<Player> players, Stack<Tile> availableTiles) {
        startingState = new State(startingBoard, startingPlayer, currentTile, players, availableTiles);
        this.maxIterations = maxIterations;
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
            node = UCT.findBestNodeWithUCT(parentNode);
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

    public State getStartingState() {
        return startingState;
    }
}