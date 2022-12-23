package luca.carcassonne;

import java.util.ArrayList;

public class Castle extends Feature{
    private boolean HAS_SHIELD;

    Castle(ArrayList<CardinalPoint> cardinalPoints, boolean hasShield) {
        super(cardinalPoints);
        super.setPointsClosed(Rules.CASTLE_POINTS_CLOSED);
        super.setPointsOpen(Rules.CASTLE_POINTS_OPEN);
        this.HAS_SHIELD = hasShield;
    }

    public boolean hasShield() {
        return HAS_SHIELD;
    }
}
