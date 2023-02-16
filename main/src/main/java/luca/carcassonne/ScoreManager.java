package luca.carcassonne;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import luca.carcassonne.player.Player;
import luca.carcassonne.tile.Tile;
import luca.carcassonne.tile.feature.Castle;
import luca.carcassonne.tile.feature.Feature;
import luca.carcassonne.tile.feature.Monastery;
import luca.carcassonne.tile.feature.Road;
import luca.carcassonne.tile.feature.Field;

// A class that handles the game's score
public class ScoreManager {

    // Scores all closed features
    public static void scoreClosedFeatures(Board board) {
        if (board.getNewlyClosedFeatures().isEmpty()) {
            return;
        }

        for (SimpleGraph<Feature, DefaultEdge> feature : board.getNewlyClosedFeatures()) {
            if (feature.vertexSet().iterator().next().getClass() == Field.class) {
                continue;
            }

            List<Player> owners = new ArrayList<>();
            int score = 0;

            owners = getFeatureOwners(feature);
            score = calculateFeatureValue(board, feature, true);

            if (owners.isEmpty()) {
                continue;
            }

            for (Player owner : owners) {
                owner.addScore(score);
            }

        }
    }

    // Scores all the open features
    public static void scoreOpenFeatures(Board board) {
        if (board.getOpenFeatures().isEmpty()) {
            return;
        }

        for (SimpleGraph<Feature, DefaultEdge> feature : board.getOpenFeatures()) {
            Set<Player> owners = new HashSet<>();
            int score = 0;

            owners = Feature.getPlayersOnFeature(feature).keySet();
            score = calculateFeatureValue(board, feature, false);

            if (owners.isEmpty() || score == 0) {
                continue;
            }

            for (Player owner : owners) {
                owner.addScore(score);
            }

        }
    }

    // Returns a list of players who own the feature
    private static List<Player> getFeatureOwners(SimpleGraph<Feature, DefaultEdge> feature) {
        ArrayList<Player> owners = new ArrayList<>();
        HashMap<Player, Integer> players = new HashMap<>();
        int maxMeeples = 0;

        players = Feature.getPlayersOnFeature(feature);

        // Find the player(s) with the most meeples on the feature
        if (!players.isEmpty()) {
            int nMeeples = 0;
            maxMeeples = players.values().stream().max(Integer::compare).get();

            for (Player p : players.keySet()) {
                nMeeples = players.get(p);
                p.incrementMeeples(nMeeples);

                if (nMeeples == maxMeeples) {
                    owners.add(p);
                }
            }

        }

        return owners;
    }

    // Calculates the value of a feature
    private static int calculateFeatureValue(Board board, SimpleGraph<Feature, DefaultEdge> feature, boolean isClosed) {
        HashSet<Tile> belongingTiles = new HashSet<>();
        Class<?> featureClass = feature.vertexSet().iterator().next().getClass();
        int score = 0;

        for (Feature vertex : feature.vertexSet()) {
            belongingTiles.add(vertex.getBelongingTile());
        }

        if (featureClass == Road.class) {
            score += belongingTiles.size();
        } else if (featureClass == Castle.class) {
            int shields = 0;

            for (Feature vertex : feature.vertexSet()) {
                if (((Castle) vertex).hasShield()) {
                    shields++;
                }
            }

            if (isClosed) {
                score += belongingTiles.size() * Settings.CASTLE_POINTS_CLOSED
                        + shields * Settings.SHIELD_POINTS_CLOSED;
            } else {
                score += belongingTiles.size() * Settings.CASTLE_POINTS_OPEN + shields * Settings.SHIELD_POINTS_OPEN;
            }
        } else if (featureClass == Field.class) {
            score += calculateFieldScore(feature, board);
        } else if (featureClass == Monastery.class) {
            if (isClosed) {
                score += 9;
            } else {
                Tile monasteryTile = board.getTileFromFeature(feature.vertexSet().iterator().next());
                score += 1 + board.getSurroundingTiles(monasteryTile);
            }
        } else {
            throw new IllegalArgumentException("Feature class not recognized");
        }

        return score;
    }

    // Scores all the fields on the board
    private static int calculateFieldScore(SimpleGraph<Feature, DefaultEdge> feature, Board board) {
        HashSet<SimpleGraph<Feature, DefaultEdge>> adjacentCastles = new HashSet<>();
        int score = 0;

        for (Feature vertex : feature.vertexSet()) {
            Field field = (Field) vertex;

            if (!field.hasAdjacentCastle()) {
                continue;
            }

            for (SimpleGraph<Feature, DefaultEdge> castle : board.getClosedFeatures()) {
                for (Feature castleVertex : field.getAdjacentCastles()) {
                    if (castle.containsVertex(castleVertex)) {
                        adjacentCastles.add(castle);
                    }
                }
            }
        }
        score += Settings.FIELD_POINTS_PER_CASTLE * adjacentCastles.size();

        return score;
    }
}
