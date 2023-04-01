package luca.carcassonne.mcts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

import luca.carcassonne.Board;
import luca.carcassonne.CloneManager;
import luca.carcassonne.ScoreManager;
import luca.carcassonne.Settings;
import luca.carcassonne.player.Player;
import luca.carcassonne.tile.Coordinates;
import luca.carcassonne.tile.SideFeature;
import luca.carcassonne.tile.Tile;
import luca.carcassonne.tile.feature.Feature;
import luca.carcassonne.tile.feature.Field;

/**
 * Represents a game state.
 * 
 * A game state is the state of the game at any point in time.
 * 
 * It is represented by a board, a list of players, a list of available tiles,
 * and the current player.
 * 
 * @author Luca Brown
 */
public class State {
    private Board board;
    private int originalPlayer;
    private int currentPlayer;
    private Tile currentTile;
    private Stack<Tile> availableTiles;
    private ArrayList<Player> players;
    private int visitCount;
    private int finalScoreDifference;

    public State() {
        this.board = null;
        this.originalPlayer = -1;
        this.currentPlayer = -1;
        this.currentTile = null;
        this.availableTiles = null;
        this.players = null;
        this.visitCount = 0;
        this.finalScoreDifference = 0;
    }

    public State(Board startingBoard, int startingPlayer, Tile currentTile,
            ArrayList<Player> players,
            Stack<Tile> availableTiles) {
        this.board = startingBoard;
        this.originalPlayer = startingPlayer;
        this.currentPlayer = startingPlayer;
        this.currentTile = currentTile;
        this.players = players;
        this.availableTiles = availableTiles;
        this.visitCount = 0;
        this.finalScoreDifference = 0;
    }

    /**
     * Returns all possible child states reachable from this state.
     * 
     * @return All possible child states reachable from this state.
     */
    public ArrayList<State> getAllPossibleChildStates() {
        ArrayList<State> possibleChildStates = new ArrayList<>();
        List<Coordinates> possibleCoordinates = this.board.getPossibleCoordinates();
        int rotations = getSymmetricRotations(currentTile);

        State junkState = CloneManager.clone(this);

        for (Coordinates coordinates : possibleCoordinates) {
            for (int i = 0; i < rotations; i++) {
                // for (int j = 0; j < currentTile.getFeatures().size() + 1; j++) {
                // State junkState = CloneManager.clone(this);
                Tile junkTile = CloneManager.clone(currentTile);
                Coordinates junkCoordinates = CloneManager.clone(coordinates);

                junkTile.rotateClockwise(i);

                boolean placed = junkState.getBoard().canPlaceJunkTile(junkCoordinates, junkTile);
                boolean meeplePlaced = false;

                if (placed) {
                    for (int k = 0; k < currentTile.getFeatures().size(); k++) {
                        State newState = CloneManager.clone(this);
                        Tile newTile = CloneManager.clone(newState.getCurrentTile());
                        Coordinates newCoordinates = CloneManager.clone(coordinates);

                        if (newState.getPlayers().get(currentPlayer).getAvailableMeeples() == 0) {
                            break;
                        }

                        newTile.rotateClockwise(i);

                        if (!newState.getBoard().placeTile(newCoordinates, newTile)) {
                            throw new RuntimeException("Tile could not be placed to determine child.");
                        }

                        meeplePlaced = newState.getBoard().placeMeeple(newTile.getFeatures().get(k),
                                newState.getPlayers().get(currentPlayer));

                        if (!meeplePlaced) {
                            // throw new RuntimeException("Meeple could not be placed to determine child.");
                            continue;
                        }

                        newState.getBoard()
                                .addNewMove(new Move(newCoordinates, newTile.getId(), i, currentPlayer, k));

                        ScoreManager.scoreClosedFeatures(newState.getBoard(), false);

                        newState.setCurrentPlayer((currentPlayer + 1) % players.size());
                        newState.setOriginalPlayer(originalPlayer);
                        newState.setCurrentTile(
                                (newState.getAvailableTiles().isEmpty() ? null
                                        : newState.getAvailableTiles().pop()));

                        possibleChildStates.add(newState);
                    }

                    State newState = CloneManager.clone(this);
                    Tile newTile = CloneManager.clone(newState.getCurrentTile());
                    Coordinates newCoordinates = CloneManager.clone(coordinates);

                    newTile.rotateClockwise(i);

                    if (!newState.getBoard().placeTile(newCoordinates, newTile)) {
                        throw new RuntimeException("Tile could not be placed to determine child.");
                    }

                    newState.getBoard().addNewMove(new Move(newCoordinates, newTile.getId(), i, currentPlayer));

                    ScoreManager.scoreClosedFeatures(newState.getBoard(), false);

                    newState.setCurrentPlayer((currentPlayer + 1) % players.size());
                    newState.setOriginalPlayer(originalPlayer);
                    newState.setCurrentTile(
                            (newState.getAvailableTiles().isEmpty() ? null : newState.getAvailableTiles().pop()));

                    possibleChildStates.add(newState);

                }
            }
        }

        return possibleChildStates;

    }

    public void incrementVisit() {
        visitCount++;
    }

    /**
     * Performs a random play from this state.
     * 
     * @return The score difference between the original player and the player who
     *         won.
     */
    public int randomPlay() {
        State newState = CloneManager.clone(this);
        Board newBoard = newState.getBoard();
        Tile newCurrentTile = newState.getCurrentTile();
        Stack<Tile> newAvailableTiles = newState.getAvailableTiles();
        ArrayList<Player> newPlayers = newState.getPlayers();
        int newCurrentPlayer = currentPlayer;

        HashMap<Coordinates, HashSet<Integer>> checkedCombinations;
        HashSet<Integer> checkedRotations;
        newAvailableTiles.push(newCurrentTile);

        while (!newAvailableTiles.empty() && currentTile != null) {
            boolean isPlaced = false;
            boolean playerPlacedTile = true;

            newCurrentTile = newAvailableTiles.pop();
            checkedCombinations = new HashMap<>();
            checkedRotations = new HashSet<>();

            while (!isPlaced) {

                Coordinates randomCoordinates = newBoard.getPossibleCoordinates()
                        .get(Settings.getRandomInt(newBoard.getPossibleCoordinates()
                                .size()));

                int randomRotation = Settings.getRandomInt(4);
                newCurrentTile.rotateClockwise(randomRotation);

                // Check if the tile can go in the given coordinates with the given rotation
                isPlaced = newBoard.placeTile(randomCoordinates, newCurrentTile);

                if (!isPlaced) {

                    // If tile is not placed, keep a record of checked combination
                    if (updateCheckedCombinations(checkedCombinations, checkedRotations, randomCoordinates,
                            randomRotation, newBoard.getPossibleCoordinates().size())) {
                        System.out.println("- - Tile not placed");
                        isPlaced = true;
                        playerPlacedTile = false;
                    }
                }
            }

            if (playerPlacedTile) {
                Object[] filteredFeature = newCurrentTile.getFeatures().stream().toArray();

                Feature randomFeature = (Feature) filteredFeature[Settings.getRandomInt(filteredFeature.length)];

                while (randomFeature.getClass() == Field.class && Settings.getRandomInt(10) < 7) {
                    randomFeature = (Feature) filteredFeature[Settings.getRandomInt(filteredFeature.length)];
                }

                // place meeple with 30% chance
                if (Settings.getRandomInt(10) < 3) {
                    board.placeMeeple(randomFeature, newPlayers.get(newCurrentPlayer));
                }

                currentTile.setOwner(newPlayers.get(newCurrentPlayer)); // to delete

                ScoreManager.scoreClosedFeatures(newBoard, false);
                newCurrentPlayer = (newCurrentPlayer + 1) % newPlayers.size();
            }
        }

        ScoreManager.scoreOpenFeatures(newBoard, false);

        finalScoreDifference = calculateScoreDifference(newPlayers, originalPlayer);
        this.setFinalScoreDifference(finalScoreDifference);

        return calculateScoreDifference(newPlayers, currentPlayer);
    }

    /**
     * Updates the checked combinations and returns true if all possible
     * combinations have been checked.
     * 
     * @param checkedCombinations     A hashmap containing the checked combinations.
     * @param checkedRotations        A hashset containing the checked rotations.
     * @param randomCoordinates       The coordinates of the tile.
     * @param randomRotation          The rotation of the tile.
     * @param possibleCoordinatesSize The number of possible coordinates.
     * @return True if all possible combinations have been checked.
     */
    private boolean updateCheckedCombinations(HashMap<Coordinates, HashSet<Integer>> checkedCombinations,
            HashSet<Integer> checkedRotations, Coordinates randomCoordinates, Integer randomRotation,
            int possibleCoordinatesSize) {

        if (!checkedCombinations.containsKey(randomCoordinates)) {
            checkedCombinations.put(randomCoordinates, new HashSet<>());
        }

        checkedRotations = checkedCombinations.get(randomCoordinates);
        checkedRotations.add(randomRotation);
        checkedCombinations.put(randomCoordinates, checkedRotations);

        if (checkedCombinations.size() == possibleCoordinatesSize
                && checkedCombinations.get(randomCoordinates).size() == 4) {
            return true;
        }

        return false;
    }

    /**
     * Calculates the score difference between the original player and the player
     * who won.
     * 
     * @param players        The players.
     * @param originalPlayer The original player.
     * @return The score difference.
     */
    public int calculateScoreDifference(ArrayList<Player> players, int originalPlayer) {
        int pointDifference = 0;

        for (int i = 0; i < players.size(); i++) {
            if (i != originalPlayer) {
                pointDifference += players.get(i).getScore();
            } else {
                pointDifference -= players.get(i).getScore();
            }
        }

        return pointDifference;
    }

    /**
     * Returns the number of symmetric rotations for a given tile.
     * 
     * Used to reduce the number of possible child states by not rotating tiles that
     * are symmetric along both axes.
     * 
     * @param tile The tile.
     * @return The number of symmetric rotations.
     */
    private int getSymmetricRotations(Tile tile) {
        int symmetricRotations = 4;
        ArrayList<SideFeature> sideFeatures = tile.getSideFeatures();

        if (sideFeatures.get(0) == sideFeatures.get(2)
                && sideFeatures.get(1) == sideFeatures
                        .get(3)
                && sideFeatures.get(0) == sideFeatures.get(3)) {
            symmetricRotations = 3;
        } else if (sideFeatures.get(0) == sideFeatures.get(2)
                && sideFeatures.get(1) == sideFeatures
                        .get(3)) {
            symmetricRotations = 2;
        }

        return symmetricRotations;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public int getOriginalPlayer() {
        return originalPlayer;
    }

    public void setOriginalPlayer(int originalPlayer) {
        this.originalPlayer = originalPlayer;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Tile getCurrentTile() {
        return currentTile;
    }

    public void setCurrentTile(Tile currentTile) {
        this.currentTile = currentTile;
    }

    public void popNewTile() {
        this.currentTile = availableTiles.pop();
    }

    public Stack<Tile> getAvailableTiles() {
        return availableTiles;
    }

    public void setAvailableTiles(Stack<Tile> availableTiles) {
        this.availableTiles = availableTiles;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(int visitCount) {
        this.visitCount = visitCount;
    }

    public int getFinalScoreDifference() {
        return finalScoreDifference;
    }

    public void setFinalScoreDifference(int finalScoreDifference) {
        this.finalScoreDifference = finalScoreDifference;
    }
}