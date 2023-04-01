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

import luca.carcassonne.mcts.Move;
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

/**
 * A class that handles the game's board.
 * 
 * The class is responsible for keeping track of tile placements, feature
 * updates and past moves.
 * 
 * @author Luca Brown
 */
public class Board {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RED = "\u001B[31m";

    private int height;
    private int width;
    private int maxY;
    private int maxX;
    private int minY;
    private int minX;
    private Tile startingTile;
    private ArrayList<Tile> placedTiles;
    private HashSet<Tile> monasteryTiles;
    private HashSet<SimpleGraph<Feature, DefaultEdge>> openFeatures;
    private HashSet<SimpleGraph<Feature, DefaultEdge>> closedFeatures;
    private HashSet<SimpleGraph<Feature, DefaultEdge>> newlyClosedFeatures;
    private ArrayList<Coordinates> possibleCoordinates;
    private ArrayList<Move> pastMoves;

    /**
     * Creates a new board with the given starting tile.
     * 
     * All collections are initialised and the starting tile is placed at (0, 0).
     * 
     * @param startingTile The starting tile of the board.
     */
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
        this.pastMoves = new ArrayList<>();
    }

    public Board() {
        this(Settings.getStartingTile());
    }

    /**
     * Tries to place a tile in the given coordinates. Returns true if the placement
     * was legal.
     * 
     * @param coordinates The coordinates to place the tile at.
     * @param newTile     The tile to be placed.
     * @return True if the tile was placed successfully.
     */
    public boolean placeTile(Coordinates coordinates, Tile newTile) {
        List<Tile> tilesToCheck = placedTiles.stream()
                .filter(e -> e.getAdjacentCoordinates().contains(coordinates))
                .collect(Collectors.toCollection(ArrayList::new));

        if (tilePlacementLegal(coordinates, newTile, tilesToCheck)) {
            newTile.setCoordinates(coordinates);

            updateBoard(newTile);
            updateFeatures(newTile, tilesToCheck);

            return true;
        }

        return false;

    }

    /**
     * Tries to place a meeple on the given feature. Returns true if the placement
     * was successful.
     * 
     * @param newFeature    The feature to place the meeple on.
     * @param currentPlayer The player placing the meeple.
     * @return True if the meeple was placed successfully.
     * 
     */
    @SuppressWarnings("unchecked")
    public boolean placeMeeple(Feature newFeature, Player currentPlayer) {
        SimpleGraph<Feature, DefaultEdge> feature = new SimpleGraph<>(DefaultEdge.class);
        HashMap<Player, Integer> players = new HashMap<>();
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

    /**
     * Updates the board's state with the new tile
     * 
     * @param newTile The new tile to be placed.
     */
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

    /**
     * Updates the set of open features by connecting the features of the new tile.
     * 
     * @param newTile      The new tile to be placed.
     * @param tilesToCheck The adjacent tiles to the new tile.
     */
    private void updateFeatures(Tile newTile, List<Tile> tilesToCheck) {
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

    /**
     * Returns true if the tile placement is legal.
     * 
     * @param coordinates  The coordinates the tile si being placed at.
     * @param tile         The tile that's being placed.
     * @param tilesToCheck The adjacent tiles to the new tile.
     * @return True if the tile placement is legal.
     */
    private boolean tilePlacementLegal(Coordinates coordinates, Tile tile, List<Tile> tilesToCheck) {
        if (possibleCoordinates.contains(coordinates)) {
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

    /**
     * Returns true if the tile can be placed. Does not place the tile.
     * 
     * @param coordinates The coordinates the tile is being placed at.
     * @param tile        The tile that's being placed.
     * @return True if the tile can be placed.
     */
    public boolean canPlaceJunkTile(Coordinates coordinates, Tile tile) {
        List<Tile> tilesToCheck = placedTiles.stream()
                .filter(e -> e.getAdjacentCoordinates().contains(coordinates))
                .collect(Collectors.toCollection(ArrayList::new));

        return tilePlacementLegal(coordinates, tile, tilesToCheck);
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

    /**
     * Finds the graph that contains the new feature and checks if it's closed
     * 
     * @param newFeature The new feature that was added to the graph.
     */
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

    /**
     * Checks if any monasteries were completed.
     */
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

    /**
     * Checks if two features match given their cardinal points.
     * 
     * @param feature                 The feature that is already in the graph.
     * @param newFeature              The new feature that is being added to the
     *                                graph.
     * @param featureCardinalPoint    The cardinal point of the feature that is
     *                                already in the graph.
     * @param newFeatureCardinalPoint The cardinal point of the new feature that is
     *                                being added to the graph.
     * @return True if the features match, false otherwise.
     */
    private boolean featuresMatch(Feature feature, Feature newFeature, CardinalPoint featureCardinalPoint,
            CardinalPoint newFeatureCardinalPoint) {
        return feature.getClass() == newFeature.getClass()
                && feature.getCardinalPoints().contains(featureCardinalPoint)
                && newFeature.getCardinalPoints().contains(newFeatureCardinalPoint);
    }

    /**
     * Connects a feature to an existing graph.
     * 
     * @param feature    The feature that is already in the graph.
     * @param newFeature The new feature that is being added to the graph.
     */
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

    /**
     * Returns a number between 0 and 3 that represents the clockwise position of a
     * tile relative to the given coordinates
     * (0 = north, 1 = east, 2 = south, 3 = west).
     * 
     * @param tile        The tile to check.
     * @param coordinates The coordinates to check.
     * @return The relative position of the tile.
     */
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

    /**
     * Returns the tile that contains the given feature.
     * 
     * @param feature The feature to check.
     * @return The tile that contains the feature.
     */
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

    /**
     * Returns the number of tiles surrounding the given tile (used for scoring
     * monasteries).
     * 
     * @param tile The tile to check.
     * @return The number of surrounding tiles.
     */
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

    public ArrayList<Move> getPastMoves() {
        return pastMoves;
    }

    public Move getLastMove() {
        return pastMoves.get(pastMoves.size() - 1);
    }

    public void setPastMoves(ArrayList<Move> pastMoves) {
        this.pastMoves = pastMoves;
    }

    public void addNewMove(Move move) {
        pastMoves.add(move);
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

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getMaxY() {
        return maxY;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    public int getMinY() {
        return minY;
    }

    public void setMinY(int minY) {
        this.minY = minY;
    }

    public int getMaxX() {
        return maxX;
    }

    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    public int getMinX() {
        return minX;
    }

    public void setMinX(int minX) {
        this.minX = minX;
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

        System.out.println();
        for (int i = maxY; i >= minY; i--) {
            for (int j = minX; j <= maxX; j++) {
                Coordinates c = new Coordinates(j, i);
                if (j == 0 && i == 0) {
                    System.out.print(defaultColour + "O " + ANSI_RESET);
                } else if (coordinates.contains(c)) {
                    if (SideFeature != null && getTileFromCoordinates(c).getSideFeatures().contains(SideFeature)) {
                        System.out.print(highlightColour + "X " + ANSI_RESET);
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

    public Tile getStartingTile() {
        return startingTile;
    }

}
