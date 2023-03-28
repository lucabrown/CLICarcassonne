package luca.carcassonne.mcts;

import luca.carcassonne.tile.Coordinates;

public class Move {
    private Coordinates coordinates;
    private String tileId;
    private int rotation;
    private int playerIndex;
    private int featureIndex;

    public Move(Coordinates coordinates, String tileId, int rotation, int playerIndex, int featureIndex) {
        this.coordinates = coordinates;
        this.tileId = tileId;
        this.rotation = rotation;
        this.playerIndex = playerIndex;
        this.featureIndex = featureIndex;
    }

    public Move(Coordinates coordinates, String tileId, int rotation, int playerIndex) {
        this.coordinates = coordinates;
        this.tileId = tileId;
        this.rotation = rotation;
        this.playerIndex = playerIndex;
        this.featureIndex = -1;
    }

    public Move() {
        this.coordinates = null;
        this.tileId = null;
        this.rotation = 0;
        this.featureIndex = -1;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public String getTileId() {
        return tileId;
    }

    public void setTileId(String tileId) {
        this.tileId = tileId;
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
        return "Move [" + coordinates + ", " + tileId + ", r" + rotation + ", p" + playerIndex
                + ", f" + featureIndex + "]";
    }
}
