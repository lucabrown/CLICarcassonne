package luca.carcassonne.tile;

// The main features on a tile's side
public enum SideFeature {
    CASTLE("castle"),
    ROAD("road"),
    FIELD("field");

    private String symbol;

    SideFeature(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

}
