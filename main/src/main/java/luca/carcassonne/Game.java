package luca.carcassonne;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Stack;

import javax.annotation.processing.Filer;

import java.util.Scanner;

// Starts the game and handles turns and available tiles
public class Game {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";

    private static final Random random = new Random();
    private static final Integer numberOfTiles = 1000; // For now at least more than 100
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
        // this.availableTiles = getRandomTiles(numberOfTiles);
        // this.availableTiles = getSmallDeckOfTiles(numberOfTiles);
        this.availableTiles = Rules.getStandardDeck();
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

            Game game = new Game(new Board(Rules.getStartingTile()));
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

            // System.out.println("White has " + game.players.get(0).getAvailableMeeples() +
            // " meeples left");
            // System.out.println("Red has " + game.players.get(1).getAvailableMeeples() + "
            // meeples left");
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
                        System.out.println("- - Tile not placed");
                        isPlaced = true;
                        playerPlacedTile = false;
                    }
                }
            }

            if (playerPlacedTile) {
                System.out.println("Placing " + currentTile.getId() + " at " + currentTile.getCoordinates() + " (" + currentTile + ")");

                // Take a feature node from the current tile and place a meeple
                boolean meeplePlaced = false;

                Object[] filteredFeature = currentTile.getFeatures().stream().toArray();
                // Object[] filteredFeature = currentTile.getFeatures().stream().filter(f ->
                // f.getClass() == Field.class)
                // .toArray();
                

                Feature randomFeature = (Feature) filteredFeature[random.nextInt(filteredFeature.length)];

                while (randomFeature.getClass() == Field.class && random.nextInt(10) < 7) {
                    randomFeature = (Feature) filteredFeature[random.nextInt(filteredFeature.length)];
                }

                // place meeple with 30% chance
                if (random.nextInt(10) < 3) {
                    meeplePlaced = board.placeMeeple(randomFeature, currentPlayer);
                }
                // meeplePlaced = board.placeMeeple(randomFeature, currentPlayer);

                if (meeplePlaced) {
                    System.out.println(currentPlayer.getColour() + " placed meeple on " + randomFeature.getClass().getSimpleName() + "(" + (randomFeature.getCardinalPoints().isEmpty() ? "null" : randomFeature.getCardinalPoints().get(0)) + ")");
                }

                currentTile.setOwner(currentPlayer); // to delete

                board.scoreClosedFeatures();
                updatePlayerQueue();
            }

            // printProgressBarStep();

        }

        board.scoreOpenFeatures();

        // board.printOpenFeatures();
        board.printClosedFeatures();
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
                    // new Tile(SideFeature.getRandomFeature(),
                    // SideFeature.getRandomFeature(),
                    // SideFeature.getRandomFeature(),
                    // SideFeature.getRandomFeature()));
                    new Tile(SideFeature.FIELD,
                            SideFeature.FIELD,
                            SideFeature.FIELD,
                            SideFeature.FIELD));
        }

        return tiles;
    }

    // Returns a few properly setup tiles
    // private Stack<Tile> getSmallDeckOfTiles(int n) {
    //     Stack<Tile> tiles = new Stack<>();
    //     n = n / 5;

    //     for (int i = 0; i < n; i++) {

    //         // Straight road
    //         tiles.push(new Tile(SideFeature.ROAD, SideFeature.FIELD, SideFeature.ROAD, SideFeature.FIELD,
    //                 new HashSet<>() {
    //                     {
    //                         HashSet<Castle> castles = new HashSet<>();
    //                         add(new Road(new ArrayList<CardinalPoint>() {
    //                             {
    //                                 add(CardinalPoint.N);
    //                                 add(CardinalPoint.S);
    //                             }
    //                         }));
    //                         add(new Field(new ArrayList<CardinalPoint>() {
    //                             {
    //                                 add(CardinalPoint.NNW);
    //                                 add(CardinalPoint.WNW);
    //                                 add(CardinalPoint.W);
    //                                 add(CardinalPoint.WSW);
    //                                 add(CardinalPoint.SSW);
    //                             }
    //                         }, castles));
    //                         add(new Field(new ArrayList<CardinalPoint>() {
    //                             {
    //                                 add(CardinalPoint.NNE);
    //                                 add(CardinalPoint.ENE);
    //                                 add(CardinalPoint.E);
    //                                 add(CardinalPoint.ESE);
    //                                 add(CardinalPoint.SSE);
    //                             }
    //                         }, castles));

    //                     }
    //                 }));

    //         // Curvy road
    //         tiles.push(new Tile(SideFeature.ROAD, SideFeature.ROAD, SideFeature.FIELD, SideFeature.FIELD,
    //                 new HashSet<>() {
    //                     {
    //                         HashSet<Castle> castles = new HashSet<>();
    //                         add(new Road(new ArrayList<CardinalPoint>() {
    //                             {
    //                                 add(CardinalPoint.N);
    //                                 add(CardinalPoint.E);
    //                             }
    //                         }));
    //                         add(new Field(new ArrayList<CardinalPoint>() {
    //                             {
    //                                 add(CardinalPoint.NNW);
    //                                 add(CardinalPoint.WNW);
    //                                 add(CardinalPoint.W);
    //                                 add(CardinalPoint.WSW);
    //                                 add(CardinalPoint.SSW);
    //                                 add(CardinalPoint.S);
    //                                 add(CardinalPoint.SSE);
    //                                 add(CardinalPoint.ESE);
    //                             }
    //                         }, castles));
    //                         add(new Field(new ArrayList<CardinalPoint>() {
    //                             {
    //                                 add(CardinalPoint.NNE);
    //                                 add(CardinalPoint.ENE);
    //                             }
    //                         }, castles));

    //                     }
    //                 }));

    //         // Curvy castle no road
    //         tiles.push(new Tile(SideFeature.CASTLE, SideFeature.CASTLE, SideFeature.FIELD, SideFeature.FIELD,
    //                 new HashSet<>() {
    //                     {
    //                         HashSet<Castle> castles = new HashSet<>() {
    //                             {
    //                                 add(new Castle(new ArrayList<CardinalPoint>() {
    //                                     {
    //                                         add(CardinalPoint.NNW);
    //                                         add(CardinalPoint.N);
    //                                         add(CardinalPoint.NNE);
    //                                         add(CardinalPoint.ENE);
    //                                         add(CardinalPoint.E);
    //                                         add(CardinalPoint.ESE);
    //                                     }
    //                                 }, false));
    //                             }
    //                         };
    //                         add(new Field(new ArrayList<CardinalPoint>() {
    //                             {
    //                                 add(CardinalPoint.WNW);
    //                                 add(CardinalPoint.W);
    //                                 add(CardinalPoint.WSW);
    //                                 add(CardinalPoint.SSW);
    //                                 add(CardinalPoint.S);
    //                                 add(CardinalPoint.SSE);
    //                             }
    //                         }, castles));
    //                         add(castles.iterator().next());

    //                     }
    //                 }));

    //         tiles.push(new Tile(SideFeature.CASTLE, SideFeature.FIELD, SideFeature.FIELD, SideFeature.FIELD,
    //                 new HashSet<>() {
    //                     {
    //                         HashSet<Castle> castles = new HashSet<>() {
    //                             {
    //                                 add(new Castle(new ArrayList<CardinalPoint>() {
    //                                     {
    //                                         add(CardinalPoint.NNE);
    //                                         add(CardinalPoint.N);
    //                                         add(CardinalPoint.NNW);
    //                                     }
    //                                 }, false));
    //                             }
    //                         };
    //                         add(new Field(new ArrayList<CardinalPoint>() {
    //                             {
    //                                 add(CardinalPoint.ENE);
    //                                 add(CardinalPoint.E);
    //                                 add(CardinalPoint.E);
    //                                 add(CardinalPoint.ESE);
    //                                 add(CardinalPoint.SSE);
    //                                 add(CardinalPoint.S);
    //                                 add(CardinalPoint.SSW);
    //                                 add(CardinalPoint.WSW);
    //                                 add(CardinalPoint.W);
    //                                 add(CardinalPoint.WNW);
    //                             }
    //                         }, castles));
    //                         add(castles.iterator().next());
    //                     }
    //                 }));

    //         // Monastery, all field
    //         tiles.push(new Tile(SideFeature.FIELD, SideFeature.FIELD, SideFeature.FIELD, SideFeature.FIELD,
    //                 new HashSet<>() {
    //                     {
    //                         add(new Field(new ArrayList<CardinalPoint>() {
    //                             {
    //                                 add(CardinalPoint.NNW);
    //                                 add(CardinalPoint.N);
    //                                 add(CardinalPoint.NNE);
    //                                 add(CardinalPoint.ENE);
    //                                 add(CardinalPoint.E);
    //                                 add(CardinalPoint.ESE);
    //                                 add(CardinalPoint.SSE);
    //                                 add(CardinalPoint.S);
    //                                 add(CardinalPoint.SSW);
    //                                 add(CardinalPoint.WSW);
    //                                 add(CardinalPoint.W);
    //                                 add(CardinalPoint.WNW);
    //                             }
    //                         }, new HashSet<>()));
    //                         add(new Monastery());
    //                     }
    //                 }));
    //     }

    //     return tiles;
    // }

    // * * * * * * * * * * * *
    // * PRINTING METHODS *
    // * * * * * * * * * * * *

    private Coordinates readCoordinatesFromInput(Scanner scanner) {
        System.out.println("Enter coordinates (x,y):");
        String input = scanner.nextLine();
        String[] coordinates = input.split(",");
        return new Coordinates(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]));
    }

    // Prints all the players' scores
    private void printScores() {
        for (Player player : players) {
            System.out.println(player.colour + " score: " + player.getScore());
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
