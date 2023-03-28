package luca.carcassonne.tile.feature;

import java.util.ArrayList;
import luca.carcassonne.player.Player;
import luca.carcassonne.tile.CardinalPoint;
import luca.carcassonne.tile.Tile;

public abstract class Feature {
    protected Player owner;
    protected ArrayList<CardinalPoint> cardinalPoints;
    protected Tile belongingTile;
    protected Integer pointsOpen;
    protected Integer pointsClosed;

    Feature(ArrayList<CardinalPoint> cardinalPoints) {
        this.cardinalPoints = cardinalPoints;
        this.belongingTile = null;
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

    public Tile getBelongingTile() {
        return belongingTile;
    }

    public void setBelongingTile(Tile belongingTile) {
        this.belongingTile = belongingTile;
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
