package Strategy;

import Game.Board;
import Game.Mark;

/**
 * Represents the AlphaPentaGo AI bot that plays the game. AlphaPentaGo is Artificial Intelligence that
 * plays the game using Monte Carlo Tree Search combined with Minimax to find the best possible move to play.
 *
 * <b>Note:</b> <code>AlphaPentaGo</code> class implements <code>Strategy</code> interface.
 *
 * @author  Denis Krylov
 * @since   1.0
 * @see     Strategy
 * @see     Board
 * @see     Mark
 * @see     DecisionEngine
 */
public class AlphaPentaGo implements Strategy {

    private int level;

    public AlphaPentaGo(int level) {
        this.level = level;
    }

    /**
     * Returns the name of the strategy as a String.
     *
     * @return      name of strategy
     */
    @Override
    public String getName() {
        return "AlphaPentaGo";
    }

    /**
     * Determines a well-chosen valid move on the board using <code>DecisionEngine</code>.
     * Returns a list which has the following structure:
     * <ul>
     *     <li>index 0 : MOVE command</li>
     *     <li>index 1 : index to put a mark</li>
     *     <li>index 2 : index to rotate the board</li>
     * </ul>
     *
     * @param   board   the current board of the game
     * @param   mark    the mark of the current player
     * @return          list of type String
     */
    @Override
    public String[] determineMove(Board board, Mark mark) {
        DecisionEngine de = new DecisionEngine(board, mark);
        return de.compute(level);
    }
}
