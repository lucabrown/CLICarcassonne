package luca.carcassonne.tile.feature;

import java.util.ArrayList;

import luca.carcassonne.Settings;
import luca.carcassonne.tile.CardinalPoint;

public class Road extends Feature {
    public Road(ArrayList<CardinalPoint> cardinalPoints) {
        super(cardinalPoints);
        super.setPointsClosed(Settings.ROAD_POINTS_CLOSED);
        super.setPointsOpen(Settings.ROAD_POINTS_OPEN);
    }

    public Road() {
        super();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object clone() {
        Road newRoad = new Road();

        newRoad.cardinalPoints = (ArrayList<CardinalPoint>) this.cardinalPoints.clone();
        newRoad.pointsClosed = this.pointsClosed;
        newRoad.pointsOpen = this.pointsOpen;

        return newRoad;
    }
}
