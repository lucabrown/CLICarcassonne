package luca.carcassonne;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.HashMap;
import org.javatuples.Pair;
import org.junit.jupiter.api.Test;

import luca.carcassonne.player.Colour;
import luca.carcassonne.player.GreedyAgent;
import luca.carcassonne.player.MonteCarloAgent;
import luca.carcassonne.player.Player;
import luca.carcassonne.player.ProgressiveHistoryAgent;
import luca.carcassonne.player.RandomAgent;
import luca.carcassonne.tile.Coordinates;
import luca.carcassonne.tile.Tile;

public class CloneManagerTest {

    @Test
    void testCoordinatesClone() {
        Coordinates coordinates = new Coordinates(0, 0);
        Coordinates clone = CloneManager.clone(coordinates);

        assertEquals(0, coordinates.getX());
        assertEquals(0, coordinates.getY());
        assertEquals(0, clone.getX());
        assertEquals(0, clone.getY());

        coordinates.setX(5);
        coordinates.setY(5);

        assertEquals(5, coordinates.getX());
        assertEquals(5, coordinates.getY());
        assertEquals(0, clone.getX());
        assertEquals(0, clone.getY());

        clone.setX(3);
        clone.setY(3);

        assertEquals(5, coordinates.getX());
        assertEquals(5, coordinates.getY());
        assertEquals(3, clone.getX());
        assertEquals(3, clone.getY());
    }

    @Test
    void testTileClone() {
        Tile tile = Settings.getStartingTile();
        Tile clone = CloneManager.clone(tile);

        assertEquals(tile.getNorthSideFeature(), clone.getNorthSideFeature());

        tile.rotateClockwise();

        assertNotEquals(tile.getNorthSideFeature(), clone.getNorthSideFeature());

        clone.rotateClockwise(2);

        assertNotEquals(tile.getNorthSideFeature(), clone.getNorthSideFeature());
    }

    @Test
    void testPlayerClone() {
        Player player = new Player(Colour.RED);
        Player clone = CloneManager.clone(player);

        assertEquals(0, player.getScore());
        assertEquals(0, clone.getScore());
        assertEquals(Settings.MAX_MEEPLES, player.getAvailableMeeples());
        assertEquals(Settings.MAX_MEEPLES, clone.getAvailableMeeples());

        player.addScore(5);

        assertEquals(5, player.getScore());
        assertEquals(0, clone.getScore());

        clone.addScore(3);

        assertEquals(5, player.getScore());
        assertEquals(3, clone.getScore());

        player.decrementMeeples();

        assertEquals(Settings.MAX_MEEPLES - 1, player.getAvailableMeeples());
        assertEquals(Settings.MAX_MEEPLES, clone.getAvailableMeeples());

        clone.decrementMeeples();
        clone.decrementMeeples();

        assertEquals(Settings.MAX_MEEPLES - 1, player.getAvailableMeeples());
        assertEquals(Settings.MAX_MEEPLES - 2, clone.getAvailableMeeples());
    }

    @Test
    void testRandomAgentClone() {
        RandomAgent agent = new RandomAgent(Colour.RED);
        RandomAgent clone = CloneManager.clone(agent);

        assertEquals(Colour.RED, agent.getColour());
        assertEquals(Colour.RED, clone.getColour());
        assertEquals(Settings.MAX_MEEPLES, agent.getAvailableMeeples());
        assertEquals(Settings.MAX_MEEPLES, clone.getAvailableMeeples());
        assertEquals(0, agent.getScore());
        assertEquals(0, clone.getScore());

        agent.addScore(5);
        agent.decrementMeeples();

        assertEquals(Colour.RED, agent.getColour());
        assertEquals(Colour.RED, clone.getColour());
        assertEquals(Settings.MAX_MEEPLES - 1, agent.getAvailableMeeples());
        assertEquals(Settings.MAX_MEEPLES, clone.getAvailableMeeples());
        assertEquals(5, agent.getScore());
        assertEquals(0, clone.getScore());
    }

    @Test
    void testGreedyAgentClone() {
        GreedyAgent agent = new GreedyAgent(Colour.RED);
        GreedyAgent clone = CloneManager.clone(agent);

        assertEquals(Colour.RED, agent.getColour());
        assertEquals(Colour.RED, clone.getColour());
        assertEquals(Settings.MAX_MEEPLES, agent.getAvailableMeeples());
        assertEquals(Settings.MAX_MEEPLES, clone.getAvailableMeeples());
        assertEquals(0, agent.getScore());
        assertEquals(0, clone.getScore());

        agent.addScore(5);
        agent.decrementMeeples();

        assertEquals(Colour.RED, agent.getColour());
        assertEquals(Colour.RED, clone.getColour());
        assertEquals(Settings.MAX_MEEPLES - 1, agent.getAvailableMeeples());
        assertEquals(Settings.MAX_MEEPLES, clone.getAvailableMeeples());
        assertEquals(5, agent.getScore());
        assertEquals(0, clone.getScore());
    }

    @Test
    void testMonteCarloAgentClone() {
        MonteCarloAgent agent = new MonteCarloAgent(Colour.RED, 500, 0.5);
        MonteCarloAgent clone = CloneManager.clone(agent);

        assertEquals(Colour.RED, agent.getColour());
        assertEquals(Colour.RED, clone.getColour());
        assertEquals(Settings.MAX_MEEPLES, agent.getAvailableMeeples());
        assertEquals(Settings.MAX_MEEPLES, clone.getAvailableMeeples());
        assertEquals(0, agent.getScore());
        assertEquals(0, clone.getScore());
        assertEquals(500, agent.getMaxIterations());
        assertEquals(500, clone.getMaxIterations());
        assertEquals(0.5, agent.getExplorationConstant());
        assertEquals(0.5, clone.getExplorationConstant());

        agent.addScore(5);
        agent.decrementMeeples();
        agent.setExplorationConstant(1);
        agent.setMaxIterations(1000);

        assertEquals(Colour.RED, agent.getColour());
        assertEquals(Colour.RED, clone.getColour());
        assertEquals(Settings.MAX_MEEPLES - 1, agent.getAvailableMeeples());
        assertEquals(Settings.MAX_MEEPLES, clone.getAvailableMeeples());
        assertEquals(5, agent.getScore());
        assertEquals(0, clone.getScore());
        assertEquals(1000, agent.getMaxIterations());
        assertEquals(500, clone.getMaxIterations());
        assertEquals(1, agent.getExplorationConstant());
        assertEquals(0.5, clone.getExplorationConstant());
    }

    @Test
    void testProgressiveHistoryAgentClone() {
        HashMap<Pair<String, Integer>, Integer> totalActionMap = new HashMap<>();
        HashMap<Pair<String, Integer>, Integer> totalStateMap = new HashMap<>();
        ProgressiveHistoryAgent agent = new ProgressiveHistoryAgent(Colour.RED, 500, 0.5, totalActionMap,
                totalStateMap);
        ProgressiveHistoryAgent clone = CloneManager.clone(agent);

        assertEquals(Colour.RED, agent.getColour());
        assertEquals(Colour.RED, clone.getColour());
        assertEquals(Settings.MAX_MEEPLES, agent.getAvailableMeeples());
        assertEquals(Settings.MAX_MEEPLES, clone.getAvailableMeeples());
        assertEquals(0, agent.getScore());
        assertEquals(0, clone.getScore());
        assertEquals(500, agent.getMaxIterations());
        assertEquals(500, clone.getMaxIterations());
        assertEquals(0.5, agent.getExplorationConstant());
        assertEquals(0.5, clone.getExplorationConstant());

        agent.addScore(5);
        agent.decrementMeeples();
        agent.setExplorationConstant(1);
        agent.setMaxIterations(1000);

        assertEquals(Colour.RED, agent.getColour());
        assertEquals(Colour.RED, clone.getColour());
        assertEquals(Settings.MAX_MEEPLES - 1, agent.getAvailableMeeples());
        assertEquals(Settings.MAX_MEEPLES, clone.getAvailableMeeples());
        assertEquals(5, agent.getScore());
        assertEquals(0, clone.getScore());
        assertEquals(1000, agent.getMaxIterations());
        assertEquals(500, clone.getMaxIterations());
        assertEquals(1, agent.getExplorationConstant());
        assertEquals(0.5, clone.getExplorationConstant());
    }

    // @Test
    // void testBoardClone() {
    // Board oldBoard = new Board();
    // ArrayList<Player> oldPlayers = new ArrayList<>();
    // oldPlayers.add(new RandomAgent(Colour.RED));
    // oldPlayers.add(new GreedyAgent(Colour.BLUE));

    // Tile castleTile = Settings.getBigCastleWithRoad();
    // Feature castleFeature = castleTile.getFeatures().get(3);
    // castleTile.rotateClockwise();
    // Tile roadTile = Settings.getStraightRoad();
    // Feature roadFeature = roadTile.getFeatures().get(1);

    // oldBoard.placeTile(new Coordinates(0, 1), castleTile);
    // oldBoard.placeMeeple(castleFeature, oldPlayers.get(0));
    // oldBoard.placeTile(new Coordinates(0, -1), roadTile);
    // oldBoard.placeMeeple(roadFeature, oldPlayers.get(1));

    // ArrayList<Player> newPlayers = oldPlayers.stream().map(CloneManager::clone)
    // .collect(Collectors.toCollection(ArrayList::new));

    // Board newBoard = CloneManager.clone(oldBoard, newPlayers);
    // for (int i = 0; i < oldPlayers.size(); i++) {
    // assertEquals(oldPlayers.get(i).getColour(), newPlayers.get(i).getColour());
    // assertEquals(oldPlayers.get(i).getScore(), newPlayers.get(i).getScore());
    // assertEquals(oldPlayers.get(i).getAvailableMeeples(),
    // newPlayers.get(i).getAvailableMeeples());
    // }
    // }
}
