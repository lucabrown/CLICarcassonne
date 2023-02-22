package luca.carcassonne.player;

import luca.carcassonne.Settings;

public class Player {
    private Colour colour;
    private Behaviour behaviour;
    private int score;
    private int availableMeeples;

    public Player(Colour colour) {
        this.colour = colour;
        this.score = 0;
        this.availableMeeples = Settings.MAX_MEEPLES;
        this.behaviour = Behaviour.RANDOM;
    }

    public Player(Colour colour, Behaviour behaviour) {
        this.colour = colour;
        this.score = 0;
        this.availableMeeples = Settings.MAX_MEEPLES;
        this.behaviour = behaviour;
    }

    public Player() {
        this.colour = null;
        this.score = 0;
        this.availableMeeples = Settings.MAX_MEEPLES;
        this.behaviour = Behaviour.RANDOM;
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

    public void setBehaviour(Behaviour behaviour) {
        this.behaviour = behaviour;
    }

    public Behaviour getBehaviour() {
        return behaviour;
    }

    // @Override
    // public Object clone() {
    // Player clone = new Player(colour);
    // clone.score = score;
    // clone.availableMeeples = availableMeeples;
    // return clone;
    // }

}
