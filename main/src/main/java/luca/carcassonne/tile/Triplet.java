package luca.carcassonne.tile;

public class Triplet {
    private int tileIndex;
    private int featureIndex;
    private int playerIndex;

    public Triplet(int tileIndex, int featureIndex, int playerIndex) {
        this.tileIndex = tileIndex;
        this.featureIndex = featureIndex;
        this.playerIndex = playerIndex;
    }

    public Triplet(int tileIndex, int featureIndex) {
        this.tileIndex = tileIndex;
        this.featureIndex = featureIndex;
        this.playerIndex = -1;
    }

    public Triplet() {
        this.tileIndex = -1;
        this.featureIndex = -1;
        this.playerIndex = -1;
    }

    public int getTileIndex() {
        return tileIndex;
    }

    public int getFeatureIndex() {
        return featureIndex;
    }

    public void setTileIndex(int tileIndex) {
        this.tileIndex = tileIndex;
    }

    public void setFeatureIndex(int featureIndex) {
        this.featureIndex = featureIndex;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    @Override
    public String toString() {
        return "(" + tileIndex + ", " + featureIndex + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Triplet)) {
            return false;
        }
        Triplet c = (Triplet) o;
        return c.getTileIndex() == tileIndex && c.getFeatureIndex() == featureIndex;
    }

}
