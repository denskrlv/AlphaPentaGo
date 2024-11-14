package Strategy.Algorithms;

import Game.Board;
import Game.Mark;

import java.util.ArrayList;
import java.util.List;

/**
 * A minimax algorithm that can hardly compute the best possible move by brute forcing all possible combinations.
 * Not suitable for the game but useful to find traps that opponent can implement. Also, can be used to find tricky
 * winning moves that MCTS might not recognize.
 * <p>
 * The <code>Minimax</code> contains two methods to find best move in a certain depth.
 * <p>
 * The <code>Minimax</code> contains one method to calculate the score for the particular combination.
 *
 * @author  Denis Krylov
 * @since   2.0
 * @see     MonteCarloTreeSearch
 * @see     Board
 * @see     Mark
 */
public class Minimax {

    private int depth;

    public Minimax(int depth) {
        this.depth = depth;
    }

    /**
     * Returns the best possible move on the current board and selected mark. Represents the
     * recursive tree with selected depth that plays all possible combinations <em>N</em> moves forward
     * (<em>N</em> equals depth). Because this function was optimised to work together with MCTS, it returns
     * a winning move or, if the move wasn't found, an empty array.
     *
     * @param   board   current board
     * @param   mark    selected mark
     * @return          winning move or an empty array
     */
    public int findNextMove(Board board, Mark mark) {
        int move = 0;
        int rotation = 0;
        int score = 0;
        int bestScore = Integer.MIN_VALUE;
        List<Integer> result = new ArrayList<>();
        List<Integer> emptyCells = board.getEmptyCells();

        for (Integer emptyCell : emptyCells) {
            for (int r = 0; r < 8; r++) {
                board.setField(emptyCell, mark);
                score = minimax(board, mark.other(), r, depth-1, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
                board.rotateBack(r);
                board.setField(emptyCell, Mark.EMPTY);
                if (score > bestScore) {
                    bestScore = score;
                    move = emptyCell;
                    rotation = r;
                }
            }
        }
        return score;
    }

    /**
     * A minimax function itself. It puts a player's mark if <code>maximizingMark</code> is true and opponent mark if
     * <code>maximizingMark</code> is false in all possible sells with all possible rotations.
     * <p>
     * If depth for particular combination is 0 tries to evaluate the score by calling <code>calculateScore</code>
     * function. If the player is winner returns positive score, if opponent is winner returns a negative score.
     * <p>
     * If depth is not 0, change the value of <code>maximizingMark</code> to opposite and perform the same
     * operations simulating the game from the opponent's side.
     *
     * @param   board           current board
     * @param   mark            selected mark
     * @param   rotation        selected rotation
     * @param   depth           computational depth
     * @param   alpha           best evaluation for the player
     * @param   beta            best evaluation for the opponent
     * @param   maximizingMark  current player to compute (opponent if false)
     * @return                  score of the move
     */
    private int minimax(Board board, Mark mark, int rotation, int depth, int alpha, int beta, boolean maximizingMark) {

        int score1 = calculateScore(board, mark.other());
        board.rotate(rotation);
        int score2 = calculateScore(board, mark.other());
        if (board.hasWinner() || depth == 0) return Math.max(score1, score2);

        if (maximizingMark) {
            int maxEval = Integer.MIN_VALUE;
            List<Integer> emptyCells = board.getEmptyCells();
            if (emptyCells.isEmpty()) return 0;
            for (Integer emptyCell : emptyCells) {
                for (int r = 0; r < 8; r++) {
                    board.setField(emptyCell, mark);
                    int eval = minimax(board, mark.other(), r, depth - 1, alpha, beta, false);
                    board.rotateBack(r);
                    board.setField(emptyCell, Mark.EMPTY);
                    maxEval = Math.max(alpha, eval);
                    alpha = Math.max(alpha, eval);
                    if (beta <= alpha) break;
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            List<Integer> emptyCells = board.getEmptyCells();
            if (emptyCells.isEmpty()) return 0;
            for (Integer emptyCell : emptyCells) {
                for (int r = 0; r < 8; r++) {
                    board.setField(emptyCell, mark);
                    int eval = -1 * minimax(board, mark.other(), r, depth - 1, alpha, beta, true);
                    board.rotateBack(r);
                    board.setField(emptyCell, Mark.EMPTY);
                    minEval = Math.min(minEval, eval);
                    beta = Math.min(beta, eval);
                    if (beta <= alpha) break;
                }
            }
            return minEval;
        }
    }

    /**
     * Computes the score of the position as following:
     * <ul>
     *     <li>if the player is winner : +1</li>
     *     <li>if the opponent is winner : -1</li>
     *     <li>if there is a tie : 0</li>
     * </ul>
     *
     * @param   board   current board
     * @param   mark    selected mark
     * @return          score
     */
    private int calculateScore(Board board, Mark mark) {
        if (board.isWinner(mark)) return 1;
        return 0;
    }

}
