package luca.carcassonne;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoardTest {
    Board board;

    @Test
    void testBoardStartsWithStartingTileAtOrigin() {
        Tile startingTile = Rules.STARTING_TILE;
        board = new Board();

        assertTrue(board.getPlacedTiles().contains(Rules.STARTING_TILE));
        assertEquals(0, startingTile.getCoordinates().getX());
        assertEquals(0, startingTile.getCoordinates().getY());
        assertEquals(1, board.getPlacedTiles().size());
    }

    // @Test
    // void testAddingTileIncreasesBoardSize(){
    //     Tile tile = new Tile(1, 0);
    //     board = new Board();

    //     board.placeTile((1, 0),tile);

    //     assertEquals(2, board.getPlacedTiles().size());
    // }
}
