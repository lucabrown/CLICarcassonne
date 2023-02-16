package luca.carcassonne.tile.feature;

import java.util.ArrayList;
import java.util.HashMap;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import luca.carcassonne.player.Player;
import luca.carcassonne.tile.CardinalPoint;
import luca.carcassonne.tile.Tile;

public abstract class Feature implements Cloneable {
    private Player owner;
    private ArrayList<CardinalPoint> cardinalPoints;
    private Tile belongingTile;
    private Integer pointsOpen;
    private Integer pointsClosed;

    Feature(ArrayList<CardinalPoint> cardinalPoints) {
        this.cardinalPoints = cardinalPoints;
        this.belongingTile = null;
    }

    // Returns a map of players and the number of meeples they have on the feature
    public static HashMap<Player, Integer> getPlayersOnFeature(SimpleGraph<Feature, DefaultEdge> feature) {
        HashMap<Player, Integer> players = new HashMap<>();

        // Map each player to the number of meeples they have on the feature
        feature.vertexSet().stream().map(v -> v.getOwner()).forEach(p -> {
            if (players.containsKey(p)) {
                players.put(p, players.get(p) + 1);
            } else {
                if (p != null) {
                    players.put(p, 1);
                }
            }
        });

        return players;
    }

    public ArrayList<CardinalPoint> getCardinalPoints() {
        return cardinalPoints;
    }

    public void setCardinalPoints(int indexOf, CardinalPoint cardinalPoint) {
        this.cardinalPoints.set(indexOf, cardinalPoint);
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

    @Override
    @SuppressWarnings("unchecked")
    public Object clone() throws CloneNotSupportedException {
        Feature newFeature = (Feature) super.clone();

        newFeature.owner = this.owner;
        newFeature.cardinalPoints = (ArrayList<CardinalPoint>) this.cardinalPoints.clone();
        newFeature.pointsClosed = this.pointsClosed;
        newFeature.pointsOpen = this.pointsOpen;
        newFeature.belongingTile = this.belongingTile;

        return newFeature;
    }

}
