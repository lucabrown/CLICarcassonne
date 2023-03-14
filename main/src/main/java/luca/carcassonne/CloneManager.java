package luca.carcassonne;

import java.util.ArrayList;
import java.util.Stack;
import java.util.stream.Collectors;

import luca.carcassonne.mcts.Move;
import luca.carcassonne.mcts.State;
import luca.carcassonne.player.MonteCarloAgent;
import luca.carcassonne.player.Player;
import luca.carcassonne.player.RandomAgent;
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
        Board newBoard = clone(board, newPlayers);

        newState.setBoard(newBoard);
        newState.setPlayers(newPlayers);
        newState.setAvailableTiles(newAvailableTiles);
        newState.setCurrentTile(newCurrentTile);
        newState.setCurrentPlayer(currentPlayer);
        newState.setVisitCount(visitCount);
        newState.setFinalScoreDifference(finalScoreDifference);

        return newState;
    }

    public static Board clone(Board oldBoard, ArrayList<Player> newPlayers) {
        Board newBoard = new Board();

        for (Move move : oldBoard.getPastMoves()) {
            Move newMove = clone(move);
            Tile tileToPlace = newMove.getTile();

            tileToPlace.rotateClockwise(newMove.getRotation());

            boolean placed = newBoard.placeTile(newMove.getCoordinates(), tileToPlace);

            if (placed == false) {
                throw new RuntimeException("Error cloning Board: tile could not be placed.");
            }

            if (newMove.getFeatureIndex() != -1) {
                Player newPlayer = newPlayers.get(newMove.getPlayerIndex());
                boolean meeplePlaced = newBoard
                        .placeMeeple(newMove.getTile().getFeatures().get(newMove.getFeatureIndex()), newPlayer);
                // if (!meeplePlaced) {
                // throw new RuntimeException("Error cloning Board: meeple could not be
                // placed.");
                // }
            }

            ScoreManager.scoreClosedFeatures(newBoard);
            newBoard.addNewMove(newMove);
        }

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
        newPlayer.setAvailableMeeples(Settings.MAX_MEEPLES);

        return newPlayer;
    }

    public static MonteCarloAgent clone(MonteCarloAgent agent) {
        MonteCarloAgent newAgent = new MonteCarloAgent(agent.getColour(), agent.getMaxIterations(),
                agent.getExplorationConstant());

        newAgent.setScore(agent.getScore());
        newAgent.setAvailableMeeples(agent.getAvailableMeeples());

        return newAgent;
    }

    public static RandomAgent clone(RandomAgent agent) {
        RandomAgent newAgent = new RandomAgent(agent.getColour());

        newAgent.setScore(agent.getScore());
        newAgent.setAvailableMeeples(agent.getAvailableMeeples());

        return newAgent;
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
