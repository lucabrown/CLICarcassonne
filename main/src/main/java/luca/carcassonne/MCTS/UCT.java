package luca.carcassonne.MCTS;

/*
* All the code in this file is from
*
*
https://github.com/eugenp/tutorials/tree/master/algorithms-modules/algorithms-searching/src/main/java/com/baeldung/algorithms/mcts
*
* unless clearly stated otherwise.
*/

public class UCT {

    public static double uctValue(int totalVisit, double nodeScoreDifference, int nodeVisit) {
        if (nodeVisit == 0) {
            return Integer.MAX_VALUE;
        }
        return (nodeScoreDifference / (double) nodeVisit) + 1.41 *
                Math.sqrt(Math.log(totalVisit) / (double) nodeVisit);
    }

    static Node findBestNodeWithUCT(Node parentNode) {
        int parentVisit = parentNode.getState().getVisitCount();
        double bestValue = Integer.MIN_VALUE;
        Node bestNode = null;

        System.out.println("Children: " + parentNode.getChildren().size());

        for (Node childNode : parentNode.getChildren()) {
            double nodeValue = uctValue(parentVisit, childNode.getState().getFinalScoreDifference(),
                    childNode.getState().getVisitCount());
            if (nodeValue > bestValue) {
                bestNode = childNode;
                bestValue = nodeValue;
            }
        }

        return bestNode;
    }
}