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
import luca.carcassonne.CloneManager;
import luca.carcassonne.player.Behaviour;
import luca.carcassonne.player.Player;
import luca.carcassonne.tile.Tile;

public class MonteCarloTreeSearch {
    private State startingState;

    public MonteCarloTreeSearch(Board startingBoard, int startingPlayer, Tile currentTile,
            ArrayList<Player> players, Stack<Tile> availableTiles) {
        startingState = new State(startingBoard, startingPlayer, currentTile, players, availableTiles);
    }

    public Move findNextMove(Behaviour behaviour) {
        Node rootNode = new Node(startingState);
        double startTime = System.currentTimeMillis();
        double timeForOneMove = 5000;
        int iterations = 0;

        if (behaviour == Behaviour.RANDOM) {
            expandNode(rootNode);
            return rootNode.getRandomChildNode().getState().getBoard().getLastMove();
        }

        while (System.currentTimeMillis() - startTime < timeForOneMove || iterations < 100) {

            System.out.println("\n\n********* ITERATION " + iterations++ + " *********");
            // Selection
            System.out.println("\n- - - SELECTION - - -");
            Node promisingNode = selectPromisingNode(rootNode);

            // Expansion
            System.out.println("\n- - - EXPANSION - - -");

            if (promisingNode.getState().getCurrentTile() != null) {
                promisingNode.getState().getBoard().printBoard();
                expandNode(promisingNode);
            }

            // Simulation
            System.out.println("\n- - - SIMULATION - - -");
            Node nodeToExplore = promisingNode;

            if (promisingNode.hasChildren()) {
                nodeToExplore = promisingNode.getRandomChildNode(); // Random??
                System.out.println("- Chosen random child: " + nodeToExplore.getState().getBoard().getPastMoves().get(0)
                        + " moves");
            }

            int playoutResult = simulateRandomPlayout(nodeToExplore);

            // Backpropagation
            System.out.println("\n- - - BACKPROPAGATION - - -");
            backPropagation(nodeToExplore, playoutResult);
        }

        Node bestNode = rootNode.getChildWithMaxScore();
        return bestNode.getState().getBoard().getLastMove();
    }

    private Node selectPromisingNode(Node parentNode) {
        if (parentNode.getChildren().isEmpty())
            return parentNode;

        Node node = null;
        int level = 0;

        while (!parentNode.getChildren().isEmpty()) {
            level++;
            node = UCT.findBestNodeWithUCT(parentNode);
            parentNode = node;
        }

        System.out.println(
                "- Selected node from " + node.getParent().getChildren().size() + " children at level " + level);
        System.out.println("- Node tile: " + node.getState().getCurrentTile());

        return node;
    }

    private void expandNode(Node promisingNode) {
        ArrayList<State> possibleStates = promisingNode.getState().getAllPossibleChildStates();
        System.out.println("- New children: " + possibleStates.size());

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
        int i = 0;
        while (tempNode != null) {
            i++;
            tempNode.getState().incrementVisit();
            // if (tempNode.getState().getCurrentPlayer() ==
            // startingState.getCurrentPlayer()) {
            // tempNode.getState().setFinalScoreDifference((int) playoutResult);
            // }

            if (tempNode.getState().getFinalScoreDifference() < playoutResult) {
                tempNode.getState().setFinalScoreDifference(playoutResult);
            }

            tempNode = tempNode.getParent();
        }

        System.out.println("- Backpropagated " + i + " nodes");
    }
}