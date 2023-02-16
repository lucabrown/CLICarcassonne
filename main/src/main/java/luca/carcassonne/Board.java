package luca.carcassonne;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import luca.carcassonne.player.Player;
import luca.carcassonne.tile.CardinalPoint;
import luca.carcassonne.tile.Coordinates;
import luca.carcassonne.tile.SideFeature;
import luca.carcassonne.tile.Tile;
import luca.carcassonne.tile.feature.Castle;
import luca.carcassonne.tile.feature.Feature;
import luca.carcassonne.tile.feature.Field;
import luca.carcassonne.tile.feature.Monastery;
import luca.carcassonne.tile.feature.Road;

// Holds all played tiles and tracks the relation between them
public class Board implements Cloneable {
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
    private HashSet<Tile> monasteryTiles;
    private HashSet<SimpleGraph<Feature, DefaultEdge>> openFeatures;
    private HashSet<SimpleGraph<Feature, DefaultEdge>> closedFeatures;
    private HashSet<SimpleGraph<Feature, DefaultEdge>> newlyClosedFeatures;
    private ArrayList<Coordinates> possibleCoordinates;

    public Board(Tile startingTile) {
        this.height = 1;
        this.width = 1;
        this.startingTile = startingTile;
        startingTile.setCoordinates(new Coordinates(0, 0));
        this.maxY = startingTile.getCoordinates().getY();
        this.maxX = startingTile.getCoordinates().getX();
        this.minY = maxY;
        this.minX = maxX;
        placedTiles = new ArrayList<>() {
            {
                add(startingTile);
            }
        };
        monasteryTiles = new HashSet<>() {
            {
                if (startingTile.getFeatures().stream().anyMatch(feature -> feature.getClass() == Monastery.class)) {
                    add(startingTile);
                }
            }
        };
        possibleCoordinates = new ArrayList<>() {
            {
                startingTile.getAdjacentCoordinates().forEach(coordinate -> {
                    add(coordinate);
                });
            }
        };
        this.openFeatures = new HashSet<>();
        for (Feature feature : startingTile.getFeatures()) {
            SimpleGraph<Feature, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
            graph.addVertex(feature);
            openFeatures.add(graph);
            feature.setBelongingTile(startingTile);
        }
        this.closedFeatures = new HashSet<>();
        this.newlyClosedFeatures = new HashSet<>();
    }

    public Board() {
        new Board(Settings.getStartingTile());
    }

    // Tries to place a tile in the given coordinates. Returns true if the placement
    // was legal.
    public boolean placeTile(Coordinates coordinates, Tile newTile) {
        if (tilePlacementLegal(coordinates, newTile)) {
            newTile.setCoordinates(coordinates);

            updateBoard(newTile);
            updateFeatures(newTile);

            return true;
        }

        return false;

    }

    // Tries to place a meeple on a feature
    @SuppressWarnings("unchecked")
    public boolean placeMeeple(Feature newFeature, Player currentPlayer) {
        SimpleGraph<Feature, DefaultEdge> feature = new SimpleGraph<>(DefaultEdge.class);
        HashMap<Player, Integer> players = new HashMap<>();
        // HashSet<SimpleGraph<Feature, DefaultEdge>> allFeatures =
        // (HashSet<SimpleGraph<Feature, DefaultEdge>>) openFeatures
        // .clone();
        HashSet<SimpleGraph<Feature, DefaultEdge>> allFeatures = (HashSet<SimpleGraph<Feature, DefaultEdge>>) openFeatures
                .stream().map(g -> (SimpleGraph<Feature, DefaultEdge>) g.clone())
                .collect(Collectors.toCollection(HashSet::new));
        allFeatures.addAll(closedFeatures);

        if (currentPlayer.getAvailableMeeples() <= 0) {
            return false;
        }

        for (SimpleGraph<Feature, DefaultEdge> f : allFeatures) {
            if (f.containsVertex(newFeature)) {
                feature = f;
                break;
            }
        }

        players = ScoreManager.getPlayersOnFeature(feature);

        if (!players.isEmpty() && !players.containsKey(currentPlayer)) {
            return false;
        }

        currentPlayer.decrementMeeples();
        newFeature.setOwner(currentPlayer);

        return true;
    }

    // Updates the board's state with the new tile
    private void updateBoard(Tile newTile) {
        placedTiles.add(newTile);
        possibleCoordinates.remove(newTile.getCoordinates());

        // For each new adjacent coordinate, add it to the list of possible coordinates
        newTile.getAdjacentCoordinates().forEach(coordinates -> {
            if (!possibleCoordinates.contains(coordinates)
                    && !placedTiles.stream().anyMatch(tile -> tile.getCoordinates().equals(coordinates))) {
                possibleCoordinates.add(coordinates);
            }
        });

        updateBoardStringParameters(newTile);
    }

    // Updates the set of open features by connecting the new open features
    private void updateFeatures(Tile newTile) {
        List<Tile> tilesToCheck = placedTiles.stream()
                .filter(e -> e.getAdjacentCoordinates().contains(newTile.getCoordinates()))
                .collect(Collectors.toCollection(ArrayList::new));

        HashMap<Feature, Integer> featuresToConnect = new HashMap<>();

        for (Feature feature : newTile.getFeatures()) {
            feature.setBelongingTile(newTile);
        }

        for (Tile tile : tilesToCheck) {
            int position = getRelativePosition(tile, newTile.getCoordinates());
            for (Feature feature : tile.getFeatures()) {
                featuresToConnect.put(feature, position);
            }
        }

        linkFeatures(featuresToConnect, newTile);
    }

    // Checks if the tile's placement is legal
    private boolean tilePlacementLegal(Coordinates coordinates, Tile tile) {
        if (possibleCoordinates.contains(coordinates)) {
            List<Tile> tilesToCheck = placedTiles.stream()
                    .filter(e -> e.getAdjacentCoordinates().contains(coordinates))
                    .collect(Collectors.toCollection(ArrayList::new));

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

    // Links the new open features to the existing graphs.
    private void linkFeatures(HashMap<Feature, Integer> featuresToConnect, Tile newTile) {
        boolean featureLinked = false;
        newlyClosedFeatures.clear();

        for (Feature newFeature : newTile.getFeatures()) {
            featureLinked = false;

            for (Feature feature : featuresToConnect.keySet()) {
                switch (featuresToConnect.get(feature)) {
                    case 0:
                        if (featuresMatch(feature, newFeature, CardinalPoint.SSW, CardinalPoint.NNW)) {
                            addFeaturesEdge(feature, newFeature);
                            featureLinked = true;
                        } else if (featuresMatch(feature, newFeature, CardinalPoint.S, CardinalPoint.N)) {
                            addFeaturesEdge(feature, newFeature);
                            featureLinked = true;
                        } else if (featuresMatch(feature, newFeature, CardinalPoint.SSE, CardinalPoint.NNE)) {
                            addFeaturesEdge(feature, newFeature);
                            featureLinked = true;
                        }
                        break;
                    case 1:
                        if (featuresMatch(feature, newFeature, CardinalPoint.WSW, CardinalPoint.ESE)) {
                            addFeaturesEdge(feature, newFeature);
                            featureLinked = true;
                        } else if (featuresMatch(feature, newFeature, CardinalPoint.W, CardinalPoint.E)) {
                            addFeaturesEdge(feature, newFeature);
                            featureLinked = true;
                        } else if (featuresMatch(feature, newFeature, CardinalPoint.WNW, CardinalPoint.ENE)) {
                            addFeaturesEdge(feature, newFeature);
                            featureLinked = true;
                        }
                        break;
                    case 2:
                        if (featuresMatch(feature, newFeature, CardinalPoint.NNE, CardinalPoint.SSE)) {
                            addFeaturesEdge(feature, newFeature);
                            featureLinked = true;
                        } else if (featuresMatch(feature, newFeature, CardinalPoint.N, CardinalPoint.S)) {
                            addFeaturesEdge(feature, newFeature);
                            featureLinked = true;
                        } else if (featuresMatch(feature, newFeature, CardinalPoint.NNW, CardinalPoint.SSW)) {
                            addFeaturesEdge(feature, newFeature);
                            featureLinked = true;
                        }
                        break;
                    case 3:
                        if (featuresMatch(feature, newFeature, CardinalPoint.ESE, CardinalPoint.WSW)) {
                            addFeaturesEdge(feature, newFeature);
                            featureLinked = true;
                        } else if (featuresMatch(feature, newFeature, CardinalPoint.E, CardinalPoint.W)) {
                            addFeaturesEdge(feature, newFeature);
                            featureLinked = true;
                        } else if (featuresMatch(feature, newFeature, CardinalPoint.ENE, CardinalPoint.WNW)) {
                            addFeaturesEdge(feature, newFeature);
                            featureLinked = true;
                        }
                        break;
                }
            }

            if (!featureLinked) {
                SimpleGraph<Feature, DefaultEdge> newGraph = new SimpleGraph<>(DefaultEdge.class);

                if (newFeature instanceof Monastery) {
                    monasteryTiles.add(newTile);
                }

                newGraph.addVertex(newFeature);
                openFeatures.add(newGraph);

            } else if (newFeature.getClass() != Field.class && newFeature.getClass() != Monastery.class) {
                checkIfFeatureIsComplete(newFeature);
            }
        }

        checkIfMonasteriesAreComplete();
    }

    // Finds the graph that contains the new feature and checks if it's closed
    private void checkIfFeatureIsComplete(Feature newFeature) {
        for (SimpleGraph<Feature, DefaultEdge> graph : openFeatures) {
            if (graph.containsVertex(newFeature)) {
                int totalCardinalPoints = graph.vertexSet().stream().mapToInt(f -> f.getCardinalPoints().size()).sum();
                int totalEdges = graph.edgeSet().size();

                if (totalEdges != 0 && totalCardinalPoints % totalEdges == 0
                        && (newFeature.getClass() == Castle.class && totalCardinalPoints / totalEdges == 6
                                || newFeature.getClass() == Road.class && totalCardinalPoints / totalEdges == 2)) {

                    newlyClosedFeatures.add(graph);
                    closedFeatures.add(graph);
                    openFeatures.remove(graph);
                }

                break;
            }
        }
    }

    // Checks if monasteries are closed
    private void checkIfMonasteriesAreComplete() {
        HashSet<Tile> tilesToDelete = new HashSet<>();
        for (Tile monasteryTile : monasteryTiles) {
            if (getSurroundingTiles(monasteryTile) == 8) {
                Iterator<Feature> it = monasteryTile.getFeatures().iterator();
                Feature monastery = it.next();

                while (!(monastery instanceof Monastery)) {
                    monastery = it.next();
                }

                for (SimpleGraph<Feature, DefaultEdge> openGraph : openFeatures) {
                    if (openGraph.containsVertex(monastery)) {

                        newlyClosedFeatures.add(openGraph);
                        closedFeatures.add(openGraph);
                        openFeatures.remove(openGraph);
                        tilesToDelete.add(monasteryTile);

                        break;
                    }
                }
            }
        }

        monasteryTiles.removeAll(tilesToDelete);
    }

    // Checks if two features match given their cardinal points
    private boolean featuresMatch(Feature feature, Feature newFeature, CardinalPoint featureCardinalPoint,
            CardinalPoint newFeatureCardinalPoint) {
        return feature.getClass() == newFeature.getClass()
                && feature.getCardinalPoints().contains(featureCardinalPoint)
                && newFeature.getCardinalPoints().contains(newFeatureCardinalPoint);
    }

    // Connects a feature to an existing graph.
    private void addFeaturesEdge(Feature feature, Feature newFeature) {
        SimpleGraph<Feature, DefaultEdge> belongingGraph = null;
        boolean foundBelongingGraph = false;
        for (SimpleGraph<Feature, DefaultEdge> graph : openFeatures) {
            if (graph.containsVertex(feature) && !graph.containsEdge(feature, newFeature)) {
                for (SimpleGraph<Feature, DefaultEdge> g : openFeatures) {
                    if (g.containsVertex(newFeature)) {
                        belongingGraph = g;
                        foundBelongingGraph = true;
                        break;
                    }
                }

                graph.addVertex(newFeature);
                graph.addEdge(feature, newFeature);

                if (foundBelongingGraph && belongingGraph != graph) {
                    Graphs.addGraph(graph, belongingGraph);
                    openFeatures.remove(belongingGraph);
                }

                break;
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

    public Tile getTileFromFeature(Feature feature) {
        for (Tile tile : placedTiles) {
            for (Feature f : tile.getFeatures()) {
                if (f.equals(feature)) {
                    return tile;
                }
            }
        }

        return null;
    }

    // Returns the number of tiles surrounding the given tile (used for scoring
    // monasteries)
    public int getSurroundingTiles(Tile tile) {
        int nSurroundingTiles = 0;
        HashSet<Coordinates> surroundingCoordinates = new HashSet<>() {
            {
                add(new Coordinates(tile.getCoordinates().getX() + 1, tile.getCoordinates().getY()));
                add(new Coordinates(tile.getCoordinates().getX() - 1, tile.getCoordinates().getY()));
                add(new Coordinates(tile.getCoordinates().getX(), tile.getCoordinates().getY() + 1));
                add(new Coordinates(tile.getCoordinates().getX(), tile.getCoordinates().getY() - 1));
                add(new Coordinates(tile.getCoordinates().getX() + 1, tile.getCoordinates().getY() + 1));
                add(new Coordinates(tile.getCoordinates().getX() - 1, tile.getCoordinates().getY() - 1));
                add(new Coordinates(tile.getCoordinates().getX() + 1, tile.getCoordinates().getY() - 1));
                add(new Coordinates(tile.getCoordinates().getX() - 1, tile.getCoordinates().getY() + 1));

            }
        };

        for (Coordinates c : surroundingCoordinates) {
            if (getTileFromCoordinates(c) != null) {
                nSurroundingTiles++;
            }
        }
        
        return nSurroundingTiles;
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

    public ArrayList<Tile> getPlacedTiles() {
        return placedTiles;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public HashSet<SimpleGraph<Feature, DefaultEdge>> getNewlyClosedFeatures() {
        return newlyClosedFeatures;
    }

    public HashSet<SimpleGraph<Feature, DefaultEdge>> getClosedFeatures() {
        return closedFeatures;
    }

    public HashSet<SimpleGraph<Feature, DefaultEdge>> getOpenFeatures() {
        return openFeatures;
    }

    public boolean isFinished() {
        return placedTiles.size() == 74;
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
                .collect(Collectors.toCollection(ArrayList::new));

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
                        if (getTileFromCoordinates(c).getOwner() == null) {
                            System.out.print("X ");
                        } else {
                            System.out.print(
                                    getTileFromCoordinates(c).getOwner().getColour().getSymbol() + "X " + ANSI_RESET);
                        }
                    }
                } else {
                    System.out.print(ANSI_CYAN + ". " + ANSI_RESET);
                }
            }

            System.out.println();
        }
        // System.out.println("Ratio: " + n / (placedTiles.size() - 1));

        // Print highlight, height and width
        if (SideFeature != null) {
            System.out.println("Highlighting " + highlightColour + SideFeature.getSymbol() + "s" + ANSI_RESET + ".");
        }

        // System.out.println("Height: " + height);
        // System.out.println("Width: " + width);
        // System.out.println("Closed features: " + closedFeatures.size());
        // System.out.println("Open features: " + openFeatures.size());
    }

    // Prints all the closed features on the board
    public void printClosedFeatures() {
        System.out.println("\n");
        for (SimpleGraph<Feature, DefaultEdge> graph : closedFeatures) {
            System.out.println(
                    graph.vertexSet().stream().map(f -> f.getClass().getSimpleName())
                            .collect(Collectors.toCollection(ArrayList::new))
                            + ""
                            + getTileFromFeature(graph.vertexSet().iterator().next()).getCoordinates()
                            + "");
        }
    }

    // Prints all the open features on the board
    public void printOpenFeatures() {
        System.out.println("\n");
        for (SimpleGraph<Feature, DefaultEdge> graph : openFeatures) {
            System.out.println(
                    graph.vertexSet().stream().map(f -> f.getClass().getSimpleName())
                            .collect(Collectors.toCollection(ArrayList::new)));
        }
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

    // Create a Board copy constructor
    @Override
    @SuppressWarnings("unchecked")
    public Object clone() {
        Board newBoard = new Board();
        newBoard.height = this.height;
        newBoard.width = this.width;
        newBoard.maxY = this.maxY;
        newBoard.maxX = this.maxX;
        newBoard.minY = this.minY;
        newBoard.minX = this.minX;
        newBoard.startingTile = (Tile) this.startingTile.clone();
        newBoard.placedTiles = (ArrayList<Tile>) this.placedTiles.stream().map(t -> (Tile) t.clone())
                .collect(Collectors.toCollection(ArrayList::new));
        newBoard.monasteryTiles = (HashSet<Tile>) this.monasteryTiles.stream().map(t -> (Tile) t.clone())
                .collect(Collectors.toCollection(HashSet::new));
        newBoard.openFeatures = (HashSet<SimpleGraph<Feature, DefaultEdge>>) this.openFeatures.stream()
                .map(g -> (SimpleGraph<Feature, DefaultEdge>) g.clone()).collect(Collectors.toCollection(HashSet::new));
        newBoard.closedFeatures = (HashSet<SimpleGraph<Feature, DefaultEdge>>) this.closedFeatures.stream()
                .map(g -> (SimpleGraph<Feature, DefaultEdge>) g.clone()).collect(Collectors.toCollection(HashSet::new));
        newBoard.newlyClosedFeatures = (HashSet<SimpleGraph<Feature, DefaultEdge>>) this.newlyClosedFeatures.stream()
                .map(g -> (SimpleGraph<Feature, DefaultEdge>) g.clone()).collect(Collectors.toCollection(HashSet::new));
        newBoard.possibleCoordinates = (ArrayList<Coordinates>) this.possibleCoordinates.stream()
                .map(c -> (Coordinates) c.clone())
                .collect(Collectors.toCollection(ArrayList::new));
        return newBoard;
    }
}
