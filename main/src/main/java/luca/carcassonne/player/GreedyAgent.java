package luca.carcassonne.player;

import java.util.ArrayList;
import java.util.Stack;

import luca.carcassonne.Board;
import luca.carcassonne.mcts.MonteCarloTreeSearch;
import luca.carcassonne.mcts.Move;
import luca.carcassonne.tile.Tile;

/**
 * The GreedyAgent that maximises its score after each move.
 * 
 * It does not do any exploration.
 * 
 * @author Luca Brown
 */
public class GreedyAgent extends Player {
    public GreedyAgent(Colour colour) {
        super(colour);
    }

    /**
     * Returns a greedy move.
     * 
     * @param startingBoard  The board to start the search from.
     * @param startingPlayer The player to start the search from.
     * @param currentTile    The tile to start the search from.
     * @param players        The players in the game.
     * @param availableTiles The tiles that are available to be placed.
     * @return A greedy move.
     */
    @Override
    public Move getNextMove(Board startingBoard, int startingPlayer, Tile currentTile,
            ArrayList<Player> players, Stack<Tile> availableTiles) {
        MonteCarloTreeSearch mcts = new MonteCarloTreeSearch(0, 0, startingBoard, startingPlayer, currentTile,
                players, availableTiles);

        return mcts.getGreedyMove();
    }
}
