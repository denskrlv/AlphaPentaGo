package Player;

import Game.Board;
import Game.Mark;

/**
 * Abstract class for the HumanPlayer and BotPlayer classes.
 */
public abstract class AbstractPlayer {

    private String name;
    private Mark mark;

    /**
     * Creates a new Player object.
     * @param   name    name of player
     * @param   mark    mark of player
     */
    /*@ requires name != null;
        requires mark == Mark.XX || mark == Mark.OO;
        ensures this.name == name;
        ensures this.mark == mark;
    @*/
    public AbstractPlayer(String name, Mark mark) {
        this.name = name;
        this.mark = mark;
    }

    /**
     * Returns the name of the player.
     *
     * @return  name of player
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the mark of the player.
     *
     * @return  mark of player
     */
    public Mark getMark() {
        return mark;
    }

    /**
     * Determines the field for the next move.
     *
     * @param   board     the current game board
     * @return            the player's choice
     */
    /*@ requires board != null && board.isFull() == false;
        ensures board.isField(\result) && board.getField(\result) == Mark.EMPTY;
    @*/
    public abstract String[] determineMove(Board board);

    /**
     * Makes a move on the board.
     *
     * @param   board   the current board
     * @return          rotation of choice
     */
    //@ requires board != null && board.isFull() == false;
    public int makeMove(Board board) {
        String[] splittedChoice = determineMove(board);
        int choice = Integer.parseInt(splittedChoice[1]);
        int rotation = Integer.parseInt(splittedChoice[2]);
        board.setField(choice, getMark());
        return rotation;
    }

}
