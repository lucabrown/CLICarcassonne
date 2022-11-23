package luca.carcassonne;

import java.util.ArrayList;

public abstract class Feature {
    ArrayList<Tile> tilesSpanned;
    ArrayList<CardinalPoint> cardinalPoints;
    ArrayList<Tile> openTiles;

    Feature(Tile tile, ArrayList<CardinalPoint> cardinalPoints) {
        tilesSpanned = new ArrayList<Tile>() {
            {
                add(tile);
            }
        };
        openTiles = new ArrayList<>() {
            {
                add(tile);
            }
        };
    }

    public void addSpannedTile(Tile tile) {
        tilesSpanned.add(tile);
    }
}
