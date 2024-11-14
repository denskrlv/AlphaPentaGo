package Strategy;

import Game.Board;
import Game.Mark;
import Strategy.Algorithms.Minimax;
import Strategy.Algorithms.MonteCarloTreeSearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents the brain of the bot that helps to find best moves on the current board.
 * <p>
 * The <code>DecisionEngine</code> contains one method to determine the best decision based on
 * the Monte Carlo Tree Search (with UCT) combined with Minimax.
 * <p>
 * The <code>DecisionEngine</code> contains two methods to dynamically decide what Minimax depth to choose
 * and how many iterations to make to compute the most probable move with MCTS.
 * <p>
 * The <code>DecisionEngine</code> contains one method to extract the move from two different board states. 
 *
 * @author  Denis Krylov
 * @since   2.0
 * @see     Minimax
 * @see     MonteCarloTreeSearch
 */
public class DecisionEngine {

    private Board board;
    private Mark mark;

    public DecisionEngine(Board board, Mark mark) {
        this.board = board;
        this.mark = mark;
    }

    /**
     * Computes the best possible move on the current board with selected mark and chosen difficulty.
     * The more difficulty is the more iterations do the MCTS. After the method converts the move
     * to the string of type MOVE~move_index~rotation_index.
     *
     * @param   difficulty  how many iterations should MCTS do
     * @return              best move as a String
     */
    public String[] compute(int difficulty) {
        MonteCarloTreeSearch mcts = new MonteCarloTreeSearch(difficulty);
        Board resultBoard = mcts.findNextMove(board, mark);
        List<Integer> decision = extractMove(board, resultBoard);
        String[] result = new String[3];
        result[0] = "MOVE";
        if (!decision.isEmpty()) {
            result[1] = decision.get(0) + "";
            result[2] = decision.get(1) + "";
        } else {
            List<Integer> emptyCells = board.getEmptyCells();
            result[1] = emptyCells.get(0) + "";
            result[2] = 0 + "";
        }
        System.out.println(result[0] + "~" + result[1] + "~" + result[2]);
        return result;
    }

    /**
     * Extracts move from different boards. For example, if there was an original board before the move
     * and a new after some move this function understand what move was made by analysing two different boards.
     * If it's not possible to determine the move returns an empty array.
     *
     * @param   originalBoard   board before the move
     * @param   currentBoard    board after the move
     * @return                  extracted move as an array
     */
    public List<Integer> extractMove(Board originalBoard, Board currentBoard) {
        Board originalBoardCopy = originalBoard.deepCopy();
        List<Integer> emptyCells = originalBoardCopy.getEmptyCells();
        List<Integer> result = new ArrayList<>();
        int move = 0;
        int rotation = 0;
        for (Integer emptyCell : emptyCells) {
            for (int r = 0; r < 8; r++) {
                originalBoardCopy.setField(emptyCell, mark);
                originalBoardCopy.rotate(r);
                if (Arrays.equals(originalBoardCopy.getFields(), currentBoard.getFields())) {
                    move = emptyCell;
                    rotation = r;
                    result.add(move);
                    result.add(rotation);
                    return result;
                }
                originalBoardCopy.rotateBack(r);
                originalBoardCopy.setField(emptyCell, Mark.EMPTY);
            }
        }
        return result;
    }

    /**
     * Calculates the amount of iterations for MCTS algorithm depending on situation. If there are a lot of iterations
     * that means more accurate computations would be. On the other hand, more iterations, means more memory to use.
     * This function returns an optimal amount of iterations for the current board.
     * @param   emptyCellsSize  the amount of empty cells on the board
     * @return                  the optimal depth
     */
    private int dynamicIterations(int emptyCellsSize) {
        return 0;
    }

    /**
     * Calculates the depth for minimax algorithm depending on situation. If there are a lot of empty cells
     * that means a lot of possibilities which is hard to compute. On the other hand, when there are small amount of cells
     * it is preferable to compute deeper. This function returns an optimal depth level for the current board.
     *
     * @param   emptyCellsSize  the amount of empty cells on the board
     * @return                  the optimal depth
     */
    public int dynamicDepth(int emptyCellsSize) {
        if (emptyCellsSize > 15) return 1;
        return 2;
    }

}
