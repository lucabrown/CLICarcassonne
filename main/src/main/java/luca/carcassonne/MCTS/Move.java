package luca.carcassonne.MCTS;

import luca.carcassonne.tile.Coordinates;
import luca.carcassonne.tile.Tile;
import luca.carcassonne.tile.feature.Feature;

public class Move implements Cloneable {
    private Coordinates coordinates;
    private Tile tile;
    private int rotation;
    private Feature feature;

    public Move(Coordinates coordinates, Tile tile, int rotation, Feature feature) {
        this.coordinates = coordinates;
        this.tile = tile;
        this.rotation = rotation;
        this.feature = feature;
    }

    public Move(Coordinates coordinates, Tile tile, int rotation) {
        this.coordinates = coordinates;
        this.tile = tile;
        this.rotation = rotation;
        this.feature = null;
    }

    public Move() {
        this.coordinates = null;
        this.tile = null;
        this.rotation = 0;
        this.feature = null;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Tile getTile() {
        return tile;
    }

    public int getRotation() {
        return rotation;
    }

    public Feature getFeature() {
        return feature;
    }

    @Override
    public String toString() {
        return "Move [" + coordinates + ", " + tile + ", " + feature.getClass().getSimpleName() + "]";
    }

    @Override
    public Object clone() {
        Move newMove = new Move();

        newMove.coordinates = (Coordinates) coordinates.clone();
        newMove.tile = (Tile) tile.clone();
        newMove.rotation = rotation;
        newMove.feature = feature;

        return newMove;

    }
}
