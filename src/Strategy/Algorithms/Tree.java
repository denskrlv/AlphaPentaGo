package Strategy.Algorithms;

/**
 * Represents the decision tree which is the part of MCTS algorithm.
 * <p>
 * The <code>Tree</code> contains two methods to set or get root node.
 * <p>
 * The <code>DecisionEngine</code> contains one method to add child node.
 *
 * @author  Denis Krylov
 * @since   2.0
 * @see     Node
 * @see     MonteCarloTreeSearch
 */
public class Tree {

    private Node root;

    public Tree() {
        root = new Node();
    }

    public Tree(Node root) {
        this.root = root;
    }

    /**
     * Returns the root node of the tree.
     *
     * @return      root node
     */
    public Node getRoot() {
        return root;
    }

    /**
     * Sets the root node of the tree.
     *
     * @param   root    root node
     */
    public void setRoot(Node root) {
        this.root = root;
    }

    /**
     * Adds a child node for selected parent node.
     *
     * @param   parent  parent node
     * @param   child   child node
     */
    public void addChild(Node parent, Node child) {
        parent.getChildArray().add(child);
    }

}
