package Strategy;

import Game.Board;
import Game.Mark;

/**
 * A strategy for the bot player in the game. The user of this interface has precise control over what strategy
 * and logic to use for the game bot. The user can access the strategy name that would be useful in
 * <code>BotPlayer</code>.
 * <p>
 * The <code>Strategy</code> interface provides one method to get the information about the strategy.
 * <p>
 * The <code>Strategy</code> interface provides one method to determine next move.
 *
 * @author  Daan Schram
 * @author  Denis Krylov
 * @since   1.0
 * @see     Player.BotPlayer
 * @see     Board
 * @see     Mark
 */
public interface Strategy {

    /**
     * Returns the name of the strategy as a String.
     *
     * @return      name of strategy
     */
    public String getName();

    /**
     * Returns a next move as a list which has the following structure:
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
    public String[] determineMove(Board board, Mark mark);

}
