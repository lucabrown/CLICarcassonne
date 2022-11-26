package luca.carcassonne;

import java.util.ArrayList;

public class Road extends Feature{
    private static final Integer POINTS_CLOSED = 1;
    private static final Integer POINTS_OPEN = 1;

    Road(ArrayList<CardinalPoint> cardinalPoints) {
        super(cardinalPoints);
        super.setPointsClosed(POINTS_CLOSED);
        super.setPointsOpen(POINTS_OPEN);
    }
}
