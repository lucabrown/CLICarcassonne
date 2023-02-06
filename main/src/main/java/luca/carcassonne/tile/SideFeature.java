package luca.carcassonne.tile;

import java.util.Random;

// The main features on a tile's side
public enum SideFeature {
    CASTLE("castle"),
    ROAD("road"),
    FIELD("field");

    private static final Random random = new Random();
    private String symbol;

    SideFeature(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static SideFeature getRandomFeature() {
        SideFeature[] features = values();
        return features[random.nextInt(features.length)];
    }

}
