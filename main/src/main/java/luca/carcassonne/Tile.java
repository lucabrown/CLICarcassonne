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
            add(new Field(Tile.this, new ArrayList<CardinalPoint>() {{
                add(CardinalPoint.ENE);
                add(CardinalPoint.WNW);
            }}));
            add(new Road(Tile.this, new ArrayList<CardinalPoint>() {{
                add(CardinalPoint.E);
                add(CardinalPoint.W);
            }}));
            add(new Field(Tile.this, new ArrayList<CardinalPoint>() {{
                add(CardinalPoint.ESE);
                add(CardinalPoint.SSE);
                add(CardinalPoint.S);
                add(CardinalPoint.SSW);
                add(CardinalPoint.WSW);
            }}));
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
        return "Tile" + coordinates.toString();
    }
}