package luca.carcassonne;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;
import java.util.concurrent.ThreadPoolExecutor;

import org.javatuples.Pair;

import luca.carcassonne.mcts.Move;
import luca.carcassonne.player.Colour;
import luca.carcassonne.player.GreedyAgent;
import luca.carcassonne.player.MonteCarloAgent;
import luca.carcassonne.player.Player;
import luca.carcassonne.player.ProgressiveHistoryAgent;
import luca.carcassonne.player.RandomAgent;
import luca.carcassonne.tile.Coordinates;
import luca.carcassonne.tile.Tile;

/**
 * The main class of the game. It is responsible for managing the board, the
 * players, the tiles and the main game loop.
 * 
 * It extends {@code Thread} so that multiple games can be run in parallel
 * through the
 * {@code ThreadManager} class.
 * 
 * @author Luca Brown
 */
public class Game extends Thread {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";

    private int failedTiles = 0;
    private int triedPlacements = 0;
    private int progressBarStep = 1;
    private long startTime;

    private Board board;
    private final ArrayList<Player> players;
    private Stack<Tile> availableTiles;
    private Tile currentTile;
    private int currentPlayer = 0;
    private HashMap<Coordinates, HashSet<Integer>> checkedCombinations;
    private HashSet<Integer> checkedRotations;

    /**
     * Creates a new game with the given board.
     * 
     * The players are added manually to the {@code players} list.
     * 
     * @param board
     */
    public Game(Board board) {
        HashMap<Pair<String, Integer>, Integer> totalActionMap = readFromData("totalMoves.csv");
        HashMap<Pair<String, Integer>, Integer> winningActionMap = readFromData("winningMoves.csv");

        System.out.println("Total actions read: " + totalActionMap.size());
        System.out.println("Winning actions read: " + winningActionMap.size());

        this.board = board;
        this.players = new ArrayList<>() {
            {
                // Uncomment any of these agents to let them play.

                // add(new RandomAgent(Colour.GREEN));
                // add(new GreedyAgent(Colour.WHITE));
                // add(new MonteCarloAgent(Colour.BLACK, 500, 0.5));
                // add(new ProgressiveHistoryAgent(Colour.BLUE, 500, 0.5, totalActionMap,
                // winningActionMap));
                // add(new GreedyAgent(Colour.RED));
                add(new RandomAgent(Colour.BLACK));
                add(new RandomAgent(Colour.BLUE));
            }
        };
        this.availableTiles = Settings.getStandardDeck();
        currentTile = new Tile(0, 0);
    }

    /**
     * Runs the game.
     * 
     * It is recommended not to start a game directly, but to instead use the
     * {@code ThreadManager} class to run multiple games in parallel.
     *
     * @param args
     */
    public static void main(String[] args) {
        float times = 1;
        float whiteTotalScore = 0;
        float redTotalScore = 0;
        float whiteWR = 0;
        float redWR = 0;
        float ties = 0;

        for (int i = 0; i < times; i++) {
            System.out.print(i);

            Game game = new Game(new Board(Settings.getSingleCastleWithStraightRoad()));
            game.run();
            whiteTotalScore += game.players.get(0).getScore();
            redTotalScore += game.players.get(1).getScore();
            if (game.players.get(0).getScore() > game.players.get(1).getScore()) {
                whiteWR++;
            } else if (game.players.get(0).getScore() == game.players.get(1).getScore()) {
                ties++;
            } else {
                redWR++;
            }
        }

        whiteWR = (whiteWR / times * 100);
        redWR = (redWR / times * 100);
        ties = (ties / times * 100);
        System.out.println("\nAfter " + times + " games:");

        System.out.println("White won " + whiteWR + "% of games with an average score of " + whiteTotalScore / times);
        System.out.println("Red won " + redWR + "% of games with an average score of " + redTotalScore / times);
        System.out.println("Ties: " + ties + "%");
    }

    /**
     * The main game loop.
     * 
     * Until there are no more tiles available, it will loop through each player,
     * calling their {@code getNextMove()} method.
     * 
     * One a {@code Move} is received it is played by updating the board and
     * keeping track of player score.
     * 
     * At the end of the game, the information is stored in the
     * {@code ThreadManager} class.
     */
    @Override
    public void run() {
        Collections.shuffle(availableTiles, Settings.getRandom());
        startTime = System.currentTimeMillis();
        progressBarStep = availableTiles.size() / 100 + 1;
        triedPlacements = 0;

        // Each loop iteration corresponds to one turn
        while (!availableTiles.empty()) {
            long timeForMove = System.currentTimeMillis();
            System.out.println(players.get(currentPlayer).getColour() + "'s turn");
            System.out.println("Available tiles: " + availableTiles.size());
            System.out.println("Performing " + players.get(currentPlayer).getClass().getSimpleName() + " move");

            boolean isPlaced = false;
            boolean playerPlacedTile = true;
            boolean meeplePlaced = false;
            Coordinates randomCoordinates = null;
            int randomRotation = -1;

            checkedCombinations = new HashMap<>();
            checkedRotations = new HashSet<Integer>();

            currentTile = availableTiles.pop();
            System.out.println("Current tile: " + currentTile.getId());

            Move move = players.get(currentPlayer).getNextMove(board, currentPlayer, currentTile, players,
                    availableTiles);

            if (move == null) {
                System.out.println("- - Tile not placed");
                isPlaced = true;
                playerPlacedTile = false;
                continue;
            }

            while (!isPlaced) {
                triedPlacements++;

                // Get coordinates from mouse click
                randomCoordinates = move.getCoordinates();

                // Get rotation from mouse click
                randomRotation = move.getRotation();
                currentTile.rotateClockwise(randomRotation);

                // Check if the tile can go in the given coordinates with the given rotation
                isPlaced = board.placeTile(randomCoordinates, currentTile);

                if (!isPlaced) {
                    // If tile is not placed, keep a record of checked combination
                    if (updateCheckedCombinations(randomCoordinates, randomRotation)) {
                        System.out.println("- - Tile not placed");
                        isPlaced = true;
                        playerPlacedTile = false;
                    }
                }
            }

            if (playerPlacedTile) {

                if (move.getFeatureIndex() != -1) {
                    meeplePlaced = board.placeMeeple(currentTile.getFeatures().get(move.getFeatureIndex()),
                            players.get(currentPlayer));
                } else {
                    meeplePlaced = false;
                }

                currentTile.setOwner(players.get(currentPlayer)); // to delete

                if (meeplePlaced) {
                    Move newMove = new Move(randomCoordinates, currentTile.getId(), randomRotation, currentPlayer,
                            currentTile.getFeatures().indexOf(currentTile.getFeatures().get(move.getFeatureIndex())));
                    board.addNewMove(newMove);
                } else {
                    Move newMove = new Move(randomCoordinates, currentTile.getId(), randomRotation, currentPlayer);
                    board.addNewMove(newMove);
                }

                ScoreManager.scoreClosedFeatures(board, true);
                currentPlayer = (currentPlayer + 1) % players.size();

                timeForMove = System.currentTimeMillis() - timeForMove;
                System.out.println("Move: " + move + " performed in " + timeForMove + "ms");

                synchronized (ThreadManager.timeForMove) {
                    int index = 71 - availableTiles.size() - 1;
                    ThreadManager.timeForMove.set(index,
                            ThreadManager.timeForMove.get(index) + (int) timeForMove);
                }

                // print score of each player
                printScores();
            }
        }
        System.out.println("Scoring open features");
        ScoreManager.scoreOpenFeatures(board, true);

        board.printBoard();
        printScores();

        printSuccessfulTiles();
        printFailedTiles();
        printTimeElapsed();

        // Obtain a lock on the synchronized list
        synchronized (ThreadManager.playersTotalScore) {
            for (int i = 0; i < players.size(); i++) {
                // Update the player's score in the synchronized list
                ThreadManager.playersTotalScore.set(i,
                        ThreadManager.playersTotalScore.get(i) + players.get(i).getScore());
            }
        }

        int maxValue = players.stream().max(Comparator.comparing(Player::getScore)).get().getScore();
        ArrayList<Integer> maxPlayers = new ArrayList<Integer>();
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getScore() == maxValue) {
                maxPlayers.add(i);
            }
        }

        synchronized (ThreadManager.ties) {
            if (maxPlayers.size() > 1) {
                for (Integer i : maxPlayers) {
                    ThreadManager.ties.set(i, ThreadManager.ties.get(i) + 1);
                }
            } else {
                synchronized (ThreadManager.playersWR) {
                    for (Integer i : maxPlayers) {
                        ThreadManager.playersWR.set(i, ThreadManager.playersWR.get(i) + 1);
                    }
                }
            }
        }

        synchronized (ThreadManager.playersMaxScore) {
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).getScore() > ThreadManager.playersMaxScore.get(i)) {
                    ThreadManager.playersMaxScore.set(i, players.get(i).getScore());
                }
            }
        }

        synchronized (ThreadManager.playersMinScore) {
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).getScore() < ThreadManager.playersMinScore.get(i)) {
                    ThreadManager.playersMinScore.set(i, players.get(i).getScore());
                }
            }
        }

        synchronized (ThreadManager.scoreDelta) {
            ThreadManager.scoreDelta.add(players.get(0).getScore() - players.get(1).getScore());
        }

        int remainingTasks = ((ThreadPoolExecutor) ThreadManager.pool).getQueue().size();
        System.out.println("\n- - - - Remaining tasks: " + remainingTasks + "\n");

        this.interrupt();
    }

    /**
     * Keeps a record of checked combinations for a tile.
     * 
     * If there are no more possible placements for a tile it is remove from the
     * game and a new one is drawn.
     * 
     * @param randomCoordinates
     * @param randomRotation
     * @return
     */
    private boolean updateCheckedCombinations(Coordinates randomCoordinates, Integer randomRotation) {
        if (!checkedCombinations.containsKey(randomCoordinates)) {
            checkedCombinations.put(randomCoordinates, new HashSet<>());
        }

        checkedRotations = checkedCombinations.get(randomCoordinates);
        checkedRotations.add(randomRotation);
        checkedCombinations.put(randomCoordinates, checkedRotations);

        if (checkedCombinations.size() == board.getPossibleCoordinates().size()
                && checkedCombinations.get(randomCoordinates).size() == 4) {
            failedTiles++;
            return true;
        }

        return false;
    }

    /**
     * Reads data from a csv file.
     * 
     * Used for the {@code ProgressiveHistoryAgent}.
     * 
     * @param fileName
     * @return
     */
    public HashMap<Pair<String, Integer>, Integer> readFromData(String fileName) {
        HashMap<Pair<String, Integer>, Integer> actionMap = new HashMap<Pair<String, Integer>, Integer>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));

            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Pair<String, Integer> action = new Pair<String, Integer>(values[0],
                        Integer.parseInt(values[1]));
                if (actionMap.containsKey(action)) {
                    actionMap.put(action, actionMap.get(action) + 1);
                } else {
                    actionMap.put(action, 1);
                }
            }

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return actionMap;
    }

    public int getNumberOfPlayers() {
        return players.size();
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    // * * * * * * * * * * * *
    // * PRINTING METHODS *
    // * * * * * * * * * * * *

    /**
     * Prints the scores of all players.
     */
    private void printScores() {
        for (Player player : players) {
            System.out.println(player.getColour() + " score: " + player.getScore() + " (" + player.getAvailableMeeples()
                    + " meeples left)");
        }
    }

    /**
     * Prints the moves of the given player.
     * 
     * @param playerIndex
     */
    @SuppressWarnings("unused")
    private void printMoves(int playerIndex) {
        for (Move move : board.getPastMoves()) {
            if (move.getPlayerIndex() == playerIndex) {
                System.out.println(move.getTileId() + " " + move.getFeatureIndex());
            }
        }
    }

    /**
     * Prints how many tiles were placed successfully.
     */
    private void printSuccessfulTiles() {
        System.out.println(
                "\nTried to place " + ANSI_GREEN + (board.getPlacedTilesSize() - 1) + ANSI_RESET + " tiles "
                        + ANSI_GREEN + triedPlacements
                        + ANSI_RESET + " times.");
    }

    /**
     * Prints how many tiles were not placed successfully.
     */
    private void printFailedTiles() {
        if (failedTiles == 1) {
            System.out.println("Failed to place " + ANSI_RED + failedTiles + ANSI_RESET + " tile.");

        } else {
            System.out.println("Failed to place " + ANSI_RED + failedTiles + ANSI_RESET + " tiles.");

        }
    }

    /**
     * Prints the time elapsed since the start of the game.
     */
    private void printTimeElapsed() {
        long finishTime = System.currentTimeMillis();
        long timeElapsed = (finishTime - startTime);
        System.out.println("Time taken: " + ANSI_GREEN + timeElapsed / 1000.0 + ANSI_RESET + "s");
    }

    /**
     * Prints 1% of the progress bar.
     */
    @SuppressWarnings("unused")
    private void printProgressBarStep() {
        if (board.getPlacedTilesSize() == 2) {
            System.out.print("\n%: ");
        }
        if (board.getPlacedTilesSize() % progressBarStep == 0) {
            System.out.print("#");
        }
    }
}
