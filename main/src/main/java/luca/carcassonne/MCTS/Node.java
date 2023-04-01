package luca.carcassonne.mcts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.stream.Collectors;

import luca.carcassonne.Settings;
import luca.carcassonne.tile.feature.Castle;
import luca.carcassonne.tile.feature.Monastery;

/**
 * A node in the MCTS tree.
 * 
 * @author Luca Brown
 */
public class Node {
    private State state;
    private Node parent;
    private ArrayList<Node> children;

    public Node() {
        this.state = new State();
        children = new ArrayList<>();
    }

    public Node(State state) {
        this.state = state;
        children = new ArrayList<>();
    }

    public Node(State state, Node parent, ArrayList<Node> children) {
        this.state = state;
        this.parent = parent;
        this.children = children;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Node> children) {
        this.children = children;
    }

    public boolean hasChildren() {
        return children.size() > 0;
    }

    public Node getRandomChildNode() {
        int noOfPossibleMoves = this.children.size();
        int selectRandom = (int) (Settings.getRandomFloat() * noOfPossibleMoves);
        if (noOfPossibleMoves == 0)
            return null;
        return this.children.get(selectRandom);
    }

    public Node getInformedRandomChildNode() {
        ArrayList<Node> goodChildren = new ArrayList<>();
        for (Node child : children) {
            Move lastMove = child.getState().getBoard().getLastMove();
            if (lastMove.getFeatureIndex() != -1
                    && (Settings.getTileFromId(lastMove.getTileId()).getFeatures().get(lastMove.getFeatureIndex())
                            .getClass() == Castle.class
                            || Settings.getTileFromId(lastMove.getTileId()).getFeatures()
                                    .get(lastMove.getFeatureIndex())
                                    .getClass() == Monastery.class)) {
                goodChildren.add(child);
            }
        }

        if (goodChildren.size() > 0) {
            int noOfPossibleMoves = goodChildren.size();
            int selectRandom = (int) (Settings.getRandomFloat() * noOfPossibleMoves);
            return goodChildren.get(selectRandom);
        }

        int noOfPossibleMoves = this.children.size();
        int selectRandom = (int) (Settings.getRandomFloat() * noOfPossibleMoves);

        return this.children.get(selectRandom);
    }

    public Node getChildWithGreedyPolicy() {
        if (this.children.size() == 0)
            return null;

        // wait for user input to continue
        Scanner scanner = new Scanner(System.in);

        ArrayList<Node> bestChildren = new ArrayList<>();
        int originalPlayer = this.state.getCurrentPlayer();
        int scoreBeforeChild = this.state.getPlayers().get(originalPlayer).getScore();

        for (Node child : this.children) {
            int scoreAfterChildState = child.getState().getPlayers().get(originalPlayer).getScore();
            if (scoreAfterChildState - scoreBeforeChild > 0 && (bestChildren.size() != 0 && scoreAfterChildState
                    - scoreBeforeChild > bestChildren.get(0).getState().getPlayers().get(originalPlayer).getScore()
                    || bestChildren.size() == 0)) {
                bestChildren.add(child);
            }
            // System.out.println("Checking " + child.getState().getBoard().getLastMove() +
            // " with score "
            // + scoreAfterChildState + " (before: " + scoreBeforeChild + ")");

        }

        if (bestChildren.size() == 0) {
            bestChildren = this.children.stream()
                    .filter(child -> child.getState().getBoard().getLastMove().getFeatureIndex() == -1)
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        int noOfPossibleMoves = bestChildren.size();
        int selectRandom = (int) (Settings.getRandomFloat() * noOfPossibleMoves);
        if (noOfPossibleMoves == 0)
            return null;

        // System.out.println("Selected " +
        // bestChildren.get(selectRandom).getState().getBoard().getLastMove()
        // + " with score " +
        // bestChildren.get(selectRandom).getState().getPlayers().get(originalPlayer)
        // .getScore());
        // scanner.nextLine();
        return bestChildren.get(selectRandom);
    }

    public Node getChildWithMaxScore() {
        if (this.children.size() == 0)
            return null;
        return Collections.max(this.children, Comparator.comparing(c -> {
            return c.getState().getVisitCount();
        }));
    }

}