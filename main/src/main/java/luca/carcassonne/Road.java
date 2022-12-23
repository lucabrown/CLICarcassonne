package luca.carcassonne;

import java.util.ArrayList;

public class Road extends Feature{
    Road(ArrayList<CardinalPoint> cardinalPoints) {
        super(cardinalPoints);
        super.setPointsClosed(Rules.ROAD_POINTS_CLOSED);
        super.setPointsOpen(Rules.ROAD_POINTS_OPEN);
    }
}
