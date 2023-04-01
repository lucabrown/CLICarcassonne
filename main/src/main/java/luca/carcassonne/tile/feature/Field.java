package luca.carcassonne.tile.feature;

import java.util.ArrayList;
import luca.carcassonne.tile.CardinalPoint;

/**
 * A field feature.
 * 
 * @author Luca Brown
 */
public class Field extends Feature {
    ArrayList<Castle> adjacentCastles;

    public Field(ArrayList<CardinalPoint> cardinalPoints, ArrayList<Castle> adjacentCastles) {
        super(cardinalPoints);
        this.adjacentCastles = adjacentCastles;
    }

    public boolean hasAdjacentCastle() {
        return adjacentCastles.size() > 0;
    }

    public ArrayList<Castle> getAdjacentCastles() {
        return adjacentCastles;
    }

}
