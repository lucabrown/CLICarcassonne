package luca.carcassonne.tile.feature;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

import luca.carcassonne.tile.CardinalPoint;

public class Field extends Feature {
    HashSet<Castle> adjacentCastles;

    public Field(ArrayList<CardinalPoint> cardinalPoints, HashSet<Castle> adjacentCastles) {
        super(cardinalPoints);
        this.adjacentCastles = adjacentCastles;
    }

    public Field() {
        super();
    }

    public boolean hasAdjacentCastle() {
        return adjacentCastles.size() > 0;
    }

    public HashSet<Castle> getAdjacentCastles() {
        return adjacentCastles;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object clone() {
        Field newField = new Field();

        newField.cardinalPoints = (ArrayList<CardinalPoint>) this.cardinalPoints.clone();
        newField.pointsClosed = this.pointsClosed;
        newField.pointsOpen = this.pointsOpen;
        newField.adjacentCastles = (HashSet<Castle>) this.adjacentCastles.stream()
                .map(c -> (Castle) c.clone()).collect(Collectors.toCollection(HashSet::new));

        return newField;
    }
}
