package luca.carcassonne;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Stack;

// Starts the game and handles turns and available tiles
public class Game {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";

    private static final Random random = new Random();
    private static final Integer numberOfTiles = 10000; // For now at least more than 100
    private static final Integer numberOfPLayers = 2;
    private static int failedTiles = 0;
    private static int triedPlacements = 0;
    private static int progressBarStep = 1;
    private static long startTime;

    private Board board;
    private final ArrayList<Player> players;
    private Stack<Tile> availableTiles;
    private Tile currentTile;
    private Player currentPlayer;
    private HashSet<Feature> features;
    private HashMap<Coordinates, HashSet<Integer>> checkedCombinations;
    private HashSet<Integer> checkedRotations;

    public Game(Board board) {
        this.board = board;
        this.players = new ArrayList<>(numberOfPLayers) {
            {
                add(new Player(Colour.WHITE));
                // add(new Player(Colour.RED));
                // add(new Player(Colour.GREEN));
                // add(new Player(Colour.YELLOW));
                // add(new Player(Colour.BLACK));
                // add(new Player(Colour.BLUE));
            }
        };
        this.availableTiles = getRandomTiles(numberOfTiles);
        currentTile = new Tile(0, 0);
        this.features = new HashSet<>() {
            {
                currentTile.getFeatures();
            }
        };
        Game.failedTiles = 0;
    }

    public static void main(String[] args) {
        Game game = new Game(new Board());
        game.play();
    }

    // The main game loop
    public void play() {
        startTime = System.currentTimeMillis();
        progressBarStep = availableTiles.size() / 100;
        triedPlacements = 0;

        // Each loop iteration corresponds to one turn
        while (!availableTiles.empty()) {
            boolean isPlaced = false;
            boolean playerPlacedTile = true;

            checkedCombinations = new HashMap<>();
            checkedRotations = new HashSet<Integer>();

            currentPlayer = players.get(0);
            currentTile = availableTiles.pop();

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
                        isPlaced = true;
                        playerPlacedTile = false;
                    }
                }
            }

            if (playerPlacedTile) {
                currentTile.setOwner(currentPlayer);
                updatePlayerQueue();
            }

            printProgressBarStep();

        }

        board.printBoard();
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

    // Moves the current player to the back of the queue
    private void updatePlayerQueue() {
        players.remove(0);
        players.add(currentPlayer);
    }

    // Returns n randomly generated tiles
    private Stack<Tile> getRandomTiles(int n) {
        Stack<Tile> tiles = new Stack<>();

        for (int i = 0; i < n; i++) {
            tiles.push(
                    new Tile(SideFeature.getRandomFeature(),
                            SideFeature.getRandomFeature(),
                            SideFeature.getRandomFeature(),
                            SideFeature.getRandomFeature()));
            // new Tile(SideFeature.ROAD,
            // SideFeature.ROAD,
            // SideFeature.ROAD,
            // SideFeature.ROAD));
        }

        return tiles;
    }

    // * * * * * * *
    // * PRINTING METHODS *
    // * * * * * * *

    // Prints how many tiles were placed successfully
    private void printSuccessfulTiles() {
        System.out.println(
                "\nTried to place " + ANSI_GREEN + numberOfTiles + ANSI_RESET + " tiles " + ANSI_GREEN + triedPlacements
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
