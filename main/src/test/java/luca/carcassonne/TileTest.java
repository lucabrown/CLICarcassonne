package luca.carcassonne;

import org.junit.jupiter.api.Test;

import luca.carcassonne.tile.CardinalPoint;
import luca.carcassonne.tile.Coordinates;
import luca.carcassonne.tile.Tile;
import luca.carcassonne.tile.feature.Feature;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
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

    // @Test
    // void testTileRotation(){
    // Field westField = new Field(new ArrayList<CardinalPoint>() {
    // {
    // add(CardinalPoint.WNW);
    // add(CardinalPoint.W);
    // add(CardinalPoint.WSW);
    // }
    // }, null);
    // Field eastField = new Field(new ArrayList<CardinalPoint>() {
    // {
    // add(CardinalPoint.ENE);
    // add(CardinalPoint.E);
    // add(CardinalPoint.ESE);
    // }
    // }, null);
    // Road northRoad = new Road(new ArrayList<CardinalPoint>() {
    // {
    // add(CardinalPoint.NNW);
    // add(CardinalPoint.N);
    // add(CardinalPoint.NNE);
    // }
    // });
    // Road southRoad = new Road(new ArrayList<CardinalPoint>() {
    // {
    // add(CardinalPoint.SSW);
    // add(CardinalPoint.S);
    // add(CardinalPoint.SSE);
    // }
    // });

    // Tile tileToRotate = new Tile(SideFeature.ROAD, SideFeature.FIELD,
    // SideFeature.ROAD, SideFeature.FIELD, new HashSet<>(){
    // {
    // add(westField);
    // add(eastField);
    // add(northRoad);
    // add(southRoad);
    // }
    // }, "null");

    // Field rotatedWestField = westField;
    // rotatedWestField.setCardinalPoints(rotatedWestField.getCardinalPoints().indexOf(CardinalPoint.WNW),
    // CardinalPoint.NNE);
    // rotatedWestField.setCardinalPoints(rotatedWestField.getCardinalPoints().indexOf(CardinalPoint.W),
    // CardinalPoint.N);
    // rotatedWestField.setCardinalPoints(rotatedWestField.getCardinalPoints().indexOf(CardinalPoint.WSW),
    // CardinalPoint.NNW);
    // Field rotatedEastField = eastField;
    // rotatedEastField.setCardinalPoints(rotatedEastField.getCardinalPoints().indexOf(CardinalPoint.ENE),
    // CardinalPoint.SSE);
    // rotatedEastField.setCardinalPoints(rotatedEastField.getCardinalPoints().indexOf(CardinalPoint.E),
    // CardinalPoint.S);
    // rotatedEastField.setCardinalPoints(rotatedEastField.getCardinalPoints().indexOf(CardinalPoint.ESE),
    // CardinalPoint.SSW);
    // Road rotatedNorthRoad = northRoad;
    // rotatedNorthRoad.setCardinalPoints(rotatedNorthRoad.getCardinalPoints().indexOf(CardinalPoint.NNE),
    // CardinalPoint.ESE);
    // rotatedNorthRoad.setCardinalPoints(rotatedNorthRoad.getCardinalPoints().indexOf(CardinalPoint.N),
    // CardinalPoint.E);
    // rotatedNorthRoad.setCardinalPoints(rotatedNorthRoad.getCardinalPoints().indexOf(CardinalPoint.NNW),
    // CardinalPoint.ENE);
    // Road rotatedSouthRoad = southRoad;
    // rotatedSouthRoad.setCardinalPoints(rotatedSouthRoad.getCardinalPoints().indexOf(CardinalPoint.SSE),
    // CardinalPoint.WSW);
    // rotatedSouthRoad.setCardinalPoints(rotatedSouthRoad.getCardinalPoints().indexOf(CardinalPoint.S),
    // CardinalPoint.W);
    // rotatedSouthRoad.setCardinalPoints(rotatedSouthRoad.getCardinalPoints().indexOf(CardinalPoint.SSW),
    // CardinalPoint.WNW);

    // Tile rotatedTile = new Tile(SideFeature.FIELD, SideFeature.ROAD,
    // SideFeature.FIELD, SideFeature.ROAD, new HashSet<>(){
    // {
    // add(rotatedWestField);
    // add(rotatedEastField);
    // add(rotatedNorthRoad);
    // add(rotatedSouthRoad);
    // }
    // }, "null");

    // tileToRotate.rotateClockwise(1);

    // assertEquals(rotatedTile.getFeatures(), tileToRotate.getFeatures());

    // }

    // @Test
    // void testTileClone() {
    // Tile singleCastle = Settings.getSingleCastle();
    // singleCastle.setCoordinates(new Coordinates(0, 0));
    // Tile clone = (Tile) singleCastle.clone();

    // assertEquals(singleCastle.getNorthSideFeature(),
    // clone.getNorthSideFeature());
    // singleCastle.rotateClockwise(1);
    // assertNotEquals(singleCastle.getNorthSideFeature(),
    // clone.getNorthSideFeature());

    // }
}
