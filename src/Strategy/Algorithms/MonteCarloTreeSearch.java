package Strategy.Algorithms;

import Game.Board;
import Game.Mark;

import java.util.List;

/**
 * Monte Carlo Tree Search (MCTS) is a search technique in the field of Artificial Intelligence (AI).
 * It is a probabilistic and heuristic driven search algorithm that combines the classic tree search implementations
 * alongside machine learning principles of reinforcement learning.
 * <p>
 * The <code>MonteCarloTreeSearch</code> contains two methods to set or get difficulty level.
 * <p>
 * The <code>MonteCarloTreeSearch</code> contains one method to find the best possible move (probably) using 4 phases:
 * selection, expansion, simulation and backpropagation.
 * <p>
 * The <code>MonteCarloTreeSearch</code> contains four methods for each of the mentioned phases.
 *
 * @author  Denis Krylov
 * @since   2.0
 * @see     Board
 * @see     Mark
 * @see     Tree
 * @see     Node
 * @see     UCT
 * @see     State
 */
public class MonteCarloTreeSearch {

    private static final int WIN_SCORE = 10;
    private int level;
    private Mark opponent;

    public MonteCarloTreeSearch(int level) {
        this.level = level;
    }

    /**
     * Returns the difficulty level of MCTS.
     *
     * @return      difficulty level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Sets the difficulty level of the MCTS.
     *
     * @param   level   difficulty level
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Finds the next possible moves using MCTS algorithm. At first, it selects the node with the best UCT score.
     * Then, if the game is not finished, expand this node by creating all possible states of the board and attach
     * them to the node. After that it gets a random child node from the child array and play a random game from
     * the state of this node. Then depending on the game result it back propagates to the root node changing parameters
     * of all connected nodes. It does it 1000 * level times (level is the difficulty level of the bot).
     *
     * @param   board   original board
     * @param   mark    playing mark
     * @return          best state with changed board
     */
    public Board findNextMove(Board board, Mark mark) {
        opponent = mark.other();
        Tree tree = new Tree();
        Node rootNode = tree.getRoot();
        rootNode.getState().setBoard(board);
        rootNode.getState().setMark(opponent);

        for (int i = 0; i < level * 10000; i++) {
            // Phase 1 - Selection
            Node promisingNode = selectPromisingNode(rootNode);

            // Phase 2 - Expansion
            if (!promisingNode.getState().getBoard().gameOver()) {
                expandNode(promisingNode);
            }

            // Phase 3 - Simulation
            Node nodeToExplore = promisingNode;
            if (promisingNode.getChildArray().size() > 0) {
                nodeToExplore = promisingNode.getRandomChildNode();
            }
            Mark gameResult = simulateRandomGame(nodeToExplore);

            // Phase 4 - Backpropagation
            backPropagation(nodeToExplore, gameResult);
        }

        Node winnerNode = rootNode.getChildWithMaxScore();
        tree.setRoot(winnerNode);
        return winnerNode.getState().getBoard();
    }

    /**
     * Selects the node with the highest UCT value.
     *
     * @param   rootNode    root node
     * @return              node with the highest UCT value
     */
    private Node selectPromisingNode(Node rootNode) {
        Node node = rootNode;
        while (node.getChildArray().size() != 0) {
            node = UCT.findBestNodeWithUCT(node);
        }
        return node;
    }

    /**
     * Adds a child array of all possible states of the board to the current node.
     *
     * @param   node    current node
     */
    private void expandNode(Node node) {
        List<State> possibleStates = node.getState().getAllPossibleStates();
        for (State state : possibleStates) {
            Node newNode = new Node(state);
            newNode.setParent(node);
            newNode.getState().setMark(node.getState().getOpponent());
            node.getChildArray().add(newNode);
        }
    }

    /**
     * Back propagates to the root node and incrementing the number of visits to the node and adding win score.
     *
     * @param   nodeToExplore   child node to explore
     * @param   mark            playing mark
     */
    private void backPropagation(Node nodeToExplore, Mark mark) {
        Node tempNode = nodeToExplore;
        while (tempNode != null) {
            tempNode.getState().incrementVisit();
            if (tempNode.getState().getMark() == mark) {
                tempNode.getState().addScore(WIN_SCORE);
            }
            tempNode = tempNode.getParent();
        }
    }

    /**
     * Simulates a random game on the selected node and returns the winner. It starts to use Minimax first, to avoid
     * critical traps that opponent can do. If there is no instant winning or losing, it uses a random game.
     * If the winner is a player, returns a player's mark, if the winner is an opponent returns an opponent's mark.
     * If no one, returns a player's mark by default.
     *
     * @param   node    child node
     * @return          a result of the random game
     */
    private Mark simulateRandomGame(Node node) {
        Node tempNode = new Node(node);
        State tempState = tempNode.getState();
        Board board = tempState.getBoard();
        Minimax minimax = new Minimax(1);

        if (board.isWinner(opponent)) {
            tempNode.getParent().getState().setWinScore(Integer.MIN_VALUE);
            return opponent;
        }

        int score = minimax.findNextMove(board, opponent);
        if (score == 1) {
            return opponent.other();
        } else if (score == -1) {
            return opponent;
        } else {
            while (!board.gameOver()) {
                tempState.toggleMark();
                tempState.randomPlay();
                if (board.isWinner(opponent)) return opponent;
                if (board.isWinner(opponent.other())) return opponent.other();
            }
        }

        return opponent.other();
    }

}
