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
        Tree tree = new Tree();
        Node rootNode = tree.getRoot();

        rootNode.getState().initialise(startingBoard, startingPlayer, currentTile, players, availableTiles);

        // Selection
        Node promisingNode = selectPromisingNode(rootNode);

        // Expansion
        if (promisingNode.getState().getBoard().checkStatus() == Board.IN_PROGRESS)
            expandNode(promisingNode);

        // Simulation
        Node nodeToExplore = promisingNode;

        if (promisingNode.hasChildren())
            nodeToExplore = promisingNode.getRandomChildNode();

        int playoutResult = simulateRandomPlayout(nodeToExplore);

        // Backpropagation
        backPropagation(nodeToExplore, playoutResult);

        return null;
    }

    private Node selectPromisingNode(Node rootNode) {
        return null;
    }

    private void expandNode(Node promisingNode) {
    }

    private int simulateRandomPlayout(Node nodeToExplore) {
        return 0;
    }

    private void backPropagation(Node nodeToExplore, int playoutResult) {
    }
}