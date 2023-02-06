package luca.carcassonne.tile.feature;

import java.util.ArrayList;

import luca.carcassonne.Settings;
import luca.carcassonne.tile.CardinalPoint;

public class Road extends Feature{
    public Road(ArrayList<CardinalPoint> cardinalPoints) {
        super(cardinalPoints);
        super.setPointsClosed(Settings.ROAD_POINTS_CLOSED);
        super.setPointsOpen(Settings.ROAD_POINTS_OPEN);
    }

}
