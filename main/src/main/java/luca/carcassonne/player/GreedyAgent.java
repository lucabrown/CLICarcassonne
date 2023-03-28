package luca.carcassonne.player;

import java.util.ArrayList;
import java.util.Stack;

import luca.carcassonne.Board;
import luca.carcassonne.mcts.MonteCarloTreeSearch;
import luca.carcassonne.mcts.Move;
import luca.carcassonne.tile.Tile;

public class GreedyAgent extends Player {
    public GreedyAgent(Colour colour) {
        super(colour);
    }

    @Override
    public Move getNextMove(Board startingBoard, int startingPlayer, Tile currentTile,
            ArrayList<Player> players, Stack<Tile> availableTiles) {
        MonteCarloTreeSearch mcts = new MonteCarloTreeSearch(0, 0, startingBoard, startingPlayer, currentTile,
                players, availableTiles);

        return mcts.getGreedyMove();
    }
}
