package luca.carcassonne;

public enum Colour {
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    BLUE("\u001B[34m"),
    YELLOW("\u001B[33m"),
    BLACK("\u001B[30m"),
    WHITE("\u001B[37m");

    private String symbol;

    Colour(String symbol){
        this.symbol=symbol;
    }

    public String getSymbol(){
        return symbol;
    }
}