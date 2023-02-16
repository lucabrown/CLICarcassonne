package luca.carcassonne.tile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import luca.carcassonne.Settings;
import luca.carcassonne.player.Player;
import luca.carcassonne.tile.feature.Feature;

// The main Tile object. The 4 sides are represented by side features, arranged clockwise 
// from the top: north, east, south, west.
public class Tile implements Cloneable {
    private String id;
    private ArrayList<SideFeature> sideFeatures;
    private Coordinates coordinates;
    private HashSet<Feature> features;
    private Player owner;

    public Tile(SideFeature north, SideFeature east, SideFeature south, SideFeature west) {
        this.sideFeatures = new ArrayList<>() {
            {
                add(north);
                add(east);
                add(south);
                add(west);
            }
        };
        features = new HashSet<>();
    }

    public Tile(SideFeature north, SideFeature east, SideFeature south, SideFeature west, HashSet<Feature> features,
            String id) {
        this.sideFeatures = new ArrayList<>() {
            {
                add(north);
                add(east);
                add(south);
                add(west);
            }
        };
        this.features = features;
        this.id = id;
    }

    public Tile() {
        this.id = null;
        this.sideFeatures = null;
        this.coordinates = null;
        this.features = null;
        this.owner = null;
    }

    public Tile(Integer x, Integer y) {
        this.coordinates = new Coordinates(x, y);
        this.sideFeatures = Settings.getStartingTile().getSideFeatures();
        features = Settings.getStartingTile().getFeatures();
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public ArrayList<Coordinates> getAdjacentCoordinates() {
        return new ArrayList<Coordinates>() {
            {
                add(new Coordinates(coordinates.getX(), coordinates.getY() + 1));
                add(new Coordinates(coordinates.getX() + 1, coordinates.getY()));
                add(new Coordinates(coordinates.getX(), coordinates.getY() - 1));
                add(new Coordinates(coordinates.getX() - 1, coordinates.getY()));
            }
        };
    }

    public void rotateClockwise(int times) {
        SideFeature west;
        for (int i = 0; i < times; i++) {
            west = sideFeatures.remove(3);
            sideFeatures.add(0, west);
            rotateSideFeaturesClockwise();
        }
    }

    public void rotateClockwise() {
        rotateClockwise(1);
    }

    private void rotateSideFeaturesClockwise() {
        for (Feature feature : features) {
            ArrayList<CardinalPoint> cardinalPoints = feature.getCardinalPoints();

            for (CardinalPoint point : cardinalPoints) {
                switch (point) {
                    case NNE:
                        feature.setCardinalPoints(cardinalPoints.indexOf(point), CardinalPoint.ESE);
                        break;
                    case ENE:
                        feature.setCardinalPoints(cardinalPoints.indexOf(point), CardinalPoint.SSE);
                        break;
                    case ESE:
                        feature.setCardinalPoints(cardinalPoints.indexOf(point), CardinalPoint.SSW);
                        break;
                    case SSE:
                        feature.setCardinalPoints(cardinalPoints.indexOf(point), CardinalPoint.WSW);
                        break;
                    case SSW:
                        feature.setCardinalPoints(cardinalPoints.indexOf(point), CardinalPoint.WNW);
                        break;
                    case WSW:
                        feature.setCardinalPoints(cardinalPoints.indexOf(point), CardinalPoint.NNW);
                        break;
                    case WNW:
                        feature.setCardinalPoints(cardinalPoints.indexOf(point), CardinalPoint.NNE);
                        break;
                    case NNW:
                        feature.setCardinalPoints(cardinalPoints.indexOf(point), CardinalPoint.ENE);
                        break;
                    case N:
                        feature.setCardinalPoints(cardinalPoints.indexOf(point), CardinalPoint.E);
                        break;
                    case E:
                        feature.setCardinalPoints(cardinalPoints.indexOf(point), CardinalPoint.S);
                        break;
                    case S:
                        feature.setCardinalPoints(cardinalPoints.indexOf(point), CardinalPoint.W);
                        break;
                    case W:
                        feature.setCardinalPoints(cardinalPoints.indexOf(point), CardinalPoint.N);
                        break;
                }
            }
        }
    }

    public HashSet<Feature> getFeatures() {
        return features;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public SideFeature getNorthSideFeature() {
        return sideFeatures.get(0);
    }

    public SideFeature getSouthSideFeature() {
        return sideFeatures.get(2);
    }

    public SideFeature getEastSideFeature() {
        return sideFeatures.get(1);
    }

    public SideFeature getWestSideFeature() {
        return sideFeatures.get(3);
    }

    public ArrayList<SideFeature> getSideFeatures() {
        return sideFeatures;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Tile" + sideFeatures;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object clone() {
        Tile newTile = new Tile();
        newTile.id = id;
        if (coordinates != null) {
            newTile.coordinates = (Coordinates) coordinates.clone();
        } else {
            newTile.coordinates = null;
        }
        newTile.sideFeatures = (ArrayList<SideFeature>) sideFeatures.clone();
        newTile.features = (HashSet<Feature>) features.stream().map(f -> (Feature) f.clone())
                .collect(Collectors.toCollection(HashSet::new));

        return newTile;
    }
}