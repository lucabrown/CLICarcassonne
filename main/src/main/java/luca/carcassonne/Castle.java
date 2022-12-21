package luca.carcassonne;

import java.util.ArrayList;

public class Castle extends Feature{
    private static final Integer POINTS_CLOSED = 2;
    private static final Integer POINTS_OPEN = 1;
    private static boolean HAS_SHIELD;

    Castle(ArrayList<CardinalPoint> cardinalPoints, boolean hasShield) {
        super(cardinalPoints);
        super.setPointsClosed(POINTS_CLOSED);
        super.setPointsOpen(POINTS_OPEN);
        HAS_SHIELD = hasShield;
    }

    public boolean hasShield() {
        return HAS_SHIELD;
    }
}
