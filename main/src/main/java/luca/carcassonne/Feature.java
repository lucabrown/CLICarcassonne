package luca.carcassonne;

import java.util.ArrayList;

public abstract class Feature {
    private ArrayList<CardinalPoint> cardinalPoints;
    private boolean isOpen;

    Feature(ArrayList<CardinalPoint> cardinalPoints) {
        this.cardinalPoints = cardinalPoints;
        this.isOpen = true;
    }

    public ArrayList<CardinalPoint> getCardinalPoints() {
        return cardinalPoints;
    }

    public void setCardinalPoints(int indexOf, CardinalPoint cardinalPoint) {
        this.cardinalPoints.set(indexOf, cardinalPoint);
    }

    public void setClosed() {
        this.isOpen = false;
    }


}
