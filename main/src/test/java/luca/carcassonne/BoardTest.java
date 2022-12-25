package luca.carcassonne;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void testAddingTileIncreasesBoardSize(){
        Tile tile = new Tile(SideFeature.CASTLE, SideFeature.CASTLE, SideFeature.CASTLE, SideFeature.CASTLE);
        board = new Board(startingTile);

        board.placeTile(new Coordinates(0, 1),tile);

        assertEquals(2, board.getPlacedTiles().size());
    }

    @Test
    void testCastlePlacement(){
        Tile tile = new Tile(SideFeature.CASTLE, SideFeature.CASTLE, SideFeature.CASTLE, SideFeature.CASTLE);
        board = new Board(startingTile);

        assertTrue(board.placeTile(new Coordinates(0, 1), tile));
        assertFalse(board.placeTile(new Coordinates(1, 0), tile));
        assertFalse(board.placeTile(new Coordinates(0, -1), tile));
        assertFalse(board.placeTile(new Coordinates(-1, 0), tile));
    }

    @Test
    void testFieldPlacement(){
        Tile tile = new Tile(SideFeature.FIELD, SideFeature.FIELD, SideFeature.FIELD, SideFeature.FIELD);
        board = new Board(startingTile);

        assertFalse(board.placeTile(new Coordinates(0, 1), tile));
        assertFalse(board.placeTile(new Coordinates(1, 0), tile));
        assertTrue(board.placeTile(new Coordinates(0, -1), tile));
        assertFalse(board.placeTile(new Coordinates(-1, 0), tile));
    }

    @Test
    void testRoadPlacement(){
        Tile tile = new Tile(SideFeature.ROAD, SideFeature.ROAD, SideFeature.ROAD, SideFeature.ROAD);
        board = new Board(new Tile(SideFeature.CASTLE, SideFeature.ROAD, SideFeature.FIELD, SideFeature.ROAD));

        assertFalse(board.placeTile(new Coordinates(0, 1), tile));
        assertTrue(board.placeTile(new Coordinates(1, 0), tile));
        assertFalse(board.placeTile(new Coordinates(0, -1), tile));
        assertTrue(board.placeTile(new Coordinates(-1, 0), tile));
    }

    @Test
    void testClosingTwoTileCastle(){
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
    void testClosingThreeTileCastleCastle(){
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
}
