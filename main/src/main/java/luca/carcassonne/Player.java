package luca.carcassonne;

public class Player {
    public static final Integer MAX_MEEPLES = 7;
    Colour colour;
    Integer points;
    Integer availableMeeples;

    public Player(Colour colour) {
        this.colour = colour;
        this.points = 0;
        this.availableMeeples = MAX_MEEPLES;
    }

    public Colour getColour() {
        return colour;
    }
}
