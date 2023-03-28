package luca.carcassonne.mcts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import luca.carcassonne.CloneManager;
import luca.carcassonne.ScoreManager;
import luca.carcassonne.Settings;
import luca.carcassonne.tile.feature.Castle;
import luca.carcassonne.tile.feature.Monastery;

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

        Node bestChild = this.children.get(0);
        for (Node child : this.children) {
            State clonedState = CloneManager.clone(child.getState());
            ScoreManager.scoreOpenFeatures(clonedState.getBoard());
            int scoreAfterChildState = clonedState.getPlayers().get(state.getCurrentPlayer()).getScore();
            if (scoreAfterChildState >= bestChild.getState().getPlayers().get(state.getCurrentPlayer()).getScore()) {
                bestChild = child;
            }
        }

        return bestChild;
    }

    public Node getChildWithMaxScore() {
        return Collections.max(this.children, Comparator.comparing(c -> {
            return c.getState().getVisitCount();
        }));
    }

}