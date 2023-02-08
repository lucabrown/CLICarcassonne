package luca.carcassonne.MCTS;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

/*
* All the code in this file is from
*
*
https://github.com/eugenp/tutorials/tree/master/algorithms-modules/algorithms-searching/src/main/java/com/baeldung/algorithms/mcts
*
* unless clearly stated otherwise.
*/

import luca.carcassonne.Board;
import luca.carcassonne.player.Player;
import luca.carcassonne.tile.Coordinates;
import luca.carcassonne.tile.Tile;
import luca.carcassonne.tile.feature.Feature;

public class State {
    private Board board;
    private Move move;
    private int currentPlayer;
    private Tile currentTile;
    private Stack<Tile> availableTiles;
    private ArrayList<Player> players;
    private int visitCount;
    private int finalPointDifference;

    public State() {
        board = new Board();
    }

    public State(State state) throws CloneNotSupportedException {
        this.board = new Board(state.getBoard());
        this.move = null;
        this.currentPlayer = 0;
        this.currentTile = null;
        this.availableTiles = null;
        this.players = null;
        this.visitCount = 0;
        this.finalPointDifference = 0;
    }

    public State(Board board) throws CloneNotSupportedException {
        this.board = new Board(board);
    }

    public void initialise(Board startingBoard, int startingPlayer, Tile currentTile,
            ArrayList<Player> players,
            Stack<Tile> availableTiles) {
        this.board = startingBoard;
        this.currentPlayer = startingPlayer;
        this.currentTile = currentTile;
        this.availableTiles = availableTiles;
    }

    @SuppressWarnings("unchecked")
    public ArrayList<State> getAllPossibleChildStates() throws CloneNotSupportedException {
        ArrayList<State> possibleChildStates = new ArrayList<>();
        List<Coordinates> possibleCoordinates = this.board.getPossibleCoordinates();

        for (Coordinates coordinates : possibleCoordinates) {
            for (int i = 0; i < 4; i++) {
                State newState = new State(this);
                Tile newTile = (Tile) currentTile.clone();

                newTile.rotateClockwise(i);

                if (newState.getBoard().placeTile((Coordinates) coordinates.clone(), newTile)) {
                    Stack<Tile> availableTilesClone = (Stack<Tile>) availableTiles.clone();
                    ArrayList<Player> playersClone = (ArrayList<Player>) this.players.clone();

                    newState.setCurrentPlayer((currentPlayer + 1) % players.size());
                    newState.setAvailableTiles(availableTilesClone);
                    newState.setCurrentTile(availableTilesClone.pop());
                    newState.setPlayers(playersClone);

                    ArrayList<Feature> featuresList = newTile.getFeatures().stream()
                            .collect(Collectors.toCollection(ArrayList::new));

                    for (int j = 0; j < featuresList.size(); j++) {
                        State newerState = new State(newState);

                        // if (newerState.getBoard().placeMeeple(newerState.getBoard().get(j),
                        // newerState.getPlayers().get(currentPlayer))) {
                        // }

                    }

                    possibleChildStates.add(newState);

                }
            }
        }

        return possibleChildStates;
    }
    // @SuppressWarnings("unchecked")
    // public ArrayList<State> getAllPossibleChildStates() throws
    // CloneNotSupportedException {
    // ArrayList<State> possibleChildStates = new ArrayList<>();
    // List<Coordinates> possibleCoordinates = this.board.getPossibleCoordinates();

    // for (Coordinates coordinates : possibleCoordinates) {
    // for (int i = 0; i < 4; i++) {
    // for (Feature feature : currentTile.getFeatures()) {
    // State newState = new State(this);
    // Tile newTile = (Tile) currentTile.clone();

    // newTile.rotateClockwise(i);
    // if (newState.getBoard().placeTile((Coordinates) coordinates.clone(), (Tile)
    // newTile.clone())) {
    // boolean meeplePlaced = newState.getBoard().placeMeeple(feature,
    // players.get(currentPlayer));

    // Stack<Tile> availableTilesClone = (Stack<Tile>) availableTiles.clone();
    // ArrayList<Player> playersClone = (ArrayList<Player>) this.players.clone();

    // newState.setCurrentPlayer((currentPlayer + 1) % players.size());
    // newState.setAvailableTiles(availableTilesClone);
    // newState.setCurrentTile(availableTilesClone.pop());
    // newState.setPlayers(playersClone);

    // if (meeplePlaced) {

    // } else {

    // }
    // possibleChildStates.add(newState);

    // }
    // }
    // }
    // }

    // return possibleChildStates;
    // }

    void incrementVisit() {
        this.visitCount++;
    }

    void randomPlay() {
        List<Coordinates> availablePositions = this.board.getPossibleCoordinates();
        int totalPossibilities = availablePositions.size();
        int selectRandom = (int) (Math.random() * totalPossibilities);
        // this.board.performMove(this.playerNo, availablePositions.get(selectRandom));
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

    double getFinalPointDifference() {
        return finalPointDifference;
    }

    void setFinalPointDifference(int finalPointDifference) {
        this.finalPointDifference = finalPointDifference;
    }
}