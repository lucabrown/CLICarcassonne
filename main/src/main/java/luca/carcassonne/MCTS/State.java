package luca.carcassonne.MCTS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.stream.Collectors;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

/*
* All the code in this file is from
*
*
https://github.com/eugenp/tutorials/tree/master/algorithms-modules/algorithms-searching/src/main/java/com/baeldung/algorithms/mcts
*
* unless clearly stated otherwise.
*/

import luca.carcassonne.Board;
import luca.carcassonne.ScoreManager;
import luca.carcassonne.Settings;
import luca.carcassonne.player.Player;
import luca.carcassonne.tile.Coordinates;
import luca.carcassonne.tile.SideFeature;
import luca.carcassonne.tile.Tile;
import luca.carcassonne.tile.feature.Feature;
import luca.carcassonne.tile.feature.Field;

public class State implements Cloneable {
    private Board board;
    private Move move;
    private int currentPlayer;
    private Tile currentTile;
    private Stack<Tile> availableTiles;
    private ArrayList<Player> players;
    private int visitCount;
    private double finalScoreDifference;

    public State() {
        board = new Board();
    }

    public void initialise(Board startingBoard, int startingPlayer, Tile currentTile,
            ArrayList<Player> players,
            Stack<Tile> availableTiles) {
        this.board = startingBoard;
        this.currentPlayer = startingPlayer;
        this.currentTile = currentTile;
        this.players = players;
        this.availableTiles = availableTiles;
    }

    @SuppressWarnings("unchecked")
    public ArrayList<State> getAllPossibleChildStates() {
        ArrayList<State> possibleChildStates = new ArrayList<>();
        List<Coordinates> possibleCoordinates = this.board.getPossibleCoordinates();

        board.printBoard();

        System.out.println("Parent tile: " + currentTile.getId());
        for (Coordinates coordinates : possibleCoordinates) {
            for (int i = getSymmetricRotations(currentTile) - 1; i < 4; i++) {
                for (int j = 0; j < currentTile.getFeatures().size() + 1; j++) {
                    State newState = (State) this.clone();
                    Tile newTile = (Tile) currentTile.clone();
                    ArrayList<Feature> featuresList = newTile.getFeatures().stream()
                            .collect(Collectors.toCollection(ArrayList::new));

                    newTile.rotateClockwise(i);
                    Coordinates newCoordinates = (Coordinates) coordinates.clone();

                    if (newState.getBoard().placeTile(newCoordinates, newTile)) {
                        Stack<Tile> availableTilesClone = (Stack<Tile>) availableTiles.stream()
                                .collect(Collectors.toCollection(Stack::new));
                        ArrayList<Player> playersClone = (ArrayList<Player>) this.players.clone();

                        newState.setCurrentPlayer((currentPlayer + 1) % players.size());
                        newState.setAvailableTiles(availableTilesClone);
                        newState.setCurrentTile(availableTilesClone.pop());
                        newState.setPlayers(playersClone);

                        if (j < currentTile.getFeatures().size()) {
                            newState.getBoard().placeMeeple(featuresList.get(j), playersClone.get(currentPlayer));
                            newState.setMove(new Move(newCoordinates, newTile, i, featuresList.get(j)));
                        } else {
                            newState.setMove(new Move(newCoordinates, newTile, i));

                        }

                        possibleChildStates.add(newState);
                    }
                }
            }
        }

        return possibleChildStates;
    }

    void incrementVisit() {
        visitCount++;
    }

    void randomPlay() {
        State newState = (State) this.clone();
        Board newBoard = newState.getBoard();
        Tile newCurrentTile = newState.getCurrentTile();
        Stack<Tile> newAvailableTiles = newState.getAvailableTiles();
        ArrayList<Player> newPlayers = newState.getPlayers();
        int newCurrentPlayer = currentPlayer;

        Random random = new Random();
        HashMap<Coordinates, HashSet<Integer>> checkedCombinations = new HashMap<>();
        HashSet<Integer> checkedRotations = new HashSet<Integer>();

        int i = 0;
        while (!newAvailableTiles.empty()) {
            i++;
            boolean isPlaced = false;
            boolean playerPlacedTile = true;

            newCurrentTile = newAvailableTiles.pop();
            while (!isPlaced) {

                Coordinates randomCoordinates = newBoard.getPossibleCoordinates()
                        .get(random.nextInt(newBoard.getPossibleCoordinates()
                                .size()));

                int randomRotation = random.nextInt(4);
                newCurrentTile.rotateClockwise(randomRotation);

                // Check if the tile can go in the given coordinates with the given rotation
                isPlaced = newBoard.placeTile(randomCoordinates, newCurrentTile);

                if (!isPlaced) {
                    // If tile is not placed, keep a record of checked combination
                    if (updateCheckedCombinations(checkedCombinations, checkedRotations, randomCoordinates,
                            randomRotation)) {
                        System.out.println("- - Tile not placed");
                        isPlaced = true;
                        playerPlacedTile = false;
                    }
                }
            }

            if (playerPlacedTile) {
                Object[] filteredFeature = newCurrentTile.getFeatures().stream().toArray();

                Feature randomFeature = (Feature) filteredFeature[random.nextInt(filteredFeature.length)];

                while (randomFeature.getClass() == Field.class && random.nextInt(10) < 7) {
                    randomFeature = (Feature) filteredFeature[random.nextInt(filteredFeature.length)];
                }

                // place meeple with 30% chance
                if (random.nextInt(10) < 3) {
                    board.placeMeeple(randomFeature, newPlayers.get(newCurrentPlayer));
                }

                currentTile.setOwner(newPlayers.get(newCurrentPlayer)); // to delete

                ScoreManager.scoreClosedFeatures(newBoard);
                newCurrentPlayer = (newCurrentPlayer + 1) % newPlayers.size();
            }

        }

        ScoreManager.scoreOpenFeatures(newBoard);

        finalScoreDifference = calculateScoreDifference(newPlayers, currentPlayer);

        System.out.println("Playout iterations: " + i);
    }

    private boolean updateCheckedCombinations(HashMap<Coordinates, HashSet<Integer>> checkedCombinations,
            HashSet<Integer> checkedRotations,
            Coordinates randomCoordinates, Integer randomRotation) {
        if (!checkedCombinations.containsKey(randomCoordinates)) {
            checkedCombinations.put(randomCoordinates, new HashSet<>());
        }

        checkedRotations = checkedCombinations.get(randomCoordinates);
        checkedRotations.add(randomRotation);
        checkedCombinations.put(randomCoordinates, checkedRotations);

        if (checkedCombinations.size() == board.getPossibleCoordinates().size()
                && checkedCombinations.get(randomCoordinates).size() == 4) {
            return true;
        }

        return false;
    }

    private double calculateScoreDifference(ArrayList<Player> players, int currentPlayer) {
        double pointDifference = 0;

        for (int i = 0; i < players.size(); i++) {
            if (i != currentPlayer) {
                pointDifference += players.get(i).getScore();
            } else {
                pointDifference -= players.get(i).getScore();
            }
        }

        return pointDifference;
    }

    private int getSymmetricRotations(Tile tile) {
        int symmetricRotations = 1;
        ArrayList<SideFeature> sideFeatures = tile.getSideFeatures();

        if (sideFeatures.get(0).getClass() == sideFeatures.get(2).getClass()
                && sideFeatures.get(1).getClass() == sideFeatures
                        .get(3).getClass()
                && sideFeatures.get(0).getClass() == sideFeatures.get(3).getClass()) {
            symmetricRotations = 4;
        } else if (sideFeatures.get(0).getClass() == sideFeatures.get(2).getClass()
                && sideFeatures.get(1).getClass() == sideFeatures
                        .get(3).getClass()) {
            symmetricRotations = 2;
        }

        return symmetricRotations;
    }

    Board getBoard() {
        return board;
    }

    void setBoard(Board board) {
        this.board = board;
    }

    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        this.move = move;
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

    double getFinalScoreDifference() {
        return finalScoreDifference;
    }

    void setFinalScoreDifference(int finalScoreDifference) {
        this.finalScoreDifference = finalScoreDifference;
    }

    @Override
    public Object clone() {
        State newState = new State();

        newState.setBoard((Board) board.clone());
        newState.setMove(move == null ? null : (Move) move.clone());
        newState.setCurrentPlayer(currentPlayer);
        newState.setCurrentTile(currentTile == null ? null : (Tile) currentTile.clone());
        newState.setAvailableTiles(
                (Stack<Tile>) availableTiles.stream().map(t -> (Tile) t.clone())
                        .collect(Collectors.toCollection(Stack::new)));
        newState.setPlayers((ArrayList<Player>) players.stream().map(p -> (Player) p.clone())
                .collect(Collectors.toCollection(ArrayList::new)));
        newState.setVisitCount(visitCount);
        newState.setFinalScoreDifference((int) finalScoreDifference);

        for (SimpleGraph<Feature, DefaultEdge> featureGraph : newState.getBoard().getOpenFeatures()) {
            for (Feature feature : featureGraph.vertexSet()) {
                if (feature.getOwner() == null) {
                    continue;
                }
                int playerIndex = players.indexOf(feature.getOwner());
                feature.setOwner(newState.getPlayers().get(playerIndex));
            }
        }

        return newState;
    }
}