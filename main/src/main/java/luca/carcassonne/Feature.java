package luca.carcassonne;

import java.util.ArrayList;

public abstract class Feature {
    private Player owner;
    private ArrayList<CardinalPoint> cardinalPoints;
    private Integer pointsOpen;
    private Integer pointsClosed;

    Feature(ArrayList<CardinalPoint> cardinalPoints) {
        this.cardinalPoints = cardinalPoints;
    }

    public ArrayList<CardinalPoint> getCardinalPoints() {
        return cardinalPoints;
    }

    public void setCardinalPoints(int indexOf, CardinalPoint cardinalPoint) {
        this.cardinalPoints.set(indexOf, cardinalPoint);
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    protected void setPointsOpen(Integer pointsOpen) {
        this.pointsOpen = pointsOpen;
    }

    protected void setPointsClosed(Integer pointsClosed) {
        this.pointsClosed = pointsClosed;
    }

    public Integer getPointsOpen() {
        return pointsOpen;
    }

    public Integer getPointsClosed() {
        return pointsClosed;
    }

}
