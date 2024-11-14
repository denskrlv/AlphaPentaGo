package Strategy.Algorithms;

import java.util.Collections;
import java.util.Comparator;

/**
 * Upper Confidence Bound applied to trees.
 * <p>
 * The <code>UCT</code> contains one method to compute UCT value.
 * <img src="data:image/jpeg;base64,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" />
 * <p>
 * The <code>UCT</code> contains one method to find the node with the highest UCT value.
 *
 * @author  Denis Krylov
 * @since   2.0
 * @see     Minimax
 * @see     MonteCarloTreeSearch
 */
public class UCT {

    /**
     * Computes the UCT value using the amount of visits to the parent node, winning score of the child node and
     * the amount of visits to the child node.
     *
     * @param   totalVisit      the amount of visits for the parent node
     * @param   nodeWinScore    the win score of the node
     * @param   nodeVisit
     * @return
     */
    public static double uctValue(int totalVisit, double nodeWinScore, int nodeVisit) {
        if (nodeVisit == 0) {
            return Integer.MAX_VALUE;
        }
        return (nodeWinScore / (double) nodeVisit) + 1.41 * Math.sqrt(Math.log(totalVisit) / (double) nodeVisit);
    }

    /**
     * Finds the node with the highest UCT in the child array of particular parent.
     *
     * @param   node    parent node
     * @return          child node with the highest UCT
     */
    static Node findBestNodeWithUCT(Node node) {
        int parentVisit = node.getState().getVisitCount();
        return Collections.max(
                node.getChildArray(),
                Comparator.comparing(c -> uctValue(parentVisit, c.getState().getWinScore(), c.getState().getVisitCount())));
    }
}
