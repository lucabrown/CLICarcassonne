package luca.carcassonne;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
    void testClosingThreeTileCastleCastle() {
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
    void testFourTileLoopRoad() {
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
    void testFourTileFourEdgeRoad(){
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
        Road road = curvyRoad.getFeatures().stream().filter(f -> f instanceof Road).map(f -> (Road) f).findFirst().get();
        for (SimpleGraph<Feature, DefaultEdge> feature : board.getOpenFeatures()) {
            if(feature.vertexSet().contains(road)){
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

        Tile straightRoad = Rules.getStraightRoad();

        int nOpenFeatures = board.getOpenFeatures().size();
        int nClosedFeatures = board.getClosedFeatures().size();
        int nNewlyClosedFeatures = board.getNewlyClosedFeatures().size();

        assertTrue(board.placeTile(new Coordinates(1, 0), straightRoad));

        assertEquals(0, board.getClosedFeatures().size());
    }
}
