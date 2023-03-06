package luca.carcassonne.player;

import java.util.ArrayList;
import java.util.Stack;

import luca.carcassonne.Board;
import luca.carcassonne.mcts.MonteCarloTreeSearch;
import luca.carcassonne.mcts.Move;
import luca.carcassonne.tile.Tile;

public class MonteCarloAgent extends Player {
    private int maxIterations = 0;

    public MonteCarloAgent(Colour colour, int maxIterations) {
        super(colour);
        this.maxIterations = maxIterations;
    }

    @Override
    public Move getNextMove(Board startingBoard, int startingPlayer, Tile currentTile,
            ArrayList<Player> players, Stack<Tile> availableTiles) {
        MonteCarloTreeSearch mcts = new MonteCarloTreeSearch(maxIterations, startingBoard, startingPlayer, currentTile,
                players, availableTiles);

        return mcts.findNextMove();
    }

    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    public int getMaxIterations() {
        return maxIterations;
    }
}
