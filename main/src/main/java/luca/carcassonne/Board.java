package luca.carcassonne;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

// Holds all played tiles and tracks the relation between them
public class Board {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RED = "\u001B[31m";

    private Integer height;
    private Integer width;
    private Integer maxY;
    private Integer maxX;
    private Integer minY;
    private Integer minX;
    private Tile startingTile;
    private ArrayList<Tile> placedTiles;
    private HashSet<SimpleGraph<Feature, DefaultEdge>> features;
    private ArrayList<Coordinates> possibleCoordinates;

    public Board() {
        this.height = 1;
        this.width = 1;
        this.startingTile = new Tile(0, 0);
        this.maxY = startingTile.getCoordinates().getY();
        this.maxX = startingTile.getCoordinates().getX();
        this.minY = maxY;
        this.minX = maxX;
        placedTiles = new ArrayList<>() {
            {
                add(startingTile);
            }
        };
        possibleCoordinates = new ArrayList<>() {
            {
                startingTile.getAdjacentCoordinates().forEach(coordinate -> {
                    add(coordinate);
                });
            }
        };
        this.features = new HashSet<>();
        for (Feature feature : startingTile.getFeatures()) {
            SimpleGraph<Feature, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
            graph.addVertex(feature);
            features.add(graph);
        }
    }

    public Tile getStartingTile() {
        return startingTile;
    }

    // Tries to place a tile in the given coordinates. Returns true if the placement
    // was legal.
    public boolean placeTile(Coordinates coordinates, Tile newTile) {
        if (tilePlacementLegal(coordinates, newTile)) {
            newTile.setCoordinates(coordinates);
            System.out.println("Number of features: " + features.size());

            updateBoard(newTile);
            updateFeatures(newTile);
            return true;
        }

        return false;

    }

    // Updates the board's state with the new tile
    private void updateBoard(Tile newTile) {
        placedTiles.add(newTile);
        possibleCoordinates.remove(newTile.getCoordinates());
        System.out.println("Current tile: " + newTile + " at " + newTile.getCoordinates());


        // For each new adjacent coordinate, add it to the list of possible coordinates
        newTile.getAdjacentCoordinates().forEach(coordinates -> {
            if (!possibleCoordinates.contains(coordinates)
                    && !placedTiles.stream().anyMatch(tile -> tile.getCoordinates().equals(coordinates))) {
                possibleCoordinates.add(coordinates);
            }
        });

        updateBoardStringParameters(newTile);
    }

    // Updates the set of features by connecting the new features
    private void updateFeatures(Tile newTile) {
        List<Tile> tilesToCheck = placedTiles.stream()
                .filter(e -> e.getAdjacentCoordinates().contains(newTile.getCoordinates()))
                .collect(Collectors.toList());

        for (Tile tile : tilesToCheck) {
            int position = getRelativePosition(tile, newTile.getCoordinates());

            linkFeatures(tile, newTile, position);
        }
    }

    // Checks if the tile's placement is legal
    private boolean tilePlacementLegal(Coordinates coordinates, Tile tile) {
        if (possibleCoordinates.contains(coordinates)) {
            List<Tile> tilesToCheck = placedTiles.stream()
                    .filter(e -> e.getAdjacentCoordinates().contains(coordinates))
                    .collect(Collectors.toList());

            for (Tile t : tilesToCheck) {
                int position = getRelativePosition(t, coordinates);

                switch (position) {
                    case 0:
                        if (tile.getNorthSideFeature() != t.getSouthSideFeature()) {
                            return false;
                        }
                        break;
                    case 1:
                        if (tile.getEastSideFeature() != t.getWestSideFeature()) {
                            return false;
                        }
                        break;
                    case 2:
                        if (tile.getSouthSideFeature() != t.getNorthSideFeature()) {
                            return false;
                        }
                        break;
                    case 3:
                        if (tile.getWestSideFeature() != t.getEastSideFeature()) {
                            return false;
                        }
                        break;
                    default:
                        return false;
                }
            }

            return true;
        }

        return false;
    }

    // Links the new features to the existing graphs.
    private void linkFeatures(Tile tile, Tile newTile, int position) {
        boolean featurePlaced = false;

        for (Feature newFeature : newTile.getFeatures()) {
            switch (position) {
                case 0:
                    for (Feature feature : tile.getFeatures()) {
                        if (feature.getClass() == newFeature.getClass()
                                && feature.getCardinalPoints().contains(CardinalPoint.SSW)
                                && newFeature.getCardinalPoints().contains(CardinalPoint.NNW)) {
                            addFeaturesEdge(feature, newFeature);
                            featurePlaced = true;
                        } else if (feature.getClass() == newFeature.getClass()
                                && feature.getCardinalPoints().contains(CardinalPoint.S)
                                && newFeature.getCardinalPoints().contains(CardinalPoint.N)) {
                            addFeaturesEdge(feature, newFeature);
                            featurePlaced = true;
                        } else if (feature.getClass() == newFeature.getClass()
                                && feature.getCardinalPoints().contains(CardinalPoint.SSE)
                                && newFeature.getCardinalPoints().contains(CardinalPoint.NNE)) {
                            addFeaturesEdge(feature, newFeature);
                            featurePlaced = true;
                        }
                    }
                    break;
                case 1:
                    for (Feature feature : tile.getFeatures()) {
                        if (feature.getClass() == newFeature.getClass()
                                && feature.getCardinalPoints().contains(CardinalPoint.WSW)
                                && newFeature.getCardinalPoints().contains(CardinalPoint.ESE)) {
                            addFeaturesEdge(feature, newFeature);
                            featurePlaced = true;
                        } else if (feature.getClass() == newFeature.getClass()
                                && feature.getCardinalPoints().contains(CardinalPoint.W)
                                && newFeature.getCardinalPoints().contains(CardinalPoint.E)) {
                            addFeaturesEdge(feature, newFeature);
                            featurePlaced = true;
                        } else if (feature.getClass() == newFeature.getClass()
                                && feature.getCardinalPoints().contains(CardinalPoint.WNW)
                                && newFeature.getCardinalPoints().contains(CardinalPoint.ENE)) {
                            addFeaturesEdge(feature, newFeature);
                            featurePlaced = true;
                        }
                    }
                    break;
                case 2:
                    for (Feature feature : tile.getFeatures()) {
                        if (feature.getClass() == newFeature.getClass()
                                && feature.getCardinalPoints().contains(CardinalPoint.NNE)
                                && newFeature.getCardinalPoints().contains(CardinalPoint.SSE)) {
                            addFeaturesEdge(feature, newFeature);
                            featurePlaced = true;
                        } else if (feature.getClass() == newFeature.getClass()
                                && feature.getCardinalPoints().contains(CardinalPoint.N)
                                && newFeature.getCardinalPoints().contains(CardinalPoint.S)) {
                            addFeaturesEdge(feature, newFeature);
                            featurePlaced = true;
                        } else if (feature.getClass() == newFeature.getClass()
                                && feature.getCardinalPoints().contains(CardinalPoint.NNW)
                                && newFeature.getCardinalPoints().contains(CardinalPoint.SSW)) {
                            addFeaturesEdge(feature, newFeature);
                            featurePlaced = true;
                        }
                    }
                    break;
                case 3:
                    for (Feature feature : tile.getFeatures()) {
                        if (feature.getClass() == newFeature.getClass()
                                && feature.getCardinalPoints().contains(CardinalPoint.ESE)
                                && newFeature.getCardinalPoints().contains(CardinalPoint.WSW)) {
                            addFeaturesEdge(feature, newFeature);
                            featurePlaced = true;
                        } else if (feature.getClass() == newFeature.getClass()
                                && feature.getCardinalPoints().contains(CardinalPoint.E)
                                && newFeature.getCardinalPoints().contains(CardinalPoint.W)) {
                            addFeaturesEdge(feature, newFeature);
                            featurePlaced = true;
                        } else if (feature.getClass() == newFeature.getClass()
                                && feature.getCardinalPoints().contains(CardinalPoint.ENE)
                                && newFeature.getCardinalPoints().contains(CardinalPoint.WNW)) {
                            addFeaturesEdge(feature, newFeature);
                            featurePlaced = true;
                        }
                    }
                    break;
            }

            if (!featurePlaced) {
                SimpleGraph<Feature, DefaultEdge> newGraph = new SimpleGraph<>(DefaultEdge.class);
                System.out.println("Creating new: " + newFeature.getClass().getSimpleName());
                newGraph.addVertex(newFeature);
                features.add(newGraph);
            }
        }
    }

    private void addFeaturesEdge(Feature feature, Feature newFeature) {
        for (SimpleGraph<Feature, DefaultEdge> graph : features) {
            if (graph.containsVertex(feature) && !graph.containsVertex(newFeature)) {
                System.out.println("Adding edge between " + feature.getClass().getSimpleName() + " and " + newFeature.getClass().getSimpleName());
                graph.addVertex(newFeature);
                graph.addEdge(feature, newFeature);
            }
        }
    }

    // Returns a number between 0 and 3 that represents the clockwise position of a
    // tile relative to the given coordinates
    private int getRelativePosition(Tile tile, Coordinates coordinates) {
        int pos = -1;
        int xDifference = tile.getCoordinates().getX() - coordinates.getX();
        int yDifference = tile.getCoordinates().getY() - coordinates.getY();

        if (yDifference == 1) {
            pos = 0;
        } else if (xDifference == 1) {
            pos = 1;
        } else if (yDifference == -1) {
            pos = 2;
        } else if (xDifference == -1) {
            pos = 3;
        }

        return pos;
    }

    public Tile getTileFromCoordinates(Coordinates coordinates) {
        return placedTiles.stream().filter(tile -> tile.getCoordinates().equals(coordinates)).findFirst().orElse(null);
    }

    public ArrayList<Coordinates> getPossibleCoordinates() {
        return possibleCoordinates;
    }

    public int getPlacedTilesSize() {
        return placedTiles.size();
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    // * * * * * * *
    // * PRINTING METHODS *
    // * * * * * * *

    // Prints the board with the option to highlight a feature
    public void printBoard(SideFeature... highlightedFeatures) {
        String defaultColour = ANSI_RESET;
        String highlightColour = defaultColour;
        SideFeature SideFeature = highlightedFeatures.length > 0 ? highlightedFeatures[0] : null;

        if (SideFeature != null) {
            switch (SideFeature) {
                case CASTLE:
                    highlightColour = ANSI_RED;
                    break;
                case ROAD:
                    highlightColour = ANSI_RESET;
                    break;
                case FIELD:
                    highlightColour = ANSI_GREEN;
                    break;
                default:
                    break;
            }
        }

        List<Coordinates> coordinates = placedTiles.stream()
                .map(t -> t.getCoordinates())
                .collect(Collectors.toList());

        System.out.println("\n");
        double n = 0.0;
        for (int i = maxY; i >= minY; i--) {
            for (int j = minX; j <= maxX; j++) {
                Coordinates c = new Coordinates(j, i);
                if (j == 0 && i == 0) {
                    System.out.print(defaultColour + "O " + ANSI_RESET);
                } else if (coordinates.contains(c)) {
                    if (SideFeature != null && getTileFromCoordinates(c).getSideFeatures().contains(SideFeature)) {
                        System.out.print(highlightColour + "X " + ANSI_RESET);
                        n += 1;
                    } else {
                        System.out.print(
                                getTileFromCoordinates(c).getOwner().getColour().getSymbol() + "X " + ANSI_RESET);
                    }
                } else {
                    System.out.print(ANSI_CYAN + ". " + ANSI_RESET);
                }
            }

            System.out.println();
        }
        System.out.println("Ratio: " + n / (placedTiles.size() - 1));

        // Print highlight, height and width
        if (SideFeature != null) {
            System.out.println("Highlighting " + highlightColour + SideFeature.getSymbol() + "s" + ANSI_RESET + ".");
        }

        System.out.println("Height: " + height);
        System.out.println("Width: " + width);
    }

    // Keeps track of the board's width and height
    private void updateBoardStringParameters(Tile tile) {
        int x = tile.getCoordinates().getX();
        int y = tile.getCoordinates().getY();

        if (x > maxX) {
            maxX = x;
            width++;
        } else if (y > maxY) {
            maxY = y;
            height++;
        } else if (x < minX) {
            minX = x;
            width++;
        } else if (y < minY) {
            minY = y;
            height++;
        }
    }
}
