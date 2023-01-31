package luca.carcassonne.tile.feature;

import java.util.ArrayList;

import luca.carcassonne.Rules;
import luca.carcassonne.tile.CardinalPoint;

public class Castle extends Feature{
    private boolean HAS_SHIELD;

    public Castle(ArrayList<CardinalPoint> cardinalPoints, boolean hasShield) {
        super(cardinalPoints);
        super.setPointsClosed(Rules.CASTLE_POINTS_CLOSED);
        super.setPointsOpen(Rules.CASTLE_POINTS_OPEN);
        this.HAS_SHIELD = hasShield;
    }

    public boolean hasShield() {
        return HAS_SHIELD;
    }
}
