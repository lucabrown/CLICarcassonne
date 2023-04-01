package luca.carcassonne.player;

import java.util.ArrayList;
import java.util.Stack;

import luca.carcassonne.Board;
import luca.carcassonne.Settings;
import luca.carcassonne.mcts.Move;
import luca.carcassonne.tile.Tile;

/**
 * Represents a player in the game.
 * 
 * It is an abstract class that is extended by the {@code RandomAgent},
 * {@code GreedyAgent}, {@code MonteCarloAgent} and
 * {@code ProgressiveHistoryAgent} classes.
 * 
 * @author Luca Brown
 */
public class Player {
    private Colour colour;
    private int score;
    private int availableMeeples;

    public Player(Colour colour) {
        this.colour = colour;
        this.score = 0;
        this.availableMeeples = Settings.MAX_MEEPLES;
    }

    public Player() {
        this.colour = null;
        this.score = 0;
        this.availableMeeples = Settings.MAX_MEEPLES;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public int getAvailableMeeples() {
        return availableMeeples;
    }

    public void setAvailableMeeples(int availableMeeples) {
        this.availableMeeples = availableMeeples;
    }

    public void incrementMeeples(int amount) {
        availableMeeples += amount;
    }

    public void incrementMeeples() {
        availableMeeples++;
    }

    public void decrementMeeples() {
        availableMeeples--;
    }

    public Colour getColour() {
        return colour;
    }

    public Move getNextMove(Board startingBoard, int startingPlayer, Tile currentTile,
            ArrayList<Player> players, Stack<Tile> availableTiles) {
        return null;
    }

}
