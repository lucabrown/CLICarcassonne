package luca.carcassonne;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.jupiter.api.Test;

import luca.carcassonne.tile.CardinalPoint;
import luca.carcassonne.tile.feature.Castle;
import luca.carcassonne.tile.feature.Field;

public class FeatureTest {

    // @Test
    // void testCastleClone() {
    // ArrayList<CardinalPoint> list = new ArrayList<CardinalPoint>() {
    // {
    // add(CardinalPoint.NNE);
    // add(CardinalPoint.N);
    // add(CardinalPoint.NNW);
    // }
    // };
    // Castle castle = new Castle(list, false);

    // Castle clone = (Castle) castle.clone();

    // castle.addCardinalPoint(CardinalPoint.S);

    // assertEquals(3, clone.getCardinalPoints().size());
    // assertEquals(4, castle.getCardinalPoints().size());

    // }

    // @Test
    // void testFieldClone() {
    // HashSet<Castle> castles = new HashSet<>() {
    // {
    // add(new Castle(new ArrayList<CardinalPoint>() {
    // {
    // add(CardinalPoint.NNE);
    // add(CardinalPoint.N);
    // add(CardinalPoint.NNW);
    // }
    // }, false));
    // }
    // };
    // ArrayList<CardinalPoint> list = new ArrayList<CardinalPoint>() {
    // {
    // add(CardinalPoint.ENE);
    // add(CardinalPoint.E);
    // add(CardinalPoint.ESE);
    // add(CardinalPoint.SSE);
    // add(CardinalPoint.S);
    // add(CardinalPoint.SSW);
    // add(CardinalPoint.WSW);
    // add(CardinalPoint.W);
    // add(CardinalPoint.WNW);
    // }
    // };

    // Field field = new Field(list, castles);

    // Field clone = (Field) field.clone();

    // field.getCardinalPoints().add(CardinalPoint.NNW);

    // assertEquals(9, clone.getCardinalPoints().size());
    // assertEquals(10, field.getCardinalPoints().size());

    // }
}
