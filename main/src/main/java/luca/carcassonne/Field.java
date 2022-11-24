package luca.carcassonne;

import java.util.ArrayList;

public class Field extends Feature{
    private ArrayList<CardinalPoint> cardinalPoints;
    private boolean isOpen;

    Field(ArrayList<CardinalPoint> cardinalPoints) {
        super(cardinalPoints);
    }
}
