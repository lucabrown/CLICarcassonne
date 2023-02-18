package luca.carcassonne.player;

import luca.carcassonne.Settings;

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

    public boolean isHuman() {
        return true;
    }

    // @Override
    // public Object clone() {
    // Player clone = new Player(colour);
    // clone.score = score;
    // clone.availableMeeples = availableMeeples;
    // return clone;
    // }

}
