package luca.carcassonne;

import java.util.ArrayList;
import java.util.HashSet;

// The main Tile object. The 4 sides are represented by side features, arranged clockwise 
// from the top: north, east, south, west.
public class Tile {
    private ArrayList<SideFeature> sideFeatures;
    private Coordinates coordinates;
    private ArrayList<Coordinates> adjacentCoordinates;
    private HashSet<Feature> features;
    private Player owner;

    Tile(SideFeature north, SideFeature east, SideFeature south, SideFeature west) {
        this.sideFeatures = new ArrayList<>() {{
            add(north);
            add(east);
            add(south);
            add(west);
        }};
        adjacentCoordinates = new ArrayList<>();
        features = new HashSet<>();
    }

    Tile(SideFeature north, SideFeature east, SideFeature south, SideFeature west, HashSet<Feature> features) {
        this.sideFeatures = new ArrayList<>() {{
            add(north);
            add(east);
            add(south);
            add(west);
        }};
        adjacentCoordinates = new ArrayList<>();
        this.features = features;
    }

    Tile(Integer x, Integer y) {
        this.coordinates = new Coordinates(x, y);
        this.sideFeatures = new ArrayList<SideFeature>() {
            {
                add(SideFeature.CASTLE);
                add(SideFeature.ROAD);
                add(SideFeature.FIELD);
                add(SideFeature.ROAD);
            }
        };
        this.adjacentCoordinates = new ArrayList<Coordinates>() {
            {
                add(new Coordinates(x, y + 1));
                add(new Coordinates(x + 1, y));
                add(new Coordinates(x, y - 1));
                add(new Coordinates(x - 1, y));
            }
        };
        features = new HashSet<Feature>() {{
            HashSet<Castle> castles = new HashSet<Castle>() {{
                add(new Castle(new ArrayList<CardinalPoint>() {{
                    add(CardinalPoint.NNE);
                    add(CardinalPoint.N);
                    add(CardinalPoint.NNW);
                }}));
            }};
            add(new Field(new ArrayList<CardinalPoint>() {{
                add(CardinalPoint.ENE);
                add(CardinalPoint.WNW);
            }}, castles));
            add(new Road(new ArrayList<CardinalPoint>() {{
                add(CardinalPoint.E);
                add(CardinalPoint.W);
            }}));
            add(new Field(new ArrayList<CardinalPoint>() {{
                add(CardinalPoint.ESE);
                add(CardinalPoint.SSE);
                add(CardinalPoint.S);
                add(CardinalPoint.SSW);
                add(CardinalPoint.WSW);
            }}, new HashSet<Castle>()));
            add(castles.iterator().next());
        }};
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
        int x = coordinates.getX();
        int y = coordinates.getY();
        adjacentCoordinates = new ArrayList<Coordinates>() {
            {
                add(new Coordinates(x, y + 1));
                add(new Coordinates(x + 1, y));
                add(new Coordinates(x, y - 1));
                add(new Coordinates(x - 1, y));
            }
        };
    }

    public ArrayList<Coordinates> getAdjacentCoordinates() {
        return adjacentCoordinates;
    }

    public void rotateClockwise(int times) {
        SideFeature west;
        for(int i = 0; i < times; i++){
            west = sideFeatures.remove(3);
            sideFeatures.add(0, west);
            rotateSideFeaturesClockwise();
        }
    }

    private void rotateSideFeaturesClockwise() {
        for (Feature feature : features) {
            ArrayList<CardinalPoint> cardinalPoints = feature.getCardinalPoints();

            for(CardinalPoint point : cardinalPoints){
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

    @Override
    public String toString() {
        return "Tile" + sideFeatures;
    }
}