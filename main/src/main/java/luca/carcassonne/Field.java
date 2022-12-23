package luca.carcassonne;

import java.util.ArrayList;
import java.util.HashSet;

public class Field extends Feature{
    HashSet<Castle> adjacentCastles;

    Field(ArrayList<CardinalPoint> cardinalPoints, HashSet<Castle> adjacentCastles) {
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
