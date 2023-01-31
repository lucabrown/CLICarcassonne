package luca.carcassonne;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Collectors;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

class BoardTest {
    Tile startingTile = Rules.getSingleCastleWithStraightRoad();
    Board board;

    @Test
    void testBoardStartsWithStartingTileAtOrigin() {
        board = new Board(startingTile);

        assertTrue(board.getPlacedTiles().contains(startingTile));
        assertEquals(0, startingTile.getCoordinates().getX());
        assertEquals(0, startingTile.getCoordinates().getY());
        assertEquals(1, board.getPlacedTiles().size());
    }

    @Test
    void testAddingTileIncreasesBoardSize() {
        Tile tile = new Tile(SideFeature.CASTLE, SideFeature.CASTLE, SideFeature.CASTLE, SideFeature.CASTLE);
        board = new Board(startingTile);

        board.placeTile(new Coordinates(0, 1), tile);

        assertEquals(2, board.getPlacedTiles().size());
    }

    @Test
    void testTileIsPlacedAtCorrectCoordinates() {
        Tile tile = new Tile(SideFeature.CASTLE, SideFeature.CASTLE, SideFeature.CASTLE, SideFeature.CASTLE);
        board = new Board(startingTile);

        board.placeTile(new Coordinates(0, 1), tile);

        assertEquals(0, tile.getCoordinates().getX());
        assertEquals(1, tile.getCoordinates().getY());
    }

    // PLACEMENT TESTS

    @Test
    void testCastlePlacement() {
        Tile tile = new Tile(SideFeature.CASTLE, SideFeature.CASTLE, SideFeature.CASTLE, SideFeature.CASTLE);
        board = new Board(startingTile);

        assertTrue(board.placeTile(new Coordinates(0, 1), tile));
        assertFalse(board.placeTile(new Coordinates(1, 0), tile));
        assertFalse(board.placeTile(new Coordinates(0, -1), tile));
        assertFalse(board.placeTile(new Coordinates(-1, 0), tile));
    }

    @Test
    void testFieldPlacement() {
        Tile tile = new Tile(SideFeature.FIELD, SideFeature.FIELD, SideFeature.FIELD, SideFeature.FIELD);
        board = new Board(startingTile);

        assertFalse(board.placeTile(new Coordinates(0, 1), tile));
        assertFalse(board.placeTile(new Coordinates(1, 0), tile));
        assertTrue(board.placeTile(new Coordinates(0, -1), tile));
        assertFalse(board.placeTile(new Coordinates(-1, 0), tile));
    }

    @Test
    void testRoadPlacement() {
        Tile tile = new Tile(SideFeature.ROAD, SideFeature.ROAD, SideFeature.ROAD, SideFeature.ROAD);
        board = new Board(new Tile(SideFeature.CASTLE, SideFeature.ROAD, SideFeature.FIELD, SideFeature.ROAD));

        assertFalse(board.placeTile(new Coordinates(0, 1), tile));
        assertTrue(board.placeTile(new Coordinates(1, 0), tile));
        assertFalse(board.placeTile(new Coordinates(0, -1), tile));
        assertTrue(board.placeTile(new Coordinates(-1, 0), tile));
    }

    // CLOSURE TESTS

    @Test
    void testClosingTwoTileCastle() {
        Board board = new Board(startingTile);

        Tile tile = Rules.getSingleCastle();

        int nOpenFeatures = board.getOpenFeatures().size();
        int nClosedFeatures = board.getClosedFeatures().size();
        int nNewlyClosedFeatures = board.getNewlyClosedFeatures().size();

        tile.rotateClockwise(2);

        assertTrue(board.placeTile(new Coordinates(0, 1), tile));

        assertEquals(nOpenFeatures - 1 + 1, board.getOpenFeatures().size());
        assertEquals(nClosedFeatures + 1, board.getClosedFeatures().size());
        assertEquals(nNewlyClosedFeatures + 1, board.getNewlyClosedFeatures().size());
        assertEquals(2, board.getClosedFeatures().iterator().next().vertexSet().size());
    }

    @Test
    void testClosingThreeTileCastle() {
        board = new Board(startingTile);

        Tile singleCastle = Rules.getSingleCastle();
        Tile longCastle = Rules.getLongCastle();

        int nOpenFeatures = board.getOpenFeatures().size();
        int nClosedFeatures = board.getClosedFeatures().size();
        int nNewlyClosedFeatures = board.getNewlyClosedFeatures().size();

        singleCastle.rotateClockwise(2);
        longCastle.rotateClockwise();

        assertTrue(board.placeTile(new Coordinates(0, 1), longCastle));

        assertEquals(nOpenFeatures + 2, board.getOpenFeatures().size());
        assertEquals(nClosedFeatures, board.getClosedFeatures().size());
        assertEquals(nNewlyClosedFeatures, board.getNewlyClosedFeatures().size());

        nOpenFeatures = board.getOpenFeatures().size();
        nClosedFeatures = board.getClosedFeatures().size();
        nNewlyClosedFeatures = board.getNewlyClosedFeatures().size();

        assertTrue(board.placeTile(new Coordinates(0, 2), singleCastle));

        assertEquals(nOpenFeatures - 1 + 1, board.getOpenFeatures().size());
        assertEquals(nClosedFeatures + 1, board.getClosedFeatures().size());
        assertEquals(nNewlyClosedFeatures + 1, board.getNewlyClosedFeatures().size());
        assertEquals(3, board.getClosedFeatures().iterator().next().vertexSet().size());

    }

    @Test
    void testClosingFourTileLoopCastle() {
        board = new Board(Rules.getCurvyCastle());

        Tile curvyCastle = Rules.getCurvyCastle();
        curvyCastle.rotateClockwise();
        assertTrue(board.placeTile(new Coordinates(0, 1), curvyCastle));
        board.printOpenFeatures();
        assertEquals(3, board.getOpenFeatures().size());
        assertEquals(0, board.getClosedFeatures().size());
        assertEquals(0, board.getNewlyClosedFeatures().size());

        curvyCastle = Rules.getCurvyCastle();
        curvyCastle.rotateClockwise(2);
        assertTrue(board.placeTile(new Coordinates(1, 1), curvyCastle));
        board.printOpenFeatures();
        assertEquals(4, board.getOpenFeatures().size());
        assertEquals(0, board.getClosedFeatures().size());
        assertEquals(0, board.getNewlyClosedFeatures().size());

        curvyCastle = Rules.getCurvyCastle();
        curvyCastle.rotateClockwise(3);
        assertTrue(board.placeTile(new Coordinates(1, 0), curvyCastle));
        board.printOpenFeatures();

        board.printClosedFeatures();
        for (SimpleGraph<Feature, DefaultEdge> feature : board.getOpenFeatures()) {
            if (feature.vertexSet().iterator().next().getClass() == Road.class) {
                System.out.println(
                        feature.vertexSet().stream().map(f -> f.getClass().getSimpleName())
                                .collect(Collectors.toList()));
                System.out.println(feature.vertexSet().stream().map(f -> f.getCardinalPoints().size()));
                System.out.println(feature.edgeSet().size());

            }
        }

        assertEquals(4, board.getOpenFeatures().size());
        assertEquals(1, board.getClosedFeatures().size());
        assertEquals(1, board.getNewlyClosedFeatures().size());

    }

    @Test
    void testClosingTwoTileRoad() {
        board = new Board(Rules.getMonasteryWithRoad());

        Tile tile = Rules.getMonasteryWithRoad();

        int nOpenFeatures = board.getOpenFeatures().size();
        int nClosedFeatures = board.getClosedFeatures().size();
        int nNewlyClosedFeatures = board.getNewlyClosedFeatures().size();

        tile.rotateClockwise(2);

        assertTrue(board.placeTile(new Coordinates(0, -1), tile));

        assertEquals(nOpenFeatures - 1 + 1, board.getOpenFeatures().size());
        assertEquals(nClosedFeatures + 1, board.getClosedFeatures().size());
        assertEquals(nNewlyClosedFeatures + 1, board.getNewlyClosedFeatures().size());
        assertEquals(2, board.getClosedFeatures().iterator().next().vertexSet().size());
    }

    @Test
    void testClosingThreeTileRoad() {
        board = new Board(Rules.getMonasteryWithRoad());

        Tile straightRoad = Rules.getStraightRoad();
        Tile closingRoad = Rules.getMonasteryWithRoad();

        int nOpenFeatures = board.getOpenFeatures().size();
        int nClosedFeatures = board.getClosedFeatures().size();
        int nNewlyClosedFeatures = board.getNewlyClosedFeatures().size();

        closingRoad.rotateClockwise(2);

        assertTrue(board.placeTile(new Coordinates(0, -1), straightRoad));

        assertEquals(nOpenFeatures, board.getOpenFeatures().size());
        assertEquals(nClosedFeatures, board.getClosedFeatures().size());
        assertEquals(nNewlyClosedFeatures, board.getNewlyClosedFeatures().size());

        nOpenFeatures = board.getOpenFeatures().size();
        nClosedFeatures = board.getClosedFeatures().size();
        nNewlyClosedFeatures = board.getNewlyClosedFeatures().size();

        assertTrue(board.placeTile(new Coordinates(0, -2), closingRoad));

        assertEquals(nOpenFeatures - 1 + 1, board.getOpenFeatures().size());
        assertEquals(nClosedFeatures + 1, board.getClosedFeatures().size());
        assertEquals(nNewlyClosedFeatures + 1, board.getNewlyClosedFeatures().size());
        assertEquals(3, board.getClosedFeatures().iterator().next().vertexSet().size());
    }

    @Test
    void testClosingFourTileLoopRoad() {
        board = new Board(Rules.getCurvyRoad());

        Tile curvyRoad = Rules.getCurvyRoad();
        curvyRoad.rotateClockwise();
        assertTrue(board.placeTile(new Coordinates(0, 1), curvyRoad));
        board.printOpenFeatures();
        assertEquals(3, board.getOpenFeatures().size());
        assertEquals(0, board.getClosedFeatures().size());
        assertEquals(0, board.getNewlyClosedFeatures().size());

        curvyRoad = Rules.getCurvyRoad();
        curvyRoad.rotateClockwise(2);
        assertTrue(board.placeTile(new Coordinates(1, 1), curvyRoad));
        board.printOpenFeatures();
        assertEquals(3, board.getOpenFeatures().size());
        assertEquals(0, board.getClosedFeatures().size());
        assertEquals(0, board.getNewlyClosedFeatures().size());

        curvyRoad = Rules.getCurvyRoad();
        curvyRoad.rotateClockwise(3);
        assertTrue(board.placeTile(new Coordinates(1, 0), curvyRoad));
        board.printOpenFeatures();

        board.printClosedFeatures();
        for (SimpleGraph<Feature, DefaultEdge> feature : board.getOpenFeatures()) {
            if (feature.vertexSet().iterator().next().getClass() == Road.class) {
                System.out.println(
                        feature.vertexSet().stream().map(f -> f.getClass().getSimpleName())
                                .collect(Collectors.toList()));
                System.out.println(feature.vertexSet().stream().map(f -> f.getCardinalPoints().size()));
                System.out.println(feature.edgeSet().size());

            }
        }

        assertEquals(2, board.getOpenFeatures().size());
        assertEquals(1, board.getClosedFeatures().size());
        assertEquals(1, board.getNewlyClosedFeatures().size());

    }

    @Test
    void testClosingFourTileFourEdgeRoad() {
        board = new Board(Rules.getThreeRoadIntersection());

        Tile curvyRoad = Rules.getCurvyRoad();
        assertTrue(board.placeTile(new Coordinates(0, -1), curvyRoad));
        System.out.println("After placing first tile");
        board.printOpenFeatures();
        assertEquals(6, board.getOpenFeatures().size());
        assertEquals(0, board.getClosedFeatures().size());
        assertEquals(0, board.getNewlyClosedFeatures().size());

        curvyRoad = Rules.getCurvyRoad();
        curvyRoad.rotateClockwise(2);
        assertTrue(board.placeTile(new Coordinates(1, 0), curvyRoad));
        System.out.println("After placing second tile");
        System.out.println("Open features:");

        board.printOpenFeatures();
        assertEquals(6, board.getOpenFeatures().size());
        assertEquals(0, board.getClosedFeatures().size());
        assertEquals(0, board.getNewlyClosedFeatures().size());

        curvyRoad = Rules.getCurvyRoad();
        curvyRoad.rotateClockwise(3);
        assertTrue(board.placeTile(new Coordinates(1, -1), curvyRoad));
        System.out.println("After placing third tile");
        System.out.println("Open features:");

        board.printOpenFeatures();

        System.out.println("Closed features:");

        board.printClosedFeatures();
        Road road = curvyRoad.getFeatures().stream().filter(f -> f instanceof Road).map(f -> (Road) f).findFirst()
                .get();
        for (SimpleGraph<Feature, DefaultEdge> feature : board.getOpenFeatures()) {
            if (feature.vertexSet().contains(road)) {
                System.out.println("Yes");
            }
        }
        board.scoreClosedFeatures();
        assertEquals(3, board.getOpenFeatures().size());
        assertEquals(1, board.getClosedFeatures().size());
        assertEquals(1, board.getNewlyClosedFeatures().size());
    }

    @Test
    void testClosingMonastery() {
        board = new Board(Rules.getMonastery());
        Player player = new Player(Colour.RED);

        Tile straightRoad = Rules.getStraightRoad();
        straightRoad.setOwner(player);
        assertTrue(board.placeTile(new Coordinates(1, 0), straightRoad));

        straightRoad = Rules.getStraightRoad();
        straightRoad.setOwner(player);
        assertTrue(board.placeTile(new Coordinates(-1, 0), straightRoad));

        straightRoad = Rules.getStraightRoad();
        straightRoad.rotateClockwise();
        straightRoad.setOwner(player);
        assertTrue(board.placeTile(new Coordinates(0, 1), straightRoad));

        straightRoad = Rules.getStraightRoad();
        straightRoad.rotateClockwise();
        straightRoad.setOwner(player);
        assertTrue(board.placeTile(new Coordinates(0, -1), straightRoad));

        Tile curvyRoad = Rules.getCurvyRoad();
        curvyRoad.setOwner(player);
        assertTrue(board.placeTile(new Coordinates(-1, -1), curvyRoad));

        curvyRoad = Rules.getCurvyRoad();
        curvyRoad.rotateClockwise();
        curvyRoad.setOwner(player);
        assertTrue(board.placeTile(new Coordinates(-1, 1), curvyRoad));

        curvyRoad = Rules.getCurvyRoad();
        curvyRoad.rotateClockwise(2);
        curvyRoad.setOwner(player);
        assertTrue(board.placeTile(new Coordinates(1, 1), curvyRoad));

        curvyRoad = Rules.getCurvyRoad();
        curvyRoad.rotateClockwise(3);
        curvyRoad.setOwner(player);
        assertTrue(board.placeTile(new Coordinates(1, -1), curvyRoad));
        System.out.println("After placing all tiles");
        board.printBoard();

        board.printOpenFeatures();
        assertEquals(2, board.getOpenFeatures().size());
        assertEquals(2, board.getClosedFeatures().size());
        assertEquals(2, board.getNewlyClosedFeatures().size());
    }

    // MEEPLE TESTS

    @Test
    void testAddingMeepleToRoad() {
        board = new Board(Rules.getStraightRoad());
        Player player = new Player(Colour.RED);

        Tile singleRoad = Rules.getMonasteryWithRoad();
        Feature roadFeature = singleRoad.getFeatures().stream().filter(f -> f instanceof Road).findFirst().get();

        singleRoad.setOwner(player);

        assertTrue(board.placeTile(new Coordinates(0, 1), singleRoad));
        assertTrue(board.placeMeeple(roadFeature, player));

        assertEquals(player, roadFeature.getOwner());
    }

    @Test
    void testAddingMeepleToCastle() {
        board = new Board(Rules.getHugeCastle());
        Player player = new Player(Colour.RED);

        Tile singleCastle = Rules.getHugeCastle();
        Feature castleFeature = singleCastle.getFeatures().stream().filter(f -> f instanceof Castle).findFirst().get();

        singleCastle.setOwner(player);

        assertTrue(board.placeTile(new Coordinates(0, 1), singleCastle));
        assertTrue(board.placeMeeple(castleFeature, player));

        assertEquals(player, castleFeature.getOwner());
    }

    @Test
    void testAddingMeepleToMonastery() {
        board = new Board(Rules.getMonastery());
        Player player = new Player(Colour.RED);

        Tile singleMonastery = Rules.getMonastery();
        Feature monasteryFeature = singleMonastery.getFeatures().stream().filter(f -> f instanceof Monastery)
                .findFirst().get();

        singleMonastery.setOwner(player);

        assertTrue(board.placeTile(new Coordinates(0, 1), singleMonastery));
        assertTrue(board.placeMeeple(monasteryFeature, player));

        assertEquals(player, monasteryFeature.getOwner());
    }

    @Test
    void testAddingMeepleToField() {
        board = new Board(Rules.getMonastery());
        Player player = new Player(Colour.RED);

        Tile singleField = Rules.getMonastery();
        Feature fieldFeature = singleField.getFeatures().stream().filter(f -> f instanceof Field).findFirst().get();

        singleField.setOwner(player);

        assertTrue(board.placeTile(new Coordinates(0, 1), singleField));
        assertTrue(board.placeMeeple(fieldFeature, player));

        assertEquals(player, fieldFeature.getOwner());
    }

    @Test
    void testAddingMeepleToTakenFeature() {
        board = new Board(Rules.getHugeCastle());
        Player player1 = new Player(Colour.RED);
        Player player2 = new Player(Colour.BLUE);

        Tile castle = Rules.getHugeCastle();
        Feature castleFeature = castle.getFeatures().stream().filter(f -> f instanceof Castle).findFirst().get();

        castle.setOwner(player1);
        assertTrue(board.placeTile(new Coordinates(0, 1), castle));
        assertTrue(board.placeMeeple(castleFeature, player1));

        castle = Rules.getHugeCastle();
        castleFeature = castle.getFeatures().stream().filter(f -> f instanceof Castle).findFirst().get();

        assertTrue(board.placeTile(new Coordinates(0, -1), castle));
        assertFalse(board.placeMeeple(castleFeature, player2));
    }

    @Test
    void testJoiningTwoSeparatelyOwnedRoads() {
        board = new Board(Rules.getStraightRoad());

        Player player1 = new Player(Colour.RED);
        Player player2 = new Player(Colour.BLUE);

        Tile road = Rules.getCurvyRoad();
        Feature roadFeature = road.getFeatures().stream().filter(f -> f instanceof Road).findFirst().get();

        road.setOwner(player1);
        road.rotateClockwise();

        assertTrue(board.placeTile(new Coordinates(0, 1), road));
        assertTrue(board.placeMeeple(roadFeature, player1));

        road = Rules.getStraightRoad();
        roadFeature = road.getFeatures().stream().filter(f -> f instanceof Road).findFirst().get();

        road.setOwner(player2);
        assertTrue(board.placeTile(new Coordinates(1, 0), road));
        assertTrue(board.placeMeeple(roadFeature, player2));

        road = Rules.getCurvyRoad();
        roadFeature = road.getFeatures().stream().filter(f -> f instanceof Road).findFirst().get();

        road.setOwner(player1);
        road.rotateClockwise(2);

        assertTrue(board.placeTile(new Coordinates(1, 1), road));
        board.printOpenFeatures();
        System.out.println("Number of features: " + board.getOpenFeatures().size());
        assertTrue(board.placeMeeple(roadFeature, player1));
    }

    @Test
    void testJoiningTwoSeparatelyOwnedCastles() {
        board = new Board(Rules.getLongCastle());

        Player player1 = new Player(Colour.RED);
        Player player2 = new Player(Colour.BLUE);

        Tile castle = Rules.getLongCastle();
        Feature castleFeature = castle.getFeatures().stream().filter(f -> f instanceof Castle).findFirst().get();

        castle.setOwner(player1);

        assertTrue(board.placeTile(new Coordinates(0, 1), castle));
        assertTrue(board.placeMeeple(castleFeature, player1));

        castle = Rules.getHugeCastle();
        castleFeature = castle.getFeatures().stream().filter(f -> f instanceof Castle).findFirst().get();

        castle.setOwner(player2);
        assertTrue(board.placeTile(new Coordinates(1, 0), castle));
        assertTrue(board.placeMeeple(castleFeature, player2));

        castle = Rules.getHugeCastle();
        castleFeature = castle.getFeatures().stream().filter(f -> f instanceof Castle).findFirst().get();

        castle.setOwner(player1);
        assertTrue(board.placeTile(new Coordinates(1, 1), castle));
        assertTrue(board.placeMeeple(castleFeature, player1));
    }

    @Test
    void testJoiningTwoSeparatelyOwnedFields() {
        board = new Board(Rules.getStraightRoad());

        Player player1 = new Player(Colour.RED);
        Player player2 = new Player(Colour.BLUE);

        Tile curvyRoad = Rules.getCurvyRoad();
        Feature fieldFeature = curvyRoad.getFeatures().stream()
                .filter(f -> f instanceof Field)
                .filter(f -> f.getCardinalPoints().size() == 2).findFirst().get();

        curvyRoad.setOwner(player1);

        assertTrue(board.placeTile(new Coordinates(0, -1), curvyRoad));
        assertTrue(board.placeMeeple(fieldFeature, player1));

        curvyRoad = Rules.getCurvyRoad();
        fieldFeature = curvyRoad.getFeatures().stream()
                .filter(f -> f instanceof Field)
                .filter(f -> f.getCardinalPoints().size() == 8).findFirst().get();

        curvyRoad.setOwner(player2);
        curvyRoad.rotateClockwise();
        assertTrue(board.placeTile(new Coordinates(0, 1), curvyRoad));
        assertTrue(board.placeMeeple(fieldFeature, player2));

        Tile singleField = Rules.getMonasteryWithRoad();
        fieldFeature = singleField.getFeatures().stream().filter(f -> f instanceof Field).findFirst().get();

        singleField.setOwner(player1);
        singleField.rotateClockwise(1);
        assertTrue(board.placeTile(new Coordinates(1, 1), singleField));
        assertTrue(board.placeMeeple(fieldFeature, player1));
    }

    // SCORING TESTS

    // ROAD

    @Test
    void testScoreClosedTwoTileRoad() {
        board = new Board(Rules.getMonasteryWithRoad());
        Player player = new Player(Colour.RED);

        Tile singleRoad = Rules.getMonasteryWithRoad();
        Feature roadFeature = singleRoad.getFeatures().stream().filter(f -> f instanceof Road).findFirst().get();

        singleRoad.rotateClockwise(2);
        singleRoad.setOwner(player);

        assertTrue(board.placeTile(new Coordinates(0, -1), singleRoad));
        assertTrue(board.placeMeeple(roadFeature, player));

        assertEquals(1, board.getClosedFeatures().size());
        assertEquals(1, board.getNewlyClosedFeatures().size());
        assertEquals(3, board.getOpenFeatures().size());

        assertEquals(0, player.getScore());

        board.scoreClosedFeatures();

        assertEquals(Rules.ROAD_POINTS_CLOSED * 2, player.getScore());
    }

    @Test
    void testScoreOpenTwoTileRoad() {
        board = new Board(Rules.getMonasteryWithRoad());
        Player player = new Player(Colour.RED);

        Tile straightRoad = Rules.getStraightRoad();
        Feature roadFeature = straightRoad.getFeatures().stream().filter(f -> f instanceof Road).findFirst().get();

        straightRoad.setOwner(player);

        assertTrue(board.placeTile(new Coordinates(0, -1), straightRoad));
        assertTrue(board.placeMeeple(roadFeature, player));

        assertEquals(3, board.getOpenFeatures().size());
        assertEquals(0, board.getClosedFeatures().size());
        assertEquals(0, board.getNewlyClosedFeatures().size());
        assertEquals(0, player.getScore());

        board.scoreOpenFeatures();

        assertEquals(Rules.ROAD_POINTS_OPEN * 2, player.getScore());
    }

    @Test
    void testScoreContestedClosedFourTileRoad() {
        board = new Board(Rules.getMonasteryWithRoad());
        Player player1 = new Player(Colour.RED);
        Player player2 = new Player(Colour.BLUE);

        Tile curvyRoad = Rules.getCurvyRoad();
        Tile singleRoad = Rules.getMonasteryWithRoad();
        Feature roadFeature = singleRoad.getFeatures().stream().filter(f -> f instanceof Road).findFirst().get();

        singleRoad.setOwner(player1);

        assertTrue(board.placeTile(new Coordinates(1, 0), singleRoad));
        assertTrue(board.placeMeeple(roadFeature, player1));

        curvyRoad.setOwner(player2);
        roadFeature = curvyRoad.getFeatures().stream().filter(f -> f instanceof Road).findFirst().get();

        assertTrue(board.placeTile(new Coordinates(0, -1), curvyRoad));
        assertTrue(board.placeMeeple(roadFeature, player2));

        curvyRoad = Rules.getCurvyRoad();
        curvyRoad.rotateClockwise(3);
        curvyRoad.setOwner(player1);
        roadFeature = curvyRoad.getFeatures().stream().filter(f -> f instanceof Road).findFirst().get();

        assertTrue(board.placeTile(new Coordinates(1, -1), curvyRoad));

        assertEquals(0, player1.getScore());
        assertEquals(0, player2.getScore());

        board.scoreClosedFeatures();

        assertEquals(Rules.ROAD_POINTS_CLOSED * 4, player1.getScore());
        assertEquals(Rules.ROAD_POINTS_CLOSED * 4, player2.getScore());

        board.printBoard();
    }

    @Test
    void testScoreClosedFourTileFiveEdgeRoad(){
        board = new Board(Rules.getFourRoadIntersection());
        Player player = new Player(Colour.RED);

        Tile curvyRoad = Rules.getCurvyRoad();
        Feature roadFeature = curvyRoad.getFeatures().stream().filter(f -> f instanceof Road).findFirst().get();

        curvyRoad.setOwner(player);

        assertTrue(board.placeTile(new Coordinates(0, -1), curvyRoad));
        assertTrue(board.placeMeeple(roadFeature, player));

        curvyRoad = Rules.getCurvyRoad();
        curvyRoad.rotateClockwise(3);
        assertTrue(board.placeTile(new Coordinates(1, -1), curvyRoad));

        curvyRoad = Rules.getCurvyRoad();
        curvyRoad.rotateClockwise(2);
        assertTrue(board.placeTile(new Coordinates(1, 0), curvyRoad));

        assertEquals(0, player.getScore());

        board.scoreClosedFeatures();

        assertEquals(Rules.ROAD_POINTS_CLOSED * 4, player.getScore());
    }
    
    // CASTLE

    @Test
    void testScoreClosedTwoTileCastle(){
        board = new Board(Rules.getSingleCastle());
        Player player = new Player(Colour.RED);

        Tile singleCastle = Rules.getSingleCastle();
        Feature castleFeature = singleCastle.getFeatures().stream().filter(f -> f instanceof Castle).findFirst().get();

        singleCastle.rotateClockwise(2);
        singleCastle.setOwner(player);

        assertTrue(board.placeTile(new Coordinates(0, 1), singleCastle));
        assertTrue(board.placeMeeple(castleFeature, player));

        assertEquals(1, board.getClosedFeatures().size());
        assertEquals(1, board.getNewlyClosedFeatures().size());
        assertEquals(2, board.getOpenFeatures().size());

        assertEquals(0, player.getScore());

        board.scoreClosedFeatures();

        assertEquals(Rules.CASTLE_POINTS_CLOSED * 2, player.getScore());
    }

    @Test
    void testScoreOpenTwoTileCastle(){
        board = new Board(Rules.getSingleCastle());
        Player player = new Player(Colour.RED);

        Tile singleCastle = Rules.getLongCastle();
        Feature castleFeature = singleCastle.getFeatures().stream().filter(f -> f instanceof Castle).findFirst().get();

        singleCastle.rotateClockwise(1);
        singleCastle.setOwner(player);

        assertTrue(board.placeTile(new Coordinates(0, 1), singleCastle));
        assertTrue(board.placeMeeple(castleFeature, player));

        assertEquals(0, board.getClosedFeatures().size());
        assertEquals(0, board.getNewlyClosedFeatures().size());
        assertEquals(4, board.getOpenFeatures().size());

        assertEquals(0, player.getScore());

        board.scoreOpenFeatures();

        assertEquals(Rules.CASTLE_POINTS_OPEN * 2, player.getScore());
    }

    @Test
    void testScoreOpenTwoTileCastleWithShield(){
        board = new Board(Rules.getSingleCastle());
        Player player = new Player(Colour.RED);

        Tile singleCastle = Rules.getLongCastleWithShield();
        Feature castleFeature = singleCastle.getFeatures().stream().filter(f -> f instanceof Castle).findFirst().get();

        singleCastle.rotateClockwise(1);
        singleCastle.setOwner(player);

        assertTrue(board.placeTile(new Coordinates(0, 1), singleCastle));
        assertTrue(board.placeMeeple(castleFeature, player));

        assertEquals(0, board.getClosedFeatures().size());
        assertEquals(0, board.getNewlyClosedFeatures().size());
        assertEquals(4, board.getOpenFeatures().size());

        assertEquals(0, player.getScore());

        board.scoreOpenFeatures();

        assertEquals(Rules.CASTLE_POINTS_OPEN * 2 + Rules.SHIELD_POINTS_OPEN, player.getScore());
    }

    @Test
    void testScoreClosedThreeTileCastleWithShield(){
        board = new Board(Rules.getSingleCastle());
        Player player = new Player(Colour.RED);

        Tile singleCastle = Rules.getLongCastleWithShield();
        Feature castleFeature = singleCastle.getFeatures().stream().filter(f -> f instanceof Castle).findFirst().get();

        singleCastle.rotateClockwise(1);
        singleCastle.setOwner(player);

        assertTrue(board.placeTile(new Coordinates(0, 1), singleCastle));
        assertTrue(board.placeMeeple(castleFeature, player));

        singleCastle = Rules.getSingleCastle();
        singleCastle.rotateClockwise(2);
        singleCastle.setOwner(player);

        assertTrue(board.placeTile(new Coordinates(0, 2), singleCastle));

        assertEquals(1, board.getClosedFeatures().size());
        assertEquals(1, board.getNewlyClosedFeatures().size());
        assertEquals(4, board.getOpenFeatures().size());

        assertEquals(0, player.getScore());

        board.scoreClosedFeatures();

        assertEquals(Rules.CASTLE_POINTS_CLOSED * 3 + Rules.SHIELD_POINTS_CLOSED, player.getScore());
    }

    @Test
    void testScoreFourTileFiveEdgeCastle(){
        Tile startingCastle = Rules.getTwoSingleCastlesAdjacent();
        startingCastle.rotateClockwise();
        board = new Board(startingCastle);
        Player player = new Player(Colour.RED);

        Tile curvyCastle = Rules.getCurvyCastle();
        Feature castleFeature = curvyCastle.getFeatures().stream().filter(f -> f instanceof Castle).findFirst().get();

        curvyCastle.setOwner(player);

        assertTrue(board.placeTile(new Coordinates(0, -1), curvyCastle));
        assertTrue(board.placeMeeple(castleFeature, player));

        curvyCastle = Rules.getCurvyCastle();
        curvyCastle.rotateClockwise(3);
        assertTrue(board.placeTile(new Coordinates(1, -1), curvyCastle));

        curvyCastle = Rules.getCurvyCastle();
        curvyCastle.rotateClockwise(2);
        assertTrue(board.placeTile(new Coordinates(1, 0), curvyCastle));

        assertEquals(0, player.getScore());

        board.scoreClosedFeatures();

        assertEquals(Rules.CASTLE_POINTS_CLOSED * 4, player.getScore());
    }

    // FIELD

    @Test
    void testScoreFieldWithOpenAdjacentCastle(){
        board = new Board(Rules.getSingleCastle());
        Player player = new Player(Colour.RED);

        Tile singleCastle = Rules.getSingleCastle();
        Feature fieldFeature = singleCastle.getFeatures().stream().filter(f -> f instanceof Field).findFirst().get();

        singleCastle.setOwner(player);
        singleCastle.rotateClockwise();

        assertTrue(board.placeTile(new Coordinates(0, -1), singleCastle));
        assertTrue(board.placeMeeple(fieldFeature, player));

        assertEquals(0, board.getClosedFeatures().size());
        assertEquals(0, board.getNewlyClosedFeatures().size());
        assertEquals(3, board.getOpenFeatures().size());

        assertEquals(0, player.getScore());

        board.scoreOpenFeatures();

        assertEquals(0, player.getScore());
    }
    
    @Test
    void testScoreFieldWithOneClosedAdjacentCastle(){
        board = new Board(Rules.getSingleCastle());
        Player player = new Player(Colour.RED);

        Tile singleCastle = Rules.getSingleCastle();
        Feature fieldFeature = singleCastle.getFeatures().stream().filter(f -> f instanceof Field).findFirst().get();

        singleCastle.setOwner(player);
        singleCastle.rotateClockwise();

        assertTrue(board.placeTile(new Coordinates(0, -1), singleCastle));
        assertTrue(board.placeMeeple(fieldFeature, player));

        singleCastle = Rules.getSingleCastle();
        singleCastle.rotateClockwise(2);

        assertTrue(board.placeTile(new Coordinates(0, 1), singleCastle));

        assertEquals(1, board.getClosedFeatures().size());
        assertEquals(1, board.getNewlyClosedFeatures().size());
        assertEquals(3, board.getOpenFeatures().size());

        assertEquals(0, player.getScore());

        board.scoreOpenFeatures();

        assertEquals(Rules.FIELD_POINTS_PER_CASTLE, player.getScore());
    }

    @Test
    void testScoreFieldWithTwoClosedAdjacentCastle(){
        board = new Board(Rules.getSingleCastle());
        Player player = new Player(Colour.RED);

        Tile singleCastle = Rules.getSingleCastle();
        Feature fieldFeature = singleCastle.getFeatures().stream().filter(f -> f instanceof Field).findFirst().get();

        singleCastle.setOwner(player);
        singleCastle.rotateClockwise();

        assertTrue(board.placeTile(new Coordinates(0, -1), singleCastle));
        assertTrue(board.placeMeeple(fieldFeature, player));

        singleCastle = Rules.getSingleCastle();
        singleCastle.rotateClockwise(2);

        assertTrue(board.placeTile(new Coordinates(0, 1), singleCastle));
        
        singleCastle = Rules.getSingleCastle();
        singleCastle.rotateClockwise(3);

        assertTrue(board.placeTile(new Coordinates(1, -1), singleCastle));

        assertEquals(2, board.getClosedFeatures().size());
        assertEquals(1, board.getNewlyClosedFeatures().size());
        assertEquals(3, board.getOpenFeatures().size());

        assertEquals(0, player.getScore());

        board.scoreOpenFeatures();

        assertEquals(Rules.FIELD_POINTS_PER_CASTLE * 2, player.getScore());
    }
    
    @Test
    void testScoreContestedField(){
        board = new Board(Rules.getSingleCastle());
        Player player1 = new Player(Colour.RED);
        Player player2 = new Player(Colour.BLUE);

        Tile castle = Rules.getSingleCastle();
        Feature fieldFeature = castle.getFeatures().stream().filter(f -> f instanceof Field).findFirst().get();

        castle.setOwner(player1);

        assertTrue(board.placeTile(new Coordinates(-1, 0), castle));
        assertTrue(board.placeMeeple(fieldFeature, player1));

        castle = Rules.getCurvyCastle();
        castle.rotateClockwise(1);

        assertTrue(board.placeTile(new Coordinates(0, 1), castle));

        castle = Rules.getSingleCastle();
        castle.rotateClockwise(3);
        fieldFeature = castle.getFeatures().stream().filter(f -> f instanceof Field).findFirst().get();

        assertTrue(board.placeTile(new Coordinates(1, 1), castle));
        assertTrue(board.placeMeeple(fieldFeature, player2));

        castle = Rules.getCurvyCastle();
        castle.rotateClockwise(1);

        assertTrue(board.placeTile(new Coordinates(1, 0), castle));

        assertEquals(1, board.getClosedFeatures().size());
        assertEquals(4, board.getOpenFeatures().size());

        assertEquals(0, player1.getScore());
        assertEquals(0, player2.getScore());

        board.scoreOpenFeatures();

        assertEquals(Rules.FIELD_POINTS_PER_CASTLE, player1.getScore());
        assertEquals(Rules.FIELD_POINTS_PER_CASTLE, player2.getScore());
    }
    
    // MONASTERY

    @Test
    void testOpenMonastery(){
        board = new Board(Rules.getSingleCastle());
        Player player = new Player(Colour.RED);

        Tile monastery = Rules.getMonastery();
        Feature monasteryFeature = monastery.getFeatures().stream().filter(f -> f instanceof Monastery).findFirst().get();

        monastery.setOwner(player);
        monastery.rotateClockwise();

        assertTrue(board.placeTile(new Coordinates(0, -1), monastery));
        assertTrue(board.placeMeeple(monasteryFeature, player));

        assertEquals(0, board.getClosedFeatures().size());
        assertEquals(0, board.getNewlyClosedFeatures().size());
        assertEquals(3, board.getOpenFeatures().size());

        assertEquals(0, player.getScore());

        board.scoreOpenFeatures();

        assertEquals(Rules.MONASTERY_POINTS_PER_TILE * 2, player.getScore());
    }

    @Test
    void testClosedMonastery(){
        Tile startingRoad = Rules.getStraightRoad();
        startingRoad.rotateClockwise();
        board = new Board(startingRoad);
        Player player = new Player(Colour.RED);

        Tile road = Rules.getCurvyRoad();

        road.rotateClockwise();
        assertTrue(board.placeTile(new Coordinates(-1, 0), road));

        road = Rules.getCurvyRoad();
        road.rotateClockwise(2);
        assertTrue(board.placeTile(new Coordinates(1, 0), road));

        road = Rules.getStraightRoad();
        assertTrue(board.placeTile(new Coordinates(1, -1), road));

        road = Rules.getStraightRoad();
        assertTrue(board.placeTile(new Coordinates(-1, -1), road));

        road = Rules.getCurvyRoad();
        assertTrue(board.placeTile(new Coordinates(-1, -2), road));

        road = Rules.getCurvyRoad();
        road.rotateClockwise(3);
        assertTrue(board.placeTile(new Coordinates(1, -2), road));

        road = Rules.getStraightRoad();
        road.rotateClockwise();
        assertTrue(board.placeTile(new Coordinates(0, -2), road));

        Tile monastery = Rules.getMonastery();
        Feature monasteryFeature = monastery.getFeatures().stream().filter(f -> f instanceof Monastery).findFirst().get();

        assertTrue(board.placeTile(new Coordinates(0, -1), monastery));
        assertTrue(board.placeMeeple(monasteryFeature, player));

        assertEquals(2, board.getClosedFeatures().size());
        assertEquals(1, board.getNewlyClosedFeatures().size());
        assertEquals(2, board.getOpenFeatures().size());

        assertEquals(0, player.getScore());

        board.scoreClosedFeatures();

        assertEquals(Rules.MONASTERY_POINTS_PER_TILE * 9, player.getScore());
    }
}