package luca.carcassonne;

import org.junit.jupiter.api.Test;

import luca.carcassonne.tile.CardinalPoint;
import luca.carcassonne.tile.Tile;
import luca.carcassonne.tile.feature.Feature;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.HashSet;
import java.util.Stack;
import java.util.stream.Collectors;

class TileTest {
    HashSet<CardinalPoint> correctCardinalPoints = new HashSet<>() {
        {
            add(CardinalPoint.NNW);
            add(CardinalPoint.N);
            add(CardinalPoint.NNE);
            add(CardinalPoint.ENE);
            add(CardinalPoint.E);
            add(CardinalPoint.ESE);
            add(CardinalPoint.SSE);
            add(CardinalPoint.S);
            add(CardinalPoint.SSW);
            add(CardinalPoint.WSW);
            add(CardinalPoint.W);
            add(CardinalPoint.WNW);
        }
    };

    // Test that a tile has the correct number of cardinal points
    @Test
    void testTileHasCorrectNumberOfCardinalPoints() {
        Stack<Tile> tiles = Settings.getStandardDeck().stream().distinct().collect(Collectors.toCollection(Stack::new));
        HashSet<CardinalPoint> cardinalPoints;

        for (Tile tile : tiles) {
            System.out.println(tile.getId());
            cardinalPoints = new HashSet<>();

            for (Feature feature : tile.getFeatures()) {
                for (CardinalPoint cardinalPoint : feature.getCardinalPoints()) {
                    cardinalPoints.add(cardinalPoint);
                }
            }

            assertEquals(12, cardinalPoints.size());

        }
    }

    @Test
    void testTileHasAllCardinalPoints() {
        Stack<Tile> tiles = Settings.getStandardDeck();
        HashSet<CardinalPoint> cardinalPoints;

        for (Tile tile : tiles) {
            System.out.println(tile.getId());
            cardinalPoints = new HashSet<>();

            for (Feature feature : tile.getFeatures()) {
                for (CardinalPoint cardinalPoint : feature.getCardinalPoints()) {
                    cardinalPoints.add(cardinalPoint);
                }
            }

            assertEquals(correctCardinalPoints, cardinalPoints);
        }
    }

}
