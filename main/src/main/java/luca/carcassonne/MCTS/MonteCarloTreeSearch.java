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

    public Move findNextMove() {
        Node rootNode = new Node();
        double startTime = System.currentTimeMillis();
        double timeForOneMove = 60000;

        rootNode.getState().initialise(startingBoard, startingPlayer, currentTile, players, availableTiles);

        int iterations = 0;
        while (iterations < 500) {
            System.out.println("\n\n********* ITERATION " + iterations++ + " *********");
            // Selection
            System.out.println("\n- - - SELECTION - - -");
            Node promisingNode = selectPromisingNode(rootNode);

            // Expansion
            System.out.println("\n- - - EXPANSION - - -");
            if (!promisingNode.getState().getBoard().isFinished())
                expandNode(promisingNode);

            // Simulation
            System.out.println("\n- - - SIMULATION - - -");
            Node nodeToExplore = promisingNode;

            if (promisingNode.hasChildren()) {
                nodeToExplore = promisingNode.getRandomChildNode(); // Random??
                System.out.println("- Chosen random child");
            }

            simulateRandomPlayout(nodeToExplore);

            // Backpropagation
            System.out.println("\n- - - BACKPROPAGATION - - -");
            backPropagation(nodeToExplore);
        }

        Node bestNode = rootNode.getChildWithMaxScore();
        return bestNode.getState().getMove();
    }

    private Node selectPromisingNode(Node parentNode) {
        if (parentNode.getChildren().isEmpty())
            return parentNode;

        Node node = null;

        while (!parentNode.getChildren().isEmpty()) {
            node = UCT.findBestNodeWithUCT(parentNode);
            parentNode = node;
        }

        System.out.println("- Selected node from " + parentNode.getChildren().size() + " children");
        System.out.println("- Node tile: " + node.getState().getCurrentTile());
        return node;
    }

    private void expandNode(Node promisingNode) {
        ArrayList<State> possibleStates = promisingNode.getState().getAllPossibleChildStates();
        System.out.println("- Getting all children: " + possibleStates.size());

        for (State state : possibleStates) {
            Node newNode = new Node(state);

            newNode.setParent(promisingNode);
            promisingNode.getChildren().add(newNode);
        }
    }

    private void simulateRandomPlayout(Node nodeToExplore) {
        Node tempNode = (Node) nodeToExplore.clone();
        State tempState = tempNode.getState();
        System.out.println("- Simulating random playout");
        tempState.randomPlay();
        System.out.println("- Playout terminated");
    }

    private void backPropagation(Node exploredNode) {
        Node tempNode = exploredNode;
        double playoutResult = exploredNode.getState().getFinalScoreDifference();
        int i = 0;
        while (tempNode != null) {
            i++;
            tempNode.getState().incrementVisit();
            if (tempNode.getState().getCurrentPlayer() == startingPlayer) {
                tempNode.getState().setFinalScoreDifference((int) playoutResult);
            }

            tempNode = tempNode.getParent();
        }
        System.out.println("- Backpropagated " + i + " nodes");
    }
}