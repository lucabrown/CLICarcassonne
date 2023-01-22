package luca.carcassonne;

public class Player {
    public static final Integer MAX_MEEPLES = 7;
    Behaviour behaviour;
    Colour colour;
    Integer score;
    Integer availableMeeples;

    public Player(Colour colour) {
        this.colour = colour;
        this.score = 0;
        this.availableMeeples = MAX_MEEPLES;
        behaviour = new Behaviour();
    }

    public Integer getScore() {
        return score;
    }

    public void addScore(Integer score) {
        this.score += score;
    }

    public Integer getAvailableMeeples() {
        return availableMeeples;
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
}
