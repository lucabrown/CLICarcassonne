package luca.carcassonne;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Stack;

import luca.carcassonne.player.Colour;
import luca.carcassonne.player.Player;
import luca.carcassonne.tile.Coordinates;
import luca.carcassonne.tile.Tile;
import luca.carcassonne.tile.feature.Feature;
import luca.carcassonne.tile.feature.Field;

// Starts the game and handles turns and available tiles
public class Game {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";

    private static final Random random = new Random();
    private static final Integer numberOfPLayers = 2;
    private static int failedTiles = 0;
    private static int triedPlacements = 0;
    private static int progressBarStep = 1;
    private static long startTime;

    private Board board;
    private final ArrayList<Player> players;
    private Stack<Tile> availableTiles;
    private Tile currentTile;
    private int currentPlayer = 0;
    private HashMap<Coordinates, HashSet<Integer>> checkedCombinations;
    private HashSet<Integer> checkedRotations;

    public Game(Board board) {
        this.board = board;
        this.players = new ArrayList<>(numberOfPLayers) {
            {
                add(new Player(Colour.WHITE));
                add(new Player(Colour.RED));
                // add(new Player(Colour.GREEN));
                // add(new Player(Colour.YELLOW));
                // add(new Player(Colour.BLACK));
                // add(new Player(Colour.BLUE));
            }
        };
        this.availableTiles = Settings.getStandardDeck();
        currentTile = new Tile(0, 0);
        Game.failedTiles = 0;
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
            game.play();
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
    public void play() {
        Collections.shuffle(availableTiles);

        startTime = System.currentTimeMillis();
        progressBarStep = availableTiles.size() / 100 + 1;
        triedPlacements = 0;

        // Each loop iteration corresponds to one turn
        while (!availableTiles.empty()) {
            boolean isPlaced = false;
            boolean playerPlacedTile = true;

            checkedCombinations = new HashMap<>();
            checkedRotations = new HashSet<Integer>();

            currentTile = availableTiles.pop();

            /*
             * 
             * 
             * Start MCTS by passing in:
             * - the current board
             * - the current tile
             * - the current player
             * - the available tiles
             * - the players
             * 
             * MCTS = new MCTS(board, currentTile, currentPlayer, availableTiles, players);
             * 
             * Move move = mcts.findNextMove();
             */

            while (!isPlaced) {
                triedPlacements++;

                // Get coordinates from mouse click
                Coordinates randomCoordinates = board.getPossibleCoordinates()
                        .get(random.nextInt(board.getPossibleCoordinates()
                                .size()));

                // Get rotation from mouse click
                int randomRotation = random.nextInt(4);
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

                // Take a feature node from the current tile and place a meeple
                boolean meeplePlaced = false;

                Object[] filteredFeature = currentTile.getFeatures().stream().toArray();

                Feature randomFeature = (Feature) filteredFeature[random.nextInt(filteredFeature.length)];

                while (randomFeature.getClass() == Field.class && random.nextInt(10) < 7) {
                    randomFeature = (Feature) filteredFeature[random.nextInt(filteredFeature.length)];
                }

                // place meeple with 30% chance
                if (random.nextInt(10) < 3) {
                    meeplePlaced = board.placeMeeple(randomFeature, players.get(currentPlayer));
                }

                currentTile.setOwner(players.get(currentPlayer)); // to delete

                Scoring.scoreClosedFeatures(board);
                currentPlayer = (currentPlayer + 1) % players.size();
            }

            // printProgressBarStep();

        }

        Scoring.scoreOpenFeatures(board);

        // board.printOpenFeatures();
        // board.printClosedFeatures();
        board.printBoard();
        printScores();

        printSuccessfulTiles();
        printFailedTiles();
        printTimeElapsed();
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

    // * * * * * * * * * * * *
    // * PRINTING METHODS *
    // * * * * * * * * * * * *

    // Prints all the players' scores
    private void printScores() {
        for (Player player : players) {
            System.out.println(player.getColour() + " score: " + player.getScore());
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
