package Strategy;

import Game.Board;
import Game.Mark;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents the RandomBot AI bot that plays the game. RandomBot is a bot that simulates a random move.
 *
 * <b>Note:</b> <code>RandomBot</code> class implements <code>Strategy</code> interface.
 *
 * @author  Daan Schram
 * @since   1.0
 * @see     Strategy
 * @see     Board
 * @see     Mark
 */
public class RandomBot implements Strategy{

    /**
     * Returns the name of the strategy as a String.
     *
     * @return      name of strategy
     */
    @Override
    public String getName() {
        return "Naive Computer";
    }

    /**
     * Simulates a random valid move on the board.
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
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < board.DIM * board.DIM; i++) {
            if (board.getField(i) == Mark.EMPTY) {
                list.add(i);
            }
        }
        Random rand = new Random();
        String[] result = new String[3];
        result[0] = "MOVE";
        result[1] = list.get(rand.nextInt(list.size())) + "";
        result[2] = rand.nextInt(7) + "";
        System.out.println(result[0] + "~" + result[1] + "~" + result[2]);
        return result;
    }

}
