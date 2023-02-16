package luca.carcassonne.tile.feature;

import java.util.ArrayList;

public class Monastery extends Feature {

    public Monastery() {
        super(new ArrayList<>());
    }

    @Override
    public Object clone() {
        Monastery newMonastery = new Monastery();

        newMonastery.pointsClosed = this.pointsClosed;
        newMonastery.pointsOpen = this.pointsOpen;

        return newMonastery;
    }
}
