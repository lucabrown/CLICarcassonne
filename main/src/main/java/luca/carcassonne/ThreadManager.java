package luca.carcassonne;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import luca.carcassonne.mcts.MonteCarloTreeSearch;
import luca.carcassonne.mcts.Move;
import luca.carcassonne.player.Colour;
import luca.carcassonne.player.Player;
import luca.carcassonne.tile.Coordinates;
import luca.carcassonne.tile.Tile;
import luca.carcassonne.tile.feature.Feature;
import luca.carcassonne.tile.feature.Field;

// Starts the game and handles turns and available tiles
public class ThreadManager {
    public static AtomicInteger whiteTotalScore = new AtomicInteger();
    public static AtomicInteger redTotalScore = new AtomicInteger();
    public static AtomicInteger whiteWR = new AtomicInteger();
    public static AtomicInteger redWR = new AtomicInteger();
    public static AtomicInteger ties = new AtomicInteger();
    static int numberOfThreads = 10;
    static float times = 10;

    public static void main(String[] args) {
        long currentTime = System.currentTimeMillis();
        ArrayList<Game> games = new ArrayList<>();
        ExecutorService pool = Executors.newFixedThreadPool(numberOfThreads);

        for (int i = 0; i < times; i++) {
            games.add(new Game(new Board(Settings.getSingleCastleWithStraightRoad())));
        }

        for (Game game : games) {
            pool.execute(game);
        }

        pool.shutdown();
        try {
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

            float fWhiteWR = (whiteWR.get() / times * 100);
            float fRedWR = (redWR.get() / times * 100);
            float fTies = (ties.get() / times * 100);
            System.out.println("\nAfter " + times + " games:");

            System.out.println(
                    "White won " + fWhiteWR + "% of games with an average score of " + whiteTotalScore.get() / times);
            System.out.println(
                    "Red won " + fRedWR + "% of games with an average score of " + redTotalScore.get() / times);
            System.out.println("Ties: " + fTies + "%");
            System.out.println("Time: " + (System.currentTimeMillis() - currentTime) / 1000 + "s");
            System.out.println(Settings.getStandardDeck().size());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
