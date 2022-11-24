package luca.carcassonne;

import java.util.ArrayList;

public class Road extends Feature{
    private ArrayList<CardinalPoint> cardinalPoints;
    private boolean isOpen;

    Road(ArrayList<CardinalPoint> cardinalPoints) {
        super(cardinalPoints);
    }
}
