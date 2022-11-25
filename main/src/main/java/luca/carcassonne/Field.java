package luca.carcassonne;

import java.util.ArrayList;

public class Field extends Feature{
    private static Integer pointsOpen = 0;

    Field(ArrayList<CardinalPoint> cardinalPoints) {
        super(cardinalPoints);
        super.setPointsOpen(0);

    }
}
