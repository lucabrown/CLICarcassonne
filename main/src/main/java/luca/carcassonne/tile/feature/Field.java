package luca.carcassonne.tile.feature;

import java.util.ArrayList;
import luca.carcassonne.tile.CardinalPoint;

public class Field extends Feature {
    ArrayList<Castle> adjacentCastles;

    public Field(ArrayList<CardinalPoint> cardinalPoints, ArrayList<Castle> adjacentCastles) {
        super(cardinalPoints);
        this.adjacentCastles = adjacentCastles;
    }

    public Field() {
        super();
    }

    public boolean hasAdjacentCastle() {
        return adjacentCastles.size() > 0;
    }

    public ArrayList<Castle> getAdjacentCastles() {
        return adjacentCastles;
    }

}
