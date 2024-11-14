package Game;

import Player.AbstractPlayer;

import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Game is the class that can simulate a game of Pentago on your local machine. This process is split up into several
 * methods like start, update and reset.
 */
public class Game {

    public static final int NUMBER_PLAYERS = 2;
    private Board board;

    private AbstractPlayer[] players;
    private int current;
    private Scanner scanner = new Scanner(new InputStreamReader(System.in));

    //@ private invariant board != null;
    //@invariant players.length == NUMBER_PLAYERS;
    //@invariant (\forall int i; (i >= 0 && i < NUMBER_PLAYERS); players[i] != null);

    /**
     * Creates a new Game object.
     *
     * @param   s0  the first player
     * @param   s1  the second player
     */
    //@ requires s0 != null && s1 != null;
    public Game(AbstractPlayer s0, AbstractPlayer s1) {
        board = new Board();
        players = new AbstractPlayer[NUMBER_PLAYERS];
        players[0] = s0;
        players[1] = s1;
        current = 0;
    }

    /**
     * Starts the game.
     * Asks after each ended game if the user want to continue. Continues until
     * the user does not want to play anymore.
     */
    public void start() {
        String continueGameString;
        boolean continueGame = true;
        while (continueGame) {
            reset();
            play();
            System.out.println("\n> Play another time? (y/n)?");
            continueGameString = scanner.next();
            if (continueGameString.equals("y")) {
                continueGame = true;
            } else {
                continueGame = false;
            }
        }
    }

    /**
     * Resets the game.
     * The board is emptied and player[0] becomes the current player.
     */
    private void reset() {
        current = 0;
        board.reset();
    }

    /**
     * Plays the game.
     * First the (still empty) board is shown. Then the game is played
     * until it is over. Players can make a move one after the other.
     * After each move, the changed game situation is printed.
     * Whether a player has won is checker before the rotation, as specified in the manual.
     */
    private void play() {
        update();
        while (!board.gameOver()) {
            int rotation = players[current].makeMove(board);
            if (!board.gameOver()) {
                board.rotate(rotation);
                update();
                if (current == 0) {
                    current = 1;
                } else if (current == 1) {
                    current = 0;
                } else {
                    System.out.println("Error occurred: wrong player");
                }
            } else {
                update();
            }
        }
        printResult();
    }

    /**
     * Prints the game state.
     */
    private void update() {
        System.out.println("\nCurrent game situation: \n\n" + board.toString()
                + "\n");
    }

    /**
     * Prints the result of the last game.
     */
    //@ requires board.hasWinner() || board.isFull();
    private void printResult() {
        if (board.hasWinner()) {
            AbstractPlayer winner = board.isWinner(players[0].getMark()) ? players[0]
                    : players[1];
            System.out.println("Player " + winner.getName() + " ("
                    + winner.getMark().toString() + ") has won!");
        } else {
            System.out.println("Draw. There is no winner!");
        }
    }

}
