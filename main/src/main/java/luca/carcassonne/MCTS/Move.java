package luca.carcassonne.mcts;

import luca.carcassonne.tile.Coordinates;
import luca.carcassonne.tile.Tile;
import luca.carcassonne.tile.feature.Feature;

public class Move {
    private Coordinates coordinates;
    private Tile tile;
    private int rotation;
    private int playerIndex;
    private int featureIndex;

    public Move(Coordinates coordinates, Tile tile, int rotation, int playerIndex, int featureIndex) {
        this.coordinates = coordinates;
        this.tile = tile;
        this.rotation = rotation;
        this.playerIndex = playerIndex;
        this.featureIndex = featureIndex;
    }

    public Move(Coordinates coordinates, Tile tile, int rotation, int playerIndex) {
        this.coordinates = coordinates;
        this.tile = tile;
        this.rotation = rotation;
        this.playerIndex = playerIndex;
        this.featureIndex = -1;
    }

    public Move() {
        this.coordinates = null;
        this.tile = null;
        this.rotation = 0;
        this.featureIndex = -1;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public int getFeatureIndex() {
        return featureIndex;
    }

    public void setFeatureIndex(int featureIndex) {
        this.featureIndex = featureIndex;
    }

    @Override
    public String toString() {
        return "Move [" + coordinates + ", " + tile + ", r" + rotation + ", p" + playerIndex
                + ", f" + featureIndex + "]";
    }

    // @Override
    // public Object clone() {
    // Move newMove = new Move();

    // newMove.coordinates = (Coordinates) coordinates.clone();
    // newMove.tile = (Tile) tile.clone();
    // newMove.rotation = rotation;
    // newMove.featureIndex = featureIndex;

    // return newMove;

    // }
}
