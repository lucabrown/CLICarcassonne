package luca.carcassonne.tile;

import java.util.ArrayList;
import java.util.HashSet;

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
    public Object clone() throws CloneNotSupportedException {
        Tile newTile = (Tile) super.clone();
        newTile.id = id;
        newTile.coordinates = (Coordinates) coordinates.clone();
        newTile.sideFeatures = (ArrayList<SideFeature>) sideFeatures.clone();
        newTile.features = (HashSet<Feature>) features.clone();

        return newTile;
    }

}