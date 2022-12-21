package luca.carcassonne;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    private HashSet<Tile> monasteryTiles;
    private HashSet<SimpleGraph<Feature, DefaultEdge>> allFeatures;
    private HashSet<SimpleGraph<Feature, DefaultEdge>> openFeatures;
    private HashSet<SimpleGraph<Feature, DefaultEdge>> closedFeatures;
    private HashSet<SimpleGraph<Feature, DefaultEdge>> newlyClosedFeatures;
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
        monasteryTiles = new HashSet<>();
        possibleCoordinates = new ArrayList<>() {
            {
                startingTile.getAdjacentCoordinates().forEach(coordinate -> {
                    add(coordinate);
                });
            }
        };
        this.allFeatures = new HashSet<>();
        this.openFeatures = new HashSet<>();
        for (Feature feature : startingTile.getFeatures()) {
            SimpleGraph<Feature, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
            graph.addVertex(feature);
            allFeatures.add(graph);
            openFeatures.add(graph);
        }
        this.closedFeatures = new HashSet<>();
        this.newlyClosedFeatures = new HashSet<>();
    }

    public Tile getStartingTile() {
        return startingTile;
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
    public boolean placeMeeple(Feature newFeature, Player currentPlayer) {
        SimpleGraph<Feature, DefaultEdge> feature = new SimpleGraph<>(DefaultEdge.class);
        HashMap<Player, Integer> players = new HashMap<>();

        if (currentPlayer.getAvailableMeeples() <= 0) {
            return false;
        }

        for (SimpleGraph<Feature, DefaultEdge> f : allFeatures) {
            if (f.containsVertex(newFeature)) {
                feature = f;
                break;
            }
        }

        players = getPlayersOnFeature(feature);

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
        // System.out.println("\nPlacing " + newTile + " at " +
        // newTile.getCoordinates());

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
                .collect(Collectors.toList());

        for (Tile tile : tilesToCheck) {
            int position = getRelativePosition(tile, newTile.getCoordinates());

            linkFeatures(tile, newTile, position);
        }

        if (newTile.getFeatures().stream().anyMatch(c -> c instanceof Monastery)) {
            // System.out.println("Tile has monastery");
            monasteryTiles.add(newTile);
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

    // Links the new open features to the existing graphs.
    private void linkFeatures(Tile tile, Tile newTile, int position) {
        boolean featureLinked = false;
        newlyClosedFeatures.clear();

        for (Feature newFeature : newTile.getFeatures()) {
            featureLinked = false;
            switch (position) {
                case 0:
                    for (Feature feature : tile.getFeatures()) {
                        if (feature.getClass() == newFeature.getClass()
                                && feature.getCardinalPoints().contains(CardinalPoint.SSW)
                                && newFeature.getCardinalPoints().contains(CardinalPoint.NNW)) {
                            addFeaturesEdge(feature, newFeature);
                            featureLinked = true;
                        } else if (feature.getClass() == newFeature.getClass()
                                && feature.getCardinalPoints().contains(CardinalPoint.S)
                                && newFeature.getCardinalPoints().contains(CardinalPoint.N)) {
                            addFeaturesEdge(feature, newFeature);
                            featureLinked = true;
                        } else if (feature.getClass() == newFeature.getClass()
                                && feature.getCardinalPoints().contains(CardinalPoint.SSE)
                                && newFeature.getCardinalPoints().contains(CardinalPoint.NNE)) {
                            addFeaturesEdge(feature, newFeature);
                            featureLinked = true;
                        }
                    }
                    break;
                case 1:
                    for (Feature feature : tile.getFeatures()) {
                        if (feature.getClass() == newFeature.getClass()
                                && feature.getCardinalPoints().contains(CardinalPoint.WSW)
                                && newFeature.getCardinalPoints().contains(CardinalPoint.ESE)) {
                            addFeaturesEdge(feature, newFeature);
                            featureLinked = true;
                        } else if (feature.getClass() == newFeature.getClass()
                                && feature.getCardinalPoints().contains(CardinalPoint.W)
                                && newFeature.getCardinalPoints().contains(CardinalPoint.E)) {
                            addFeaturesEdge(feature, newFeature);
                            featureLinked = true;
                        } else if (feature.getClass() == newFeature.getClass()
                                && feature.getCardinalPoints().contains(CardinalPoint.WNW)
                                && newFeature.getCardinalPoints().contains(CardinalPoint.ENE)) {
                            addFeaturesEdge(feature, newFeature);
                            featureLinked = true;
                        }
                    }
                    break;
                case 2:
                    for (Feature feature : tile.getFeatures()) {
                        if (feature.getClass() == newFeature.getClass()
                                && feature.getCardinalPoints().contains(CardinalPoint.NNE)
                                && newFeature.getCardinalPoints().contains(CardinalPoint.SSE)) {
                            addFeaturesEdge(feature, newFeature);
                            featureLinked = true;
                        } else if (feature.getClass() == newFeature.getClass()
                                && feature.getCardinalPoints().contains(CardinalPoint.N)
                                && newFeature.getCardinalPoints().contains(CardinalPoint.S)) {
                            addFeaturesEdge(feature, newFeature);
                            featureLinked = true;
                        } else if (feature.getClass() == newFeature.getClass()
                                && feature.getCardinalPoints().contains(CardinalPoint.NNW)
                                && newFeature.getCardinalPoints().contains(CardinalPoint.SSW)) {
                            addFeaturesEdge(feature, newFeature);
                            featureLinked = true;
                        }
                    }
                    break;
                case 3:
                    for (Feature feature : tile.getFeatures()) {
                        if (feature.getClass() == newFeature.getClass()
                                && feature.getCardinalPoints().contains(CardinalPoint.ESE)
                                && newFeature.getCardinalPoints().contains(CardinalPoint.WSW)) {
                            addFeaturesEdge(feature, newFeature);
                            featureLinked = true;
                        } else if (feature.getClass() == newFeature.getClass()
                                && feature.getCardinalPoints().contains(CardinalPoint.E)
                                && newFeature.getCardinalPoints().contains(CardinalPoint.W)) {
                            addFeaturesEdge(feature, newFeature);
                            featureLinked = true;
                        } else if (feature.getClass() == newFeature.getClass()
                                && feature.getCardinalPoints().contains(CardinalPoint.ENE)
                                && newFeature.getCardinalPoints().contains(CardinalPoint.WNW)) {
                            addFeaturesEdge(feature, newFeature);
                            featureLinked = true;
                        }
                    }
                    break;
            }

            if (!featureLinked) {
                SimpleGraph<Feature, DefaultEdge> newGraph = new SimpleGraph<>(DefaultEdge.class);
                // System.out.println("Creating new: " + newFeature.getClass().getSimpleName());
                newGraph.addVertex(newFeature);
                allFeatures.add(newGraph);
                openFeatures.add(newGraph);

                checkIfMonasteriesAreComplete();
            } else if (newFeature.getClass() != Field.class) {
                checkIfFeatureIsComplete(newFeature);
            }
        }
    }

    // Finds the graph that contains the new feature and checks if it's closed
    private void checkIfFeatureIsComplete(Feature newFeature) {
        for (SimpleGraph<Feature, DefaultEdge> graph : openFeatures) {
            if (graph.containsVertex(newFeature)) {
                int totalCardinalPoints = graph.vertexSet().stream().mapToInt(f -> f.getCardinalPoints().size()).sum();
                int totalEdges = graph.edgeSet().size();

                if (totalEdges != 0 && (newFeature.getClass() == Castle.class && totalCardinalPoints / totalEdges == 6
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
        for (Tile monastery : monasteryTiles) {
            if (getSurroundingTiles(monastery) == 8) {
                SimpleGraph<Feature, DefaultEdge> newGraph;

                for (SimpleGraph<Feature, DefaultEdge> g : openFeatures) {
                    if (g.containsVertex(monastery.getFeatures().iterator().next())) {
                        newGraph = g;

                        newlyClosedFeatures.add(newGraph);
                        closedFeatures.add(newGraph);
                        openFeatures.remove(newGraph);

                        break;
                    }
                }
            }
        }
    }

    // Connects a feature to an existing graph.
    private void addFeaturesEdge(Feature feature, Feature newFeature) {
        for (SimpleGraph<Feature, DefaultEdge> graph : openFeatures) {
            if (graph.containsVertex(feature) && !graph.containsVertex(newFeature)) {
                // System.out.println("Adding edge between " +
                // feature.getClass().getSimpleName() + " and "
                // + newFeature.getClass().getSimpleName());
                graph.addVertex(newFeature);
                graph.addEdge(feature, newFeature);
            }
        }
    }

    // Scores all closed features
    public void scoreClosedFeatures() {
        if (newlyClosedFeatures.isEmpty()) {
            return;
        }

        for (SimpleGraph<Feature, DefaultEdge> feature : newlyClosedFeatures) {
            if (feature.vertexSet().iterator().next().getClass() == Field.class) {
                continue;
            }

            List<Player> owners = new ArrayList<>();
            int score = 0;

            owners = getFeatureOwners(feature);
            score = calculateClosedFeatureValue(feature);

            if (owners.isEmpty()) {
                break;
            }

            for (Player owner : owners) {
                owner.addScore(score);
                // System.out.println("Scored a " + feature.vertexSet().size() + " tile "
                // + feature.vertexSet().iterator().next().getClass().getSimpleName() + " for "
                // + owner.getColour() + " worth " + score + " points");
            }

        }
    }

    // Scores all the open features
    public void scoreOpenFeatures() {
        if (openFeatures.isEmpty()) {
            System.out.println("No open features to score");
            return;
        }

        for (SimpleGraph<Feature, DefaultEdge> feature : openFeatures) {
            Set<Player> owners = new HashSet<>();
            int score = 0;

            owners = getPlayersOnFeature(feature).keySet();
            score = calculateOpenFeatureValue(feature);

            if (owners.isEmpty() || score == 0) {
                continue;
            }

            // System.out.println("Scoring " +
            // feature.vertexSet().iterator().next().getClass().getSimpleName() + " for "
            // + owners.iterator().next().getColour() + " worth " + score + " points");

            for (Player owner : owners) {
                owner.addScore(score);
                // System.out.println("Scored a " + feature.vertexSet().size() + " tile "
                // + feature.vertexSet().iterator().next().getClass().getSimpleName() + " for "
                // + owner.getColour() + " worth " + score + " points");
            }

        }
    }

    // Returns a list of players who own the feature
    private List<Player> getFeatureOwners(SimpleGraph<Feature, DefaultEdge> feature) {
        ArrayList<Player> owners = new ArrayList<>();
        HashMap<Player, Integer> players = new HashMap<>();
        int maxMeeples = 0;

        players = getPlayersOnFeature(feature);

        // Find the player(s) with the most meeples on the feature
        if (!players.isEmpty()) {
            int nMeeples = 0;
            maxMeeples = players.values().stream().max(Integer::compare).get();

            for (Player p : players.keySet()) {
                nMeeples = players.get(p);
                p.incrementMeeples(nMeeples);

                if (nMeeples == maxMeeples) {
                    owners.add(p);
                }
            }

        }

        return owners;
    }

    // Returns a map of players and the number of meeples they have on the feature
    private HashMap<Player, Integer> getPlayersOnFeature(SimpleGraph<Feature, DefaultEdge> feature) {
        HashMap<Player, Integer> players = new HashMap<>();

        // Map each player to the number of meeples they have on the feature
        feature.vertexSet().stream().map(v -> v.getOwner()).forEach(p -> {
            if (players.containsKey(p)) {
                players.put(p, players.get(p) + 1);
            } else {
                if (p != null) {
                    players.put(p, 1);
                }
            }
        });

        return players;
    }

    // Calculates the value of a closed feature
    private int calculateClosedFeatureValue(SimpleGraph<Feature, DefaultEdge> feature) {
        int score = 0;
        for (Feature vertex : feature.vertexSet()) {
            if (vertex.getClass() == Castle.class && ((Castle) vertex).hasShield()) {
                score += 2 + vertex.getPointsClosed();
                continue;
            } else if (vertex.getClass() == Monastery.class) {
                score += 9;
                continue;
            }

            score += vertex.getPointsClosed();
        }

        return score;
    }

    // Calculates the value of an open feature
    private int calculateOpenFeatureValue(SimpleGraph<Feature, DefaultEdge> feature) {
        int score = 0;
        Feature v = feature.vertexSet().iterator().next();

        if (v.getClass() != Field.class) {
            for (Feature vertex : feature.vertexSet()) {
                if (vertex.getClass() == Castle.class && ((Castle) vertex).hasShield()) {
                    score += 1 + vertex.getPointsClosed();
                    continue;
                } else if (vertex.getClass() == Monastery.class) {
                    Tile monasteryTile = getTileFromFeature(vertex);
                    score += 1 + getSurroundingTiles(monasteryTile);
                    continue;
                }
                score += vertex.getPointsOpen();
            }
        } else {
            score += calculateFieldScore(feature);
        }

        return score;
    }

    // Scores all the fields on the board
    private int calculateFieldScore(SimpleGraph<Feature, DefaultEdge> feature) {
        HashSet<SimpleGraph<Feature, DefaultEdge>> adjacentCastles = new HashSet<>();
        int score = 0;

        for (Feature vertex : feature.vertexSet()) {
            Field field = (Field) vertex;

            if (!field.hasAdjacentCastle()) {
                continue;
            }

            for (SimpleGraph<Feature, DefaultEdge> castle : closedFeatures) {
                for (Feature castleVertex : field.getAdjacentCastles()) {
                    if (castle.containsVertex(castleVertex)) {
                        adjacentCastles.add(castle);
                    }
                }
            }
        }
        score += Field.POINTS_PER_CASTLE * adjacentCastles.size();

        return score;
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

    // Returns the number of tiles surrounding the given tile (used for scoring
    // monasteries)
    private int getSurroundingTiles(Tile tile) {
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

    private Tile getTileFromFeature(Feature feature) {
        for (Tile tile : placedTiles) {
            for (Feature f : tile.getFeatures()) {
                if (f.equals(feature)) {
                    return tile;
                }
            }
        }

        return null;
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

    public HashSet<SimpleGraph<Feature, DefaultEdge>> getNewlyClosedFeatures() {
        return newlyClosedFeatures;
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
        System.out.println("Closed features: " + closedFeatures.size());
        System.out.println("Open features: " + openFeatures.size());
    }

    // Prints all the closed features on the board
    public void printClosedFeatures() {
        System.out.println("\n");
        for (SimpleGraph<Feature, DefaultEdge> graph : closedFeatures) {
            System.out.println(
                    graph.vertexSet().stream().map(f -> f.getClass().getSimpleName()).collect(Collectors.toList()));
        }
    }

    // Prints all the open features on the board
    public void printOpenFeatures() {
        System.out.println("\n");
        for (SimpleGraph<Feature, DefaultEdge> graph : openFeatures) {
            System.out.println(
                    graph.vertexSet().stream().map(f -> f.getClass().getSimpleName()).collect(Collectors.toList()));
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
}
