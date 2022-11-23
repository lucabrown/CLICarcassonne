package luca.carcassonne;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Holds all played tiles and tracks the relation between them
public class Board {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RED = "\u001B[31m";

    private Integer height;
    private Integer width;
    private Integer maxY;
    private Integer maxX;
    private Integer minY;
    private Integer minX;
    private Tile startingTile;
    private ArrayList<Tile> placedTiles;
    private ArrayList<Coordinates> possibleCoordinates;

    public Board() {
        this.height = 1;
        this.width = 1;
        this.startingTile = new Tile(0, 0);
        this.maxY = startingTile.getCoordinates().getY();
        this.maxX = startingTile.getCoordinates().getX();
        this.minY = maxY;
        this.minX = maxX;
        placedTiles = new ArrayList<>() {
            {
                add(startingTile);
            }
        };
        possibleCoordinates = new ArrayList<>() {
            {
                startingTile.getAdjacentCoordinates().forEach(coordinate -> {
                    add(coordinate);
                });
            }
        };
    }

    public Tile getStartingTile() {
        return startingTile;
    }

    // Tries to place a tile in the given coordinates. Returns true if the placement
    // was legal.
    public boolean placeTile(Coordinates coordinates, Tile tile) {
        if (tilePlacementLegal(coordinates, tile)) {
            tile.setCoordinates(coordinates);

            updateBoard(tile);

            return true;
        }

        return false;

    }

    // Updates the board's state with the new tile
    private void updateBoard(Tile tile) {
        placedTiles.add(tile);
        possibleCoordinates.remove(tile.getCoordinates());

        // For each new adjacent coordinate, add it to the list of possible coordinates
        tile.getAdjacentCoordinates().forEach(coordinates -> {
            if (!possibleCoordinates.contains(coordinates)
                    && !placedTiles.stream().anyMatch(tile1 -> tile1.getCoordinates().equals(coordinates))) {
                possibleCoordinates.add(coordinates);
            }
        });

        updateBoardStringParameters(tile);
    }

    // Checks if the tile's placement is legal
    private boolean tilePlacementLegal(Coordinates coordinates, Tile tile) {
        if (possibleCoordinates.contains(coordinates)) {
            List<Tile> tilesToCheck = placedTiles.stream()
                    .filter(e -> e.getAdjacentCoordinates().contains(coordinates))
                    .collect(Collectors.toList());

            for (Tile t : tilesToCheck) {
                int position = getRelativePosition(t, coordinates);

                switch (position) {
                    case 0:
                        if (tile.getNorthSideFeature() != t.getSouthSideFeature()) {
                            return false;
                        }
                        break;
                    case 1:
                        if (tile.getEastSideFeature() != t.getWestSideFeature()) {
                            return false;
                        }
                        break;
                    case 2:
                        if (tile.getSouthSideFeature() != t.getNorthSideFeature()) {
                            return false;
                        }
                        break;
                    case 3:
                        if (tile.getWestSideFeature() != t.getEastSideFeature()) {
                            return false;
                        }
                        break;
                    default:
                        return false;
                }
            }

            return true;
        }

        return false;
    }

    // Returns a number between 0 and 3 that represents the clockwise position of a
    // tile relative to the given coordinates
    private int getRelativePosition(Tile tile, Coordinates coordinates) {
        int pos = -1;
        int xDifference = tile.getCoordinates().getX() - coordinates.getX();
        int yDifference = tile.getCoordinates().getY() - coordinates.getY();

        if (yDifference == 1) {
            pos = 0;
        } else if (xDifference == 1) {
            pos = 1;
        } else if (yDifference == -1) {
            pos = 2;
        } else if (xDifference == -1) {
            pos = 3;
        }

        return pos;
    }

    public Tile getTileFromCoordinates(Coordinates coordinates) {
        return placedTiles.stream().filter(tile -> tile.getCoordinates().equals(coordinates)).findFirst().orElse(null);
    }

    public ArrayList<Coordinates> getPossibleCoordinates() {
        return possibleCoordinates;
    }

    public int getPlacedTilesSize() {
        return placedTiles.size();
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    //  *   *   *   *   *   *   *
    //  *   PRINTING METHODS    *
    //  *   *   *   *   *   *   *

    // Prints the board with the option to highlight a feature
    public void printBoard(SideFeature... highlightedFeatures) {
        String defaultColour = ANSI_RESET;
        String highlightColour = defaultColour;
        SideFeature SideFeature = highlightedFeatures.length > 0 ? highlightedFeatures[0] : null;

        if (SideFeature != null) {
            switch (SideFeature) {
                case CASTLE:
                    highlightColour = ANSI_RED;
                    break;
                case ROAD:
                    highlightColour = ANSI_RESET;
                    break;
                case FIELD:
                    highlightColour = ANSI_GREEN;
                    break;
                default:
                    break;
            }
        }

        List<Coordinates> coordinates = placedTiles.stream()
                .map(t -> t.getCoordinates())
                .collect(Collectors.toList());

        System.out.println("\n");
        double n = 0.0;
        for (int i = maxY; i >= minY; i--) {
            for (int j = minX; j <= maxX; j++) {
                Coordinates c = new Coordinates(j, i);
                if (j == 0 && i == 0) {
                    System.out.print(defaultColour + "O " + ANSI_RESET);
                } else if (coordinates.contains(c)) {
                    if (SideFeature != null && getTileFromCoordinates(c).getSideFeatures().contains(SideFeature)) {
                        System.out.print(highlightColour + "X " + ANSI_RESET);
                        n += 1;
                    } else {
                        System.out.print(getTileFromCoordinates(c).getOwner().getColour().getSymbol() + "X " + ANSI_RESET);
                    }
                } else {
                    System.out.print(ANSI_CYAN + ". " + ANSI_RESET);
                }
            }

            System.out.println();
        }
        System.out.println("Ratio: " + n/(placedTiles.size() - 1) );

        // Print highlight, height and width
        if (SideFeature != null) {
            System.out.println("Highlighting " + highlightColour + SideFeature.getSymbol() + "s" + ANSI_RESET + ".");
        }

        System.out.println("Height: " + height);
        System.out.println("Width: " + width);
    }

    // Keeps track of the board's width and height
    private void updateBoardStringParameters(Tile tile) {
        int x = tile.getCoordinates().getX();
        int y = tile.getCoordinates().getY();

        if (x > maxX) {
            maxX = x;
            width++;
        } else if (y > maxY) {
            maxY = y;
            height++;
        } else if (x < minX) {
            minX = x;
            width++;
        } else if (y < minY) {
            minY = y;
            height++;
        }
    }
}
