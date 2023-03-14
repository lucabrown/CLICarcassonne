package luca.carcassonne;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Stack;

import org.javatuples.Pair;

import luca.carcassonne.mcts.Move;
import luca.carcassonne.player.Colour;
import luca.carcassonne.player.MonteCarloAgent;
import luca.carcassonne.player.Player;
import luca.carcassonne.player.ProgressiveHistoryAgent;
import luca.carcassonne.player.RandomAgent;
import luca.carcassonne.tile.Coordinates;
import luca.carcassonne.tile.Tile;
import luca.carcassonne.tile.feature.Feature;

// Starts the game and handles turns and available tiles
public class Game extends Thread {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";

    public static final float totalChildren[] = new float[72];

    private static final Random random = new Random();
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

    public Game(Board board) {
        HashMap<Pair<String, Integer>, Integer> totalActionMap = readFromData("totalMoves.csv");
        HashMap<Pair<String, Integer>, Integer> winningActionMap = readFromData("winningMoves.csv");

        System.out.println("Total actions read: " + totalActionMap.size());
        System.out.println("Winning actions read: " + winningActionMap.size());

        this.board = board;
        this.players = new ArrayList<>() {
            {
                add(new ProgressiveHistoryAgent(Colour.WHITE, 500, 0.5, totalActionMap, winningActionMap));
                // add(new MonteCarloAgent(Colour.WHITE, 1000, 0.5));
                add(new MonteCarloAgent(Colour.RED, 500, 0.5));
                // add(new MonteCarloAgent(Colour.RED, 100));
                // add(new RandomAgent(Colour.WHITE));
                // add(new RandomAgent(Colour.RED));
                // add(new Player(Colour.YELLOW, Behaviour.MCTS));
                // add(new Player(Colour.BLACK));
                // add(new Player(Colour.BLUE));
            }
        };
        this.availableTiles = Settings.getStandardDeck();
        currentTile = new Tile(0, 0);
    }

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

    // The main game loop
    @Override
    public void run() {
        Collections.shuffle(availableTiles);
        startTime = System.currentTimeMillis();
        progressBarStep = availableTiles.size() / 100 + 1;
        triedPlacements = 0;

        // Each loop iteration corresponds to one turn
        while (!availableTiles.empty()) {
            long timeForMove = System.currentTimeMillis();
            System.out.println(players.get(currentPlayer).getColour() + "'s turn");
            System.out.println("Available tiles: " + availableTiles.size());
            System.out.println("Performing " + players.get(currentPlayer).getClass() + " move");

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

            // totalChildren[moveNumber] = totalChildren[moveNumber]
            // + mcts.getStartingState().getAllPossibleChildStates().size();

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

                Feature feature = null;
                if (move.getFeatureIndex() != -1) {
                    feature = currentTile.getFeatures().get(move.getFeatureIndex());

                    meeplePlaced = board.placeMeeple(currentTile.getFeatures().get(move.getFeatureIndex()),
                            players.get(currentPlayer));
                } else {
                    meeplePlaced = false;
                }

                currentTile.setOwner(players.get(currentPlayer)); // to delete

                if (meeplePlaced) {
                    Move newMove = new Move(randomCoordinates, currentTile, randomRotation, currentPlayer,
                            currentTile.getFeatures().indexOf(currentTile.getFeatures().get(move.getFeatureIndex())));
                    board.addNewMove(newMove);
                } else {
                    Move newMove = new Move(randomCoordinates, currentTile, randomRotation, currentPlayer);
                    board.addNewMove(newMove);
                }

                ScoreManager.scoreClosedFeatures(board);
                currentPlayer = (currentPlayer + 1) % players.size();

                timeForMove = System.currentTimeMillis() - timeForMove;
                System.out.println("Move " + move + " performed in " + timeForMove + "ms");
                System.out.println("Enter for next turn.");
            }
        }

        ScoreManager.scoreOpenFeatures(board);

        board.printBoard();
        printScores();

        printSuccessfulTiles();
        printFailedTiles();
        printTimeElapsed();

        int winningPlayer = players.get(0).getScore() > players.get(1).getScore() ? 0 : 1;

        try {
            FileWriter totalMovesWriter = new FileWriter("totalMoves.csv", true);
            FileWriter winningMovesWriter = new FileWriter("winningMoves.csv", true);

            for (Move move : board.getPastMoves()) {
                if (move.getPlayerIndex() == 0) {
                    String data = move.getTile().getId() + "," + move.getFeatureIndex() + "\n";
                    totalMovesWriter.write(data);
                    if (move.getPlayerIndex() == winningPlayer) {
                        winningMovesWriter.write(data);
                    }

                }
            }

            totalMovesWriter.close();
            winningMovesWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ThreadManager.whiteTotalScore.addAndGet(players.get(0).getScore());
        ThreadManager.redTotalScore.addAndGet(players.get(1).getScore());

        if (players.get(0).getScore() > players.get(1).getScore()) {
            ThreadManager.whiteWR.incrementAndGet();
            printMoves(0);
        } else if (players.get(0).getScore() == players.get(1).getScore()) {
            ThreadManager.ties.incrementAndGet();
        } else {
            ThreadManager.redWR.incrementAndGet();
            printMoves(1);
        }

        this.interrupt();
    }

    // Keeps a record of checked combinations for one tile
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

        // for (Pair<String, Integer> action : actionMap.keySet()) {
        // System.out.println(action + " " + actionMap.get(action));
        // }

        return actionMap;
    }

    // * * * * * * * * * * * *
    // * PRINTING METHODS *
    // * * * * * * * * * * * *

    // Prints all the players' scores
    private void printScores() {
        for (Player player : players) {
            System.out.println(player.getColour() + " score: " + player.getScore());
        }
    }

    private void printMoves(int winner) {
        for (Move move : board.getPastMoves()) {
            if (move.getPlayerIndex() == winner) {
                System.out.println(move.getTile().getId() + " " + move.getFeatureIndex());
            }
        }
    }

    // Prints how many tiles were placed successfully
    private void printSuccessfulTiles() {
        System.out.println(
                "\nTried to place " + ANSI_GREEN + (board.getPlacedTilesSize() - 1) + ANSI_RESET + " tiles "
                        + ANSI_GREEN + triedPlacements
                        + ANSI_RESET + " times.");
    }

    // Prints how many tiles were not placed successfully
    private void printFailedTiles() {
        if (failedTiles == 1) {
            System.out.println("Failed to place " + ANSI_RED + failedTiles + ANSI_RESET + " tile.");

        } else {
            System.out.println("Failed to place " + ANSI_RED + failedTiles + ANSI_RESET + " tiles.");

        }
    }

    // Prints how much time it took to place all the tiles
    private void printTimeElapsed() {
        long finishTime = System.currentTimeMillis();
        long timeElapsed = (finishTime - startTime);
        System.out.println("Time taken: " + ANSI_GREEN + timeElapsed / 1000.0 + ANSI_RESET + "s");
    }

    // Prints 1% of the progress bar
    private void printProgressBarStep() {
        if (board.getPlacedTilesSize() == 2) {
            System.out.print("\n%: ");
        }
        if (board.getPlacedTilesSize() % progressBarStep == 0) {
            System.out.print("#");
        }
    }
}
