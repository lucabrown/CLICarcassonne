package luca.carcassonne;

import java.util.ArrayList;

public class Castle extends Feature{
    private static final Integer POINTS_CLOSED = 4;
    private static final Integer POINTS_OPEN = 2;

    Castle(ArrayList<CardinalPoint> cardinalPoints) {
        super(cardinalPoints);
        super.setPointsClosed(POINTS_CLOSED);
        super.setPointsOpen(POINTS_OPEN);
    }
}
