package luca.carcassonne;

import java.util.ArrayList;
import java.util.Stack;
import java.util.stream.Collectors;

import luca.carcassonne.MCTS.Move;
import luca.carcassonne.MCTS.State;
import luca.carcassonne.player.Player;
import luca.carcassonne.tile.Coordinates;
import luca.carcassonne.tile.Tile;

// Magnificent aren't they?
public class CloneManager {
    public static State clone(State state) {
        Board board = state.getBoard();
        ArrayList<Player> players = state.getPlayers();
        Stack<Tile> availableTiles = state.getAvailableTiles();
        Tile currentTile = state.getCurrentTile();
        int currentPlayer = state.getCurrentPlayer();
        int visitCount = state.getVisitCount();
        int finalScoreDifference = state.getFinalScoreDifference();

        State newState = new State();
        ArrayList<Player> newPlayers = (ArrayList<Player>) players.stream().map(p -> clone(p))
                .collect(Collectors.toCollection(ArrayList::new));
        Stack<Tile> newAvailableTiles = (Stack<Tile>) availableTiles.stream().map(t -> clone(t))
                .collect(Collectors.toCollection(Stack::new));
        Tile newCurrentTile = clone(currentTile);
        Board newBoard = cloneBoard(board, newPlayers);

        newState.setBoard(newBoard);
        newState.setPlayers(newPlayers);
        newState.setAvailableTiles(newAvailableTiles);
        newState.setCurrentTile(newCurrentTile);
        newState.setCurrentPlayer(currentPlayer);
        newState.setVisitCount(visitCount);
        newState.setFinalScoreDifference(finalScoreDifference);

        // System.out.println("Cloning state: " + newState.hashCode());
        // System.out.println("- Players: " + (newState.getPlayers() == null ? "null" :
        // newState.getPlayers().size()));
        // System.out.println("- Available tiles: "
        // + (newState.getAvailableTiles() == null ? "null" :
        // newState.getAvailableTiles().size()));
        // System.out.println(
        // "- Current tile: " + (newState.getCurrentTile() == null ? "null" :
        // newState.getCurrentTile().getId()));
        // System.out.println("- Current player: " + newState.getCurrentPlayer());
        // System.out.println("- Visit count: " + newState.getVisitCount());
        // System.out.println("- Final score difference: " +
        // newState.getFinalScoreDifference());

        return newState;
    }

    public static Board cloneBoard(Board oldBoard, ArrayList<Player> newPlayers) {
        Board newBoard = new Board();
        // System.out.println("- Cloning board: " + newBoard.hashCode());
        // System.out.println("- - Starting Tile: "
        // + (newBoard.getStartingTile() == null ? "null" :
        // newBoard.getStartingTile().getId()));
        // System.out.println(
        // "- - Past Moves: " + (newBoard.getPastMoves() == null ? "null" :
        // newBoard.getPastMoves().size()));
        // System.out.println(
        // "- - Placed tiles: " + (newBoard.getPlacedTiles() == null ? "null" :
        // newBoard.getPlacedTiles().size()));
        // System.out.println("- - Open Features: "
        // + (newBoard.getOpenFeatures() == null ? "null" :
        // newBoard.getOpenFeatures().size()));
        // System.out.println("- - Closed Features: "
        // + (newBoard.getClosedFeatures() == null ? "null" :
        // newBoard.getClosedFeatures().size()));

        newBoard.setMaxX(oldBoard.getMaxX());
        newBoard.setMaxY(oldBoard.getMaxY());
        newBoard.setMinX(oldBoard.getMinX());
        newBoard.setMinY(oldBoard.getMinY());
        newBoard.setHeight(oldBoard.getHeight());
        newBoard.setWidth(oldBoard.getWidth());
        // System.out.println("- Moves before: " + newBoard.getPastMoves().size());
        for (Move move : oldBoard.getPastMoves()) {
            Move newMove = clone(move);

            // System.out.println("- Making move");
            // System.out.println("- New board: " + (newBoard == null ? "null" :
            // newBoard.hashCode()));
            // System.out.println("- New move: " + (newMove == null ? "null" : newMove));
            Tile tileToPlace = newMove.getTile();
            tileToPlace.rotateClockwise(newMove.getRotation());

            boolean placed = newBoard.placeTile(newMove.getCoordinates(), tileToPlace);
            if (placed == false) {
                throw new RuntimeException("Error cloning Board: tile could not be placed.");
            }

            if (newMove.getFeatureIndex() != -1) {
                Player newPlayer = newPlayers.get(newMove.getPlayerIndex());
                newBoard.placeMeeple(newMove.getTile().getFeatures().get(newMove.getFeatureIndex()), newPlayer);
            }

            ScoreManager.scoreClosedFeatures(newBoard);
            newBoard.addNewMove(newMove);
        }
        // System.out.println("- Moves after: " + newBoard.getPastMoves().size());

        return newBoard;
    }

    public static Tile clone(Tile tile) {
        return (tile == null ? null : Settings.getTileFromId(tile.getId()));
    }

    public static Coordinates clone(Coordinates coordinates) {
        return new Coordinates(coordinates.getX(), coordinates.getY());
    }

    public static Player clone(Player player) {
        Player newPlayer = new Player(player.getColour());

        newPlayer.setScore(player.getScore());
        newPlayer.setAvailableMeeples(player.getAvailableMeeples());
        newPlayer.setBehaviour(player.getBehaviour());

        return newPlayer;
    }

    public static Move clone(Move move) {
        Move newMove = new Move();

        newMove.setCoordinates(clone(move.getCoordinates()));
        newMove.setTile(clone(move.getTile()));
        newMove.setRotation(move.getRotation());
        newMove.setPlayerIndex(move.getPlayerIndex());
        newMove.setFeatureIndex(move.getFeatureIndex());

        return newMove;
    }
}
