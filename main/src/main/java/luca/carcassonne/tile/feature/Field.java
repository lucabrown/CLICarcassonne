package luca.carcassonne.tile.feature;

import java.util.ArrayList;
import java.util.HashSet;

import luca.carcassonne.tile.CardinalPoint;

public class Field extends Feature{
    HashSet<Castle> adjacentCastles;

    public Field(ArrayList<CardinalPoint> cardinalPoints, HashSet<Castle> adjacentCastles) {
        super(cardinalPoints);
        this.adjacentCastles = adjacentCastles;
    }

    public boolean hasAdjacentCastle() {
        return adjacentCastles.size() > 0;
    }

    public HashSet<Castle> getAdjacentCastles() {
        return adjacentCastles;
    }


}
