package luca.carcassonne.MCTS;

import luca.carcassonne.tile.Coordinates;
import luca.carcassonne.tile.Tile;
import luca.carcassonne.tile.feature.Feature;

public class Move {
    private Coordinates coordinate;
    private Tile tile;
    private int rotation;
    private Feature feature;

    public Move(Coordinates coordinate, Tile tile, int rotation, Feature feature) {
        this.coordinate = coordinate;
        this.tile = tile;
        this.rotation = rotation;
        this.feature = feature;
    }

    public Move(Coordinates coordinate, Tile tile, int rotation) {
        this.coordinate = coordinate;
        this.tile = tile;
        this.rotation = rotation;
        this.feature = null;
    }

    public Coordinates getCoordinate() {
        return coordinate;
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
}
