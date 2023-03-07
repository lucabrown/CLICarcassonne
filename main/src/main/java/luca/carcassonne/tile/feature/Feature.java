package luca.carcassonne.tile.feature;

import java.util.ArrayList;
import java.util.HashMap;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import luca.carcassonne.player.Player;
import luca.carcassonne.tile.CardinalPoint;
import luca.carcassonne.tile.Tile;

public abstract class Feature {
    protected ArrayList<CardinalPoint> cardinalPoints;
    protected Integer pointsOpen;
    protected Integer pointsClosed;

    Feature(ArrayList<CardinalPoint> cardinalPoints) {
        this.cardinalPoints = cardinalPoints;
    }

    Feature() {
    }

    public ArrayList<CardinalPoint> getCardinalPoints() {
        return cardinalPoints;
    }

    public void setCardinalPoints(int indexOf, CardinalPoint cardinalPoint) {
        this.cardinalPoints.set(indexOf, cardinalPoint);
    }

    public void setCardinalPoints(ArrayList<CardinalPoint> cardinalPoints) {
        this.cardinalPoints = cardinalPoints;
    }

    // public int getOwner() {
    // return owner;
    // }

    // public void setOwner(int owner) {
    // this.owner = owner;
    // }

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

    // @Override
    // public abstract Object clone();

}
