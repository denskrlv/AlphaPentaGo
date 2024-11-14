package Strategy.Algorithms;

import java.util.*;

/**
 * Represents the node in the decision tree which is the part of MCTS algorithm.
 * <p>
 * The <code>Node</code> contains six methods to get or set state, parent or child array for the node.
 * <p>
 * The <code>Node</code> contains one method that chooses random child node.
 * <p>
 * The <code>Node</code> contains one method that chooses child node with maximum score.
 *
 * @author  Denis Krylov
 * @since   2.0
 * @see     Tree
 * @see     State
 * @see     MonteCarloTreeSearch
 */
public class Node {
    State state;
    Node parent;
    List<Node> childArray;

    public Node() {
        this.state = new State();
        childArray = new ArrayList<>();
    }

    public Node(State state) {
        this.state = state;
        childArray = new ArrayList<>();
    }

    public Node(State state, Node parent, List<Node> childArray) {
        this.state = state;
        this.parent = parent;
        this.childArray = childArray;
    }

    public Node(Node node) {
        this.childArray = new ArrayList<>();
        this.state = new State(node.getState());
        if (node.getParent() != null)
            this.parent = node.getParent();
        List<Node> childArray = node.getChildArray();
        for (Node child : childArray) {
            this.childArray.add(new Node(child));
        }
    }

    /**
     * Returns the state of the current node.
     *
     * @return      state of the board
     */
    public State getState() {
        return state;
    }

    /**
     * Sets the selected state to the current node.
     *
     * @param   state   state of the board
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * Returns the parent of the current node.
     *
     * @return      parent of the node
     */
    public Node getParent() {
        return parent;
    }

    /**
     * Sets the parent node to the current node.
     *
     * @param   parent  parent node
     */
    public void setParent(Node parent) {
        this.parent = parent;
    }

    /**
     * Returns the array of child nodes
     *
     * @return      array of child nodes
     */
    public List<Node> getChildArray() {
        return childArray;
    }

    /**
     * Sets the array of nodes to the current node.
     *
     * @param   childArray  array of nodes
     */
    public void setChildArray(List<Node> childArray) {
        this.childArray = childArray;
    }

    /**
     * Select a random node in the child node array.
     *
     * @return      random node
     */
    public Node getRandomChildNode() {
        int noOfPossibleMoves = this.childArray.size();
        int selectRandom = (int) (Math.random() * noOfPossibleMoves);
        return this.childArray.get(selectRandom);
    }

    /**
     * Select the child node with maximum score. The more score is the more probability that
     * the move will help to win.
     *
     * @return      node in the child array with the highest score
     */
    public Node getChildWithMaxScore() throws NoSuchElementException {
            return Collections.max(childArray, Comparator.comparing(c -> c.getState().getVisitCount()));
    }

}
