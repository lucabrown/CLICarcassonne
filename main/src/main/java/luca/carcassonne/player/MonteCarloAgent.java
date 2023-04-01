package luca.carcassonne.player;

import java.util.ArrayList;
import java.util.Stack;

import luca.carcassonne.Board;
import luca.carcassonne.mcts.MonteCarloTreeSearch;
import luca.carcassonne.mcts.Move;
import luca.carcassonne.tile.Tile;

/**
 * The MonteCarloAgent that uses Monte Carlo Tree Search to find the best move.
 * 
 * @author Luca Brown
 */
public class MonteCarloAgent extends Player {
    private int maxIterations = 0;
    private double explorationConstant = 0;

    public MonteCarloAgent(Colour colour, int maxIterations, double explorationConstant) {
        super(colour);
        this.maxIterations = maxIterations;
        this.explorationConstant = explorationConstant;
    }

    /**
     * Returns the best move found by Monte Carlo Tree Search.
     * 
     * @param startingBoard  The board to start the search from.
     * @param startingPlayer The player to start the search from.
     * @param currentTile    The tile to start the search from.
     * @param players        The players in the game.
     * @param availableTiles The tiles that are available to be placed.
     * @return The best move found by Monte Carlo Tree Search.
     */
    @Override
    public Move getNextMove(Board startingBoard, int startingPlayer, Tile currentTile,
            ArrayList<Player> players, Stack<Tile> availableTiles) {

        MonteCarloTreeSearch mcts = new MonteCarloTreeSearch(maxIterations, explorationConstant, startingBoard,
                startingPlayer, currentTile,
                players, availableTiles);

        return mcts.findNextMove();
    }

    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public void setExplorationConstant(double explorationConstant) {
        this.explorationConstant = explorationConstant;
    }

    public double getExplorationConstant() {
        return explorationConstant;
    }
}
