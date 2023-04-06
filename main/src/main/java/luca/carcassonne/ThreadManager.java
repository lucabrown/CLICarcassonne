package luca.carcassonne;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * This class is used to run multiple games in parallel.
 * 
 * {@code times} specifies the number of total games to be run.
 * {@code numberOfThreads} specifies the number of threads.
 * 
 * It is recommended to set {@code times} to be multiple of
 * {@code numberOfThreads}.
 * 
 * The results of all the games are stored in the static variables of this
 * class.
 * 
 * The results are printed to the console after all the games are finished.
 * 
 * @author Luca Brown
 */
public class ThreadManager {
    public static ExecutorService pool;
    public static List<Integer> playersTotalScore = Collections
            .synchronizedList(new ArrayList<Integer>());
    public static List<Integer> playersWR = Collections
            .synchronizedList(new ArrayList<Integer>());
    public static List<Integer> playersMaxScore = Collections
            .synchronizedList(new ArrayList<Integer>());
    public static List<Integer> playersMinScore = Collections
            .synchronizedList(new ArrayList<Integer>());
    public static List<Integer> scoreDelta = Collections
            .synchronizedList(new ArrayList<Integer>());
    public static List<Integer> ties = Collections
            .synchronizedList(new ArrayList<Integer>());
    public static List<Integer> timeForMove = Collections
            .synchronizedList(new ArrayList<Integer>(Collections.nCopies(71, 0)));
    static int numberOfThreads = 1;
    static float times = 1;

    /**
     * Runs the games.
     * 
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        long currentTime = System.currentTimeMillis();
        ArrayList<Game> games = new ArrayList<>();
        pool = Executors.newFixedThreadPool(numberOfThreads);

        for (int i = 0; i < times; i++) {
            games.add(new Game(new Board(Settings.getSingleCastleWithStraightRoad())));
        }

        for (int i = 0; i < games.get(0).getNumberOfPlayers(); i++) {
            playersTotalScore.add(0);
            playersWR.add(0);
            playersMaxScore.add(0);
            playersMinScore.add(100);
            ties.add(0);
        }

        for (Game game : games) {
            pool.execute(game);
        }

        pool.shutdown();

        try {
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            ArrayList<String> players = (new Game(new Board(Settings.getSingleCastleWithStraightRoad()))).getPlayers()
                    .stream().map(p -> p.getClass().getSimpleName()).collect(Collectors.toCollection(ArrayList::new));

            // calculate standard deviation of score delta
            float mean = scoreDelta.stream().mapToInt(Integer::intValue).sum() / scoreDelta.size();
            float sum = 0;
            for (int i = 0; i < scoreDelta.size(); i++) {
                sum += Math.pow(scoreDelta.get(i) - mean, 2);
            }
            float standardDeviation = (float) Math.sqrt(sum / scoreDelta.size());
            // calculate confidence interval
            float confidenceInterval = 1.96f * standardDeviation / (float) Math.sqrt(scoreDelta.size());
            // calculate standard error
            float standardError = standardDeviation / (float) Math.sqrt(scoreDelta.size());

            System.out.println("\nAfter " + times + " games:");
            for (int i = 0; i < playersWR.size(); i++) {
                System.out.println(players.get(i) + " won " + ((float) playersWR.get(i)) / times * 100
                        + "% of games (+" + (float) ties.get(i) + " tie(s)) " + "with an average score of "
                        + (float) playersTotalScore.get(i) / times
                        + " (range: " + playersMaxScore.get(i) + "-" + playersMinScore.get(i) + ")");

            }
            // System.out.println("Ties: " + (float) ties.get() / times * 100 + "%");
            System.out.println("Time: " + (System.currentTimeMillis() - currentTime) / 1000 + "s");
            System.out.println("Mean: " + mean);
            System.out.println("Standard deviation: " + standardDeviation);
            System.out.println("Standard error: " + standardError);
            System.out.println("Confidence interval: ±" + confidenceInterval);

            for (int i = 0; i < timeForMove.size(); i++) {
                // divide each element by the number og games
                timeForMove.set(i, timeForMove.get(i) / (int) times);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
