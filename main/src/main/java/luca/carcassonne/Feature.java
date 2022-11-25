package luca.carcassonne;

import java.util.ArrayList;

public abstract class Feature {
    private ArrayList<CardinalPoint> cardinalPoints;
    private static Integer pointsOpen;
    private static Integer pointsClosed;
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

    public boolean isOpen() {
        return isOpen;
    }

    protected static void setPointsOpen(Integer pointsOpen) {
        Feature.pointsOpen = pointsOpen;
    }

    protected static void setPointsClosed(Integer pointsClosed) {
        Feature.pointsClosed = pointsClosed;
    }

    public static Integer getPointsOpen() {
        return pointsOpen;
    }

    public static Integer getPointsClosed() {
        return pointsClosed;
    }

}
