package luca.carcassonne;

import java.util.ArrayList;
import java.util.Stack;
import java.util.stream.Collectors;

import luca.carcassonne.mcts.Move;
import luca.carcassonne.mcts.State;
import luca.carcassonne.player.GreedyAgent;
import luca.carcassonne.player.MonteCarloAgent;
import luca.carcassonne.player.Player;
import luca.carcassonne.player.ProgressiveHistoryAgent;
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
        int originalPlayer = state.getOriginalPlayer();
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
        newState.setOriginalPlayer(originalPlayer);
        newState.setVisitCount(visitCount);
        newState.setFinalScoreDifference(finalScoreDifference);

        return newState;
    }

    public static Board clone(Board oldBoard, ArrayList<Player> newPlayers) {
        Board newBoard = new Board(CloneManager.clone(oldBoard.getStartingTile()));
        for (Move move : oldBoard.getPastMoves()) {
            Move newMove = clone(move);
            Tile tileToPlace = Settings.getTileFromId(newMove.getTileId());
            Coordinates coordinates = newMove.getCoordinates();
            int rotation = newMove.getRotation();
            int featureIndex = newMove.getFeatureIndex();
            int playerIndex = newMove.getPlayerIndex();

            tileToPlace.rotateClockwise(rotation);

            boolean placed = newBoard.placeTile(coordinates, tileToPlace);

            if (placed == false) {
                throw new RuntimeException("Error cloning Board: tile could not be placed.");
            }

            if (featureIndex != -1) {
                Player newPlayer = newPlayers.get(playerIndex);
                boolean meeplePlaced = newBoard
                        .placeMeeple(tileToPlace.getFeatures().get(featureIndex), newPlayer);

                if (!meeplePlaced) {
                    throw new RuntimeException("Error cloning Board: meeple could not be placed.");
                }
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

    public static ProgressiveHistoryAgent clone(ProgressiveHistoryAgent agent) {
        ProgressiveHistoryAgent newAgent = new ProgressiveHistoryAgent(agent.getColour(), agent.getMaxIterations(),
                agent.getExplorationConstant(), agent.getTotalActionMap(), agent.getWinningActionMap());

        newAgent.setScore(agent.getScore());
        newAgent.setAvailableMeeples(agent.getAvailableMeeples());

        return newAgent;
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

    public static GreedyAgent clone(GreedyAgent agent) {
        GreedyAgent newAgent = new GreedyAgent(agent.getColour());

        newAgent.setScore(agent.getScore());
        newAgent.setAvailableMeeples(agent.getAvailableMeeples());

        return newAgent;
    }

    public static Move clone(Move move) {
        Move newMove = new Move();

        newMove.setCoordinates(clone(move.getCoordinates()));
        newMove.setTileId(move.getTileId());
        newMove.setRotation(move.getRotation());
        newMove.setPlayerIndex(move.getPlayerIndex());
        newMove.setFeatureIndex(move.getFeatureIndex());

        return newMove;
    }
}
