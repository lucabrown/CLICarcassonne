package luca.carcassonne.MCTS;

/*
* All the code in this file is from
*
*
https://github.com/eugenp/tutorials/tree/master/algorithms-modules/algorithms-searching/src/main/java/com/baeldung/algorithms/mcts
*
* unless clearly stated otherwise.
*/

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import luca.carcassonne.tile.feature.Castle;
import luca.carcassonne.tile.feature.Monastery;

public class Node {
    State state;
    Node parent;
    ArrayList<Node> children;

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

    // public Node getRandomChildNode() {
    // int noOfPossibleMoves = this.children.size();
    // int selectRandom = (int) (Math.random() * noOfPossibleMoves);

    // return this.children.get(selectRandom);
    // }

    public Node getRandomChildNode() {
        for (Node child : children) {
            Move lastMove = child.getState().getBoard().getLastMove();
            if (lastMove.getFeatureIndex() != -1
                    && (lastMove.getTile().getFeatures().get(lastMove.getFeatureIndex()).getClass() == Castle.class
                            || lastMove.getTile().getFeatures().get(lastMove.getFeatureIndex())
                                    .getClass() == Monastery.class)) {
                return child;
            }

        }

        int noOfPossibleMoves = this.children.size();
        int selectRandom = (int) (Math.random() * noOfPossibleMoves);

        return this.children.get(selectRandom);
    }

    public Node getChildWithMaxScore() {
        return Collections.max(this.children, Comparator.comparing(c -> {
            return c.getState().getVisitCount();
        }));
    }

    // @Override
    // public Object clone() {
    // Node newNode = new Node();

    // newNode.setState((State) state.clone());
    // newNode.setParent(parent);
    // for (Node child : children) {
    // newNode.getChildren().add((Node) child.clone());
    // }

    // return newNode;
    // }

}