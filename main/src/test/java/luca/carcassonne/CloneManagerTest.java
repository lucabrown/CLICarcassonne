package luca.carcassonne;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import luca.carcassonne.player.Colour;
import luca.carcassonne.player.Player;

public class CloneManagerTest {

    @Test
    void testPlayerClone() {
        Player player = new Player(Colour.RED);
        Player clone = CloneManager.clone(player);

        assertEquals(0, player.getScore());
        assertEquals(0, clone.getScore());

        player.addScore(5);

        assertEquals(5, player.getScore());
        assertEquals(0, clone.getScore());

    }
}
