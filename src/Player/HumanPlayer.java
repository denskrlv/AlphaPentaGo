package Player;

import Game.Board;
import Game.Mark;

import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Extends the AbstractPlayer class, implements the determineMove method by retrieving an input from the user.
 */
public class HumanPlayer extends AbstractPlayer{

    private Scanner scanner = new Scanner(new InputStreamReader(System.in));

    /**
     * Creates a new Human player object.
     *
     * @param   name    name of player
     * @param   mark    mark of player
     */
    /*
    @requires name != null;
    @requires mark == Mark.XX || mark == Mark.OO;
    */
    public HumanPlayer(String name, Mark mark) {
        super(name, mark);
    }

    /**
     * Returns a list which has the following structure:
     * index 0 : MOVE command,
     * index 1 : move index,
     * index 2 : rotation index
     *
     * @param   board   Board object
     * @return          list of type String
     */
    /*@ requires board != null;
        ensures board.isField() && board.getField(\result) == Mark.EMPTY;
    @*/
    public String[] determineMove(Board board) {
        String prompt = "> " + getName() + " (" + getMark().toString() + ")"
                + ", what is your choice? ";

        System.out.println(prompt);
        String stringChoice = scanner.nextLine();
        String[] splittedChoice = stringChoice.split("~");

        String command = splittedChoice[0];
        int choice = Integer.parseInt(splittedChoice[1]);
        int rotation = Integer.parseInt(splittedChoice[2]);

        boolean valid = rotation >= 0 && rotation < 8 && command.equals("MOVE") && board.isField(choice) && board.isEmptyField(choice);
        while (!valid) {
            System.out.println("ERROR: " + stringChoice
                    + " is no valid choice.");
            System.out.println(prompt);
            stringChoice = scanner.nextLine();
            splittedChoice = stringChoice.split("~");

            command = splittedChoice[0];
            choice = Integer.parseInt(splittedChoice[1]);
            rotation = Integer.parseInt(splittedChoice[2]);
            valid = rotation >= 0 && rotation < 8 && command.equals("MOVE") && board.isField(choice) && board.isEmptyField(choice);
        }
        return splittedChoice;
    }

}
