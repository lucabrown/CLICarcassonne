package luca.carcassonne;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.jgrapht.Graphs;
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
    @SuppressWarnings("unchecked")
    public boolean placeMeeple(Feature newFeature, Player currentPlayer) {
        SimpleGraph<Feature, DefaultEdge> feature = new SimpleGraph<>(DefaultEdge.class);
        HashMap<Player, Integer> players = new HashMap<>();
        HashSet<SimpleGraph<Feature, DefaultEdge>> allFeatures = (HashSet<SimpleGraph<Feature, DefaultEdge>>) openFeatures
                .clone();
        allFeatures.addAll(closedFeatures);

        if (currentPlayer.getAvailableMeeples() <= 0) {
            System.out.println("You have no more meeples!");
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
            System.out.println("You cannot place a meeple on a feature with other players' meeples!");
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
                // System.out.println("Creating new: " + newFeature.getClass().getSimpleName());

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
                // System.out.println("Adding edge between " +
                // feature.getClass().getSimpleName() + " and "
                // + newFeature.getClass().getSimpleName());
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
            score = calculateFeatureValue(feature, true);

            if (owners.isEmpty()) {
                continue;
            }

            for (Player owner : owners) {
                owner.addScore(score);
            }

        }
    }

    // Scores all the open features
    public void scoreOpenFeatures() {
        if (openFeatures.isEmpty()) {
            return;
        }

        for (SimpleGraph<Feature, DefaultEdge> feature : openFeatures) {
            Set<Player> owners = new HashSet<>();
            int score = 0;

            owners = getPlayersOnFeature(feature).keySet();
            score = calculateFeatureValue(feature, false);

            if (owners.isEmpty() || score == 0) {
                continue;
            }

            for (Player owner : owners) {
                owner.addScore(score);
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

    // Calculates the value of a feature
    private int calculateFeatureValue(SimpleGraph<Feature, DefaultEdge> feature, boolean isClosed) {
        HashSet<Tile> belongingTiles = new HashSet<>();
        Class<?> featureClass = feature.vertexSet().iterator().next().getClass();
        int score = 0;

        for (Feature vertex : feature.vertexSet()) {
            belongingTiles.add(vertex.getBelongingTile());
        }

        if (featureClass == Road.class) {
            score += belongingTiles.size();
        }
        else if(featureClass == Castle.class){
            int shields = 0;

            for(Feature vertex : feature.vertexSet()){
                if(((Castle)vertex).hasShield()){
                    shields++;
                }
            }

            if(isClosed){
                score += belongingTiles.size() * Rules.CASTLE_POINTS_CLOSED + shields * Rules.SHIELD_POINTS_CLOSED;
            }
            else{
                score += belongingTiles.size() * Rules.CASTLE_POINTS_OPEN + shields * Rules.SHIELD_POINTS_OPEN;
            }
        }
        else if(featureClass == Field.class){
            score += calculateFieldScore(feature);
        }
        else if(featureClass == Monastery.class){
            if(isClosed){
                score += 9;
            }
            else{
                // Tile monasteryTile = getTileFromFeature(vertex);
                score += 1 + getSurroundingTiles(feature.vertexSet().iterator().next().getBelongingTile());
            }
        }
        else{
            throw new IllegalArgumentException("Feature class not recognized");
        }

        return score;
    }

    // // Calculates the value of a closed feature
    // private int calculateClosedFeatureValue(SimpleGraph<Feature, DefaultEdge> feature) {
    //     // HashSet<Tile>
    //     int score = 0;
    //     for (Feature vertex : feature.vertexSet()) {
    //         if (vertex.getClass() == Castle.class && ((Castle) vertex).hasShield()) {
    //             score += Rules.SHIELD_POINTS_CLOSED + vertex.getPointsClosed();
    //             continue;
    //         } else if (vertex.getClass() == Monastery.class) {
    //             score += 9;
    //             continue;
    //         }

    //         score += vertex.getPointsClosed();
    //     }

    //     return score;
    // }

    // // Calculates the value of an open feature
    // private int calculateOpenFeatureValue(SimpleGraph<Feature, DefaultEdge> feature) {
    //     int score = 0;
    //     Feature v = feature.vertexSet().iterator().next();

    //     if (v.getClass() != Field.class) {
    //         for (Feature vertex : feature.vertexSet()) {
    //             if (vertex.getClass() == Castle.class && ((Castle) vertex).hasShield()) {
    //                 score += Rules.SHIELD_POINTS_OPEN + vertex.getPointsClosed();
    //                 continue;
    //             } else if (vertex.getClass() == Monastery.class) {
    //                 Tile monasteryTile = getTileFromFeature(vertex);
    //                 score += 1 + getSurroundingTiles(monasteryTile);
    //                 continue;
    //             }
    //             score += vertex.getPointsOpen();
    //         }
    //     } else {
    //         score += calculateFieldScore(feature);
    //     }

    //     return score;
    // }

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
        score += Rules.FIELD_POINTS_PER_CASTLE * adjacentCastles.size();

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
        System.out.println("Surrounding tiles: " + nSurroundingTiles);
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
                    graph.vertexSet().stream().map(f -> f.getClass().getSimpleName()).collect(Collectors.toList())
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
