package luca.carcassonne;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

// Starts the game and handles turns and available tiles
public class ThreadManager {
    public static List<Integer> playersTotalScore = Collections
            .synchronizedList(new ArrayList<Integer>());
    public static List<Integer> playersWR = Collections
            .synchronizedList(new ArrayList<Integer>());
    public static List<Integer> playersMaxScore = Collections
            .synchronizedList(new ArrayList<Integer>());
    public static List<Integer> playersMinScore = Collections
            .synchronizedList(new ArrayList<Integer>());
    public static AtomicInteger ties = new AtomicInteger();
    static int numberOfThreads = 1;
    static float times = 1;

    public static void main(String[] args) {
        long currentTime = System.currentTimeMillis();
        ArrayList<Game> games = new ArrayList<>();
        ExecutorService pool = Executors.newFixedThreadPool(numberOfThreads);

        for (int i = 0; i < times; i++) {
            games.add(new Game(new Board(Settings.getSingleCastleWithStraightRoad())));
        }

        for (int i = 0; i < games.get(0).getNumberOfPlayers(); i++) {
            playersTotalScore.add(0);
            playersWR.add(0);
            playersMaxScore.add(0);
            playersMinScore.add(100);
        }

        for (Game game : games) {
            pool.execute(game);
        }

        pool.shutdown();
        try {
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

            System.out.println("\nAfter " + times + " games:");

            for (int i = 0; i < playersWR.size(); i++) {
                System.out.println("Player " + i + " won " + ((float) playersWR.get(i)) / times * 100
                        + "% of games with an average score of "
                        + (float) playersTotalScore.get(i) / times
                        + " (range: " + playersMaxScore.get(i) + "-" + playersMinScore.get(i) + ")");

            }
            System.out.println("Ties: " + ties + "%");
            System.out.println("Time: " + (System.currentTimeMillis() - currentTime) / 1000 + "s");
            System.out.println(Settings.getStandardDeck().size());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
