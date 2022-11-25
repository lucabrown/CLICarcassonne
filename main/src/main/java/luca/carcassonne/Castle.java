package luca.carcassonne;

import java.util.ArrayList;

public class Castle extends Feature{
    private ArrayList<CardinalPoint> cardinalPoints;
    private boolean isOpen;

    Castle(ArrayList<CardinalPoint> cardinalPoints) {
        super(cardinalPoints);
        super.setPointsClosed(4);
        super.setPointsOpen(2);
        
    }
}
