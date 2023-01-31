package luca.carcassonne.tile.feature;

import java.util.ArrayList;

import luca.carcassonne.Rules;
import luca.carcassonne.tile.CardinalPoint;

public class Road extends Feature{
    public Road(ArrayList<CardinalPoint> cardinalPoints) {
        super(cardinalPoints);
        super.setPointsClosed(Rules.ROAD_POINTS_CLOSED);
        super.setPointsOpen(Rules.ROAD_POINTS_OPEN);
    }
}