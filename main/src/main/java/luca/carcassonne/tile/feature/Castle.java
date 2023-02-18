package luca.carcassonne.tile.feature;

import java.util.ArrayList;
import java.util.stream.Collectors;

import luca.carcassonne.Settings;
import luca.carcassonne.tile.CardinalPoint;

public class Castle extends Feature {
    private boolean HAS_SHIELD;

    public Castle(ArrayList<CardinalPoint> cardinalPoints, boolean hasShield) {
        super(cardinalPoints);
        super.setPointsClosed(Settings.CASTLE_POINTS_CLOSED);
        super.setPointsOpen(Settings.CASTLE_POINTS_OPEN);
        this.HAS_SHIELD = hasShield;
    }

    public Castle() {
        super();
    }

    public boolean hasShield() {
        return HAS_SHIELD;
    }

    public void addCardinalPoint(CardinalPoint cardinalPoint) {
        super.getCardinalPoints().add(cardinalPoint);
    }

    // @Override
    // @SuppressWarnings("unchecked")
    // public Object clone() {
    // Castle newCastle = new Castle();

    // newCastle.cardinalPoints = (ArrayList<CardinalPoint>)
    // this.cardinalPoints.clone();
    // newCastle.pointsClosed = this.pointsClosed;
    // newCastle.pointsOpen = this.pointsOpen;
    // newCastle.HAS_SHIELD = this.HAS_SHIELD;

    // return newCastle;
    // }

}
