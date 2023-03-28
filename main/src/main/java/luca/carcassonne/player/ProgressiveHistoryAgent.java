package luca.carcassonne.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import org.javatuples.Pair;

import luca.carcassonne.Board;
import luca.carcassonne.mcts.MonteCarloTreeSearch;
import luca.carcassonne.mcts.Move;
import luca.carcassonne.tile.Tile;

public class ProgressiveHistoryAgent extends Player {
    private int maxIterations = 0;
    private double explorationConstant = 0;
    private HashMap<Pair<String, Integer>, Integer> totalActionMap = new HashMap<>();
    private HashMap<Pair<String, Integer>, Integer> winningActionMap = new HashMap<>();

    public ProgressiveHistoryAgent(
            Colour colour,
            int maxIterations,
            double explorationConstant, HashMap<Pair<String, Integer>, Integer> totalActionMap,
            HashMap<Pair<String, Integer>, Integer> winningActionMap) {
        super(colour);
        this.maxIterations = maxIterations;
        this.explorationConstant = explorationConstant;
    }

    @Override
    public Move getNextMove(Board startingBoard, int startingPlayer, Tile currentTile,
            ArrayList<Player> players, Stack<Tile> availableTiles) {

        MonteCarloTreeSearch mcts = new MonteCarloTreeSearch(maxIterations, explorationConstant, startingBoard,
                startingPlayer, currentTile,
                players, availableTiles);

        return mcts.findProgressiveHistoryMove(totalActionMap, winningActionMap);
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

    public HashMap<Pair<String, Integer>, Integer> getTotalActionMap() {
        return totalActionMap;
    }

    public void setTotalActionMap(HashMap<Pair<String, Integer>, Integer> totalActionMap) {
        this.totalActionMap = totalActionMap;
    }

    public HashMap<Pair<String, Integer>, Integer> getWinningActionMap() {
        return winningActionMap;
    }

    public void setWinningActionMap(HashMap<Pair<String, Integer>, Integer> winningActionMap) {
        this.winningActionMap = winningActionMap;
    }

}
