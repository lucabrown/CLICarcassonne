package luca.carcassonne.tile.feature;

import java.util.ArrayList;
import luca.carcassonne.Settings;
import luca.carcassonne.tile.CardinalPoint;

/**
 * A castle feature.
 * 
 * @author Luca Brown
 */
public class Castle extends Feature {
    private boolean HAS_SHIELD;

    public Castle(ArrayList<CardinalPoint> cardinalPoints, boolean hasShield) {
        super(cardinalPoints);
        super.setPointsClosed(Settings.CASTLE_POINTS_CLOSED);
        super.setPointsOpen(Settings.CASTLE_POINTS_OPEN);
        this.HAS_SHIELD = hasShield;
    }

    public boolean hasShield() {
        return HAS_SHIELD;
    }

    public void addCardinalPoint(CardinalPoint cardinalPoint) {
        super.getCardinalPoints().add(cardinalPoint);
    }

}
