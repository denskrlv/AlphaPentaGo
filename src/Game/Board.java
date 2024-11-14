package Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Board is the class which represents the Pentago game board. The board is represented by an array of Marks. The class
 * provides lots of fundamental methods to play the game. The client and server, strategy and the players all need methods
 * from this class.
 *
 * @author  Daan Schram
 * @author  Denis Krylov
 * @since   1.0
 */

public class Board {

    /**
     * An array of marks that represents the board.
     */
    private final Mark[] fields = new Mark[DIM * DIM];
    /**
     * An empty String delimiter for the board.
     */
    private static final String DELIM = "     ";
    /**
     * Dimension of the board.
     */
    public static final int DIM = 6;
    /**
     * A String delimiter for the board.
     */
    private static final String LINE = "----+----+----+----+----+----";
    /**
     * A numpad for the board.
     */
    private static final String[] NUMBERING =
            {" 0  | 1  | 2  | 3  | 4  | 5  ", LINE,
                    " 6  | 7  | 8  | 9  | 10 | 11 ", LINE,
                    " 12 | 13 | 14 | 15 | 16 | 17 ", LINE,
                    " 18 | 19 | 20 | 21 | 22 | 23 ", LINE,
                    " 24 | 25 | 26 | 27 | 28 | 29 ", LINE,
                    " 30 | 31 | 32 | 33 | 34 | 35 ", LINE
            };

    /**
     * Creates an empty game board of size DIM x DIM.
     */
    public Board() {
        for (int i = 0; i < DIM * DIM; i++) {
            fields[i] = Mark.EMPTY;
        }
    }

    /**
     * Creates a copy of the board. The copy should contain the same position on the board as the original one.
     *
     * @return  copy of the board. If the board is empty,
     *          returns an empty copy of the board
     */
    public Board deepCopy() {
        Board copyBoard = new Board();
        for (int i = 0; i < DIM * DIM; i++) {
            copyBoard.fields[i] = this.fields[i];
        }
        return copyBoard;
    }

    /**
     * Calculates the index of the board from a (row, col) pair.
     *
     * @param   row   row of the board
     * @param   col   column of the board
     * @return        the index belonging to the (row,col)-field
     */
    /*@
    requires row >= 0 && row < DIM;
    requires col >= 0 && row < DIM;
     */
    public int index(int row, int col) {
        return DIM * row + col;
    }

    /**
     * Returns true if index is a valid index of a field on the board.
     *
     * @param   index   index of the board
     * @return          true if 0 <= index < DIM * DIM
     */
    //@ ensures index >= 0 && index < DIM*DIM ==> \result == true;
    public boolean isField(int index) {
        return index >= 0 && index < DIM * DIM;
    }

    /**
     * Returns true of the (row,col) pair refers to a valid field on the board.
     *
     * @param   col     column of board
     * @param   row     row of board
     * @return          true if 0 <= row < DIM && 0 <= col < DIM
     */
    //@ ensures row >= 0 && row < DIM && col >= 0 && col < DIM ==> \result == true;
    public boolean isField(int row, int col) {
        return row >= 0 && row < DIM && col >= 0 && col < DIM;
    }

    /**
     * Returns current mark of the selected field.
     *
     * @param   i   the number of the field (see NUMBERING)
     * @return      the mark on the field
     */
    /*@
    requires isField(i);
    ensures \result == Mark.EMPTY || \result == Mark.OO || \result == Mark.XX;
     */
    public Mark getField(int i) {
        if (isField(i)) {
            return fields[i];
        }
        return null;
    }

    /**
     * Returns current mark of the selected field using the (row,col) pair.
     *
     * @param   row     the row of the field
     * @param   col     the column of the field
     * @return          the mark on the field
     */
    /*@
    requires isField(row, col);
    ensures \result == Mark.EMPTY || \result == Mark.OO || \result == Mark.XX;
    */
    public Mark getField(int row, int col) {
        if (isField(row, col)) {
            return fields[index(row, col)];
        }
        return null;
    }

    /**
     * Returns a board as an array of marks.
     *
     * @return      array of marks
     */
    public Mark[] getFields() {
        return this.fields;
    }

    /**
     * Returns true if the selected field is empty.
     *
     * @param   i   the index of the field (see NUMBERING)
     * @return      true if the field is empty
     */
    /*@
    requires isField(i);
    ensures getField(i) == Mark.EMPTY ==> \result == true;
    */
    public boolean isEmptyField(int i) {
        if (isField(i)) {
            return getField(i) == Mark.EMPTY;
        }
        return false;
    }

    /**
     * Returns true if the selected field referenced by the (row,col) pair is empty.
     *
     * @param   row     the row of the field
     * @param   col     the column of the field
     * @return          true if the field is empty
     */
    /*@
    requires isField(row, col);
    ensures getField(row, col) == Mark.EMPTY ==> \result == true;
     */
    public boolean isEmptyField(int row, int col) {
        if (isField(row, col)) {
            return getField(row, col) == Mark.EMPTY;
        }
        return false;
    }

    /**
     * Checks if the board is full. When the board full there is no empty space on it.
     *
     * @return      true if all fields are occupied
     */
    //@ ensures (\forall int i; (i >= 0 && i < DIM*DIM); fields[i] == Mark.XX || fields[i] == Mark.OO);
    public boolean isFull() {
        for (int i = 0; i < DIM * DIM; i++) {
            if (fields[i] == Mark.EMPTY) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the game over. The game is over when there is a winner
     * or the whole board is full.
     *
     * @return      true if the game is over
     */
    //@ ensures isFull() || hasWinner() ==> \result == true;
    public boolean gameOver() {
        return isFull() || hasWinner();
    }

    /**
     * Checks if there is a row that contains a selected number of marks in succession.
     *
     * @param   m       selected mark
     * @param   streak  the amount of marks that are the same in succession
     * @return          true if there is a column that contains more than a selected number of marks in succession
     *
     * @see     Mark
     */
    /*@
    requires m == Mark.XX || m == Mark.OO;
    requires streak > 0 && streak <= 6;
    ensures isWinner(Mark.XX) || isWinner(Mark.OO) ==> \result == true;
    */
    public boolean hasRow(Mark m, int streak) {
        int row;
        for (int i = 0; i < DIM; i++) {
            row = 0;
            for (int j = 0; j < DIM; j++) {
                if (getField(i * DIM + j) == m) {
                    row += 1;
                } else {
                    row = 0;
                }
                if (row >= streak) return true;
            }
        }
        return false;
    }

    /**
     * Checks if there is a column that contains a selected number of marks in succession.
     *
     * @param   m       selected mark
     * @param   streak  the amount of marks that are the same in succession
     * @return          true if there is a column that contains more than a selected number of marks in succession
     *
     * @see     Mark
     */
    /*@
    requires m == Mark.XX || m == Mark.OO;
    requires streak > 0 && streak <= 6;
    ensures isWinner(Mark.XX) || isWinner(Mark.OO) ==> \result == true;
    */
    public boolean hasColumn(Mark m, int streak) {
        int column;
        for (int i = 0; i < DIM; i++) {
            column = 0;
            for (int j = 0; j < DIM; j++) {
                if (getField(i + j * DIM) == m) {
                    column += 1;
                } else {
                    column = 0;
                }
                if (column >= streak) return true;
            }
        }
        return false;
    }

    /**
     * Checks if there is a diagonal that contains more than 4 selected marks in succession.
     *
     * @param   m   selected mark
     * @return      true if there is a diagonal contains more than 4 selected marks in succession
     *
     * @see     Mark
     */
    /*@
    requires m == Mark.XX || m == Mark.OO;
    requires streak > 0 && streak <= 6;
    ensures isWinner(Mark.XX) || isWinner(Mark.OO) ==> \result == true;
    */
    public boolean hasDiagonal(Mark m, int streak) {
        int diagonal = 0;
        // iterate through the main left-up to right-bottom diagonal
        for (int i = 0; i <= 36; i+=7) {
            // if the cell has required mark, add +1 to the counter
            // if not, reset the counter
            if (getField(i) == m) {
                diagonal++;
            } else {
                diagonal = 0;
            }
            // if there are 5 marks in a column, return true
            if (diagonal >= streak) {
                return true;
            }
        }
        // do the same logic but with other 5 diagonals
        diagonal = 0;
        for (int i = 1; i <= 29; i+=7) {
            if (getField(i) == m) {
                diagonal++;
            }
            if (diagonal >= streak) {
                return true;
            }
        }
        diagonal = 0;
        for (int i = 6; i <= 34; i+=7) {
            if (getField(i) == m) {
                diagonal++;
            }
            if (diagonal >= streak) {
                return true;
            }
        }
        diagonal = 0;
        for (int i = 5; i <= 30; i+=5) {
            if (getField(i) == m) {
                diagonal++;
            } else {
                diagonal = 0;
            }
            if (diagonal >= streak) {
                return true;
            }
        }
        diagonal = 0;
        for (int i = 4; i <= 24; i+=5) {
            if (getField(i) == m) {
                diagonal++;
            }
            if (diagonal >= streak) {
                return true;
            }
        }
        diagonal = 0;
        for (int i = 11; i <= 31; i+=5) {
            if (getField(i) == m) {
                diagonal++;
            }
            if (diagonal >= streak) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the selected mark has won. Player wins if player has more than 4 marks on at
     * least one row, column or diagonal.
     *
     * @param   m   selected mark
     * @return      true if the selected mark has won
     *
     * @see     Mark
     */
    /*@
    requires m == Mark.XX || m == Mark.OO;
    ensures hasRow(m, 5) || hasColumn(m, 5) || hasDiagonal(m, 5) ==> \result == true;
    */
    public boolean isWinner(Mark m) {
        return hasRow(m, 5) || hasColumn(m, 5) || hasDiagonal(m, 5);
    }

    /**
     * Checks if the game has a winner. The game has a winner if one of two players has
     * more than 4 marks on at least one row, column or diagonal.
     *
     * @return      true if the winner exists,
     *              false if there is a tie
     */
    //@ ensures isWinner(Mark.XX) || isWinner(Mark.OO) ==> \result == true;
    public boolean hasWinner() {
        return isWinner(Mark.XX) || isWinner(Mark.OO);
    }

    /**
     * Shows the board as a String. On the left side shows the current state of the board.
     * On the right side shows numbering of the fields.
     *
     * @return      the board as a String
     */
    public String toString() {
        String s = "";
        for (int i = 0; i < DIM; i++) {
            String row = "";
            for (int j = 0; j < DIM; j++) {
                row = row + " " + getField(i, j).toString().substring(0, 1).replace("E", "  ").replace("X", "X ").replace("O", "O ") + " ";
                if (j < DIM - 1) {
                    row = row + "|";
                }
            }
            s = s + row + DELIM + NUMBERING[i * 2];
            if (i < DIM - 1) {
                s = s + "\n" + LINE + DELIM + NUMBERING[i * 2 + 1] + "\n";
            }
        }
        return s;
    }

    /**
     * Empties all fields on the board.
     */
    //@ ensures (\forall int i; (i >= 0 && i < DIM*DIM); fields[i] == Mark.EMPTY);
    public void reset() {
        for (int i = 0; i < DIM*DIM; i++) {
            fields[i] = Mark.EMPTY;
        }
    }

    /**
     * Puts the selected mark on the selected field on the board.
     *
     * @param   i   the field number (see NUMBERING)
     * @param   m   the mark to be placed
     */
    /*@
    requires isField(i);
    ensures getField(i) == m;
     */
    public void setField(int i, Mark m) {
        if (isField(i)) {
            fields[i] = m;
        }
    }

    /**
     * Puts the selected mark on the selected field referenced by the (row,col) pair on the board.
     *
     * @param   row     the row of the field
     * @param   col     the column of the field
     * @param   m       selected mark
     *
     * @see     Mark
     */
    /*@
    requires isField(row, col);
    ensures getField(row, col) == m;
     */
    public void setField(int row, int col, Mark m) {
        if (isField(row, col)) {
            fields[index(row, col)] = m;
        }
    }

    /**
     * Changes indexes of the marks on the board. This method take choice as the following:
     * choice = 0 : rotate the top left sub-board counter-clockwise,
     * choice = 1 : rotate the top left sub-board clockwise,
     * choice = 2 : rotate the top right sub-board counter-clockwise,
     * choice = 3 : rotate the top right sub-board clockwise,
     * choice = 4 : rotate the bottom left sub-board counter-clockwise,
     * choice = 5 : rotate the bottom left sub-board clockwise,
     * choice = 6 : rotate the bottom right sub-board counter-clockwise,
     * choice = 7 : rotate the bottom right sub-board clockwise.
     *
     * Example: if the mark on the index 2 that means it's located on the top-left sub-board.
     * If 0 has chosen, index would change to 0. If 1 has chosen, index would change to 14.
     * If other indexed have chosen, the index would stay the same.
     *
     * @param   choice    index at which the sub-board starts
     */
    /*@
    requires 0 <= choice && choice <= 7;
    */
    public void rotate(int choice) {
        //first creates copy of the current board
        Mark[] copyList = Arrays.copyOf(fields, fields.length);
        //create empty resulting list
        Mark[] resultList = new Mark[36];
        switch (choice) {
            //index is 0 because the first subboard needs to be rotated
            case 0: resultList = rotateCounterClockWise(copyList, 0); break;
            case 1: resultList = rotateClockWise(copyList, 0); break;
            //index is 3 because the second subboard needs to be rotated and this subboard contains the same indices as the first subboard with 3 added each time
            case 2: resultList = rotateCounterClockWise(copyList, 3); break;
            case 3: resultList = rotateClockWise(copyList, 3); break;
            //index is 18 because the third subboard needs to be rotated and this subboard contains the same indices as the first subboard with 18 added each time
            case 4: resultList = rotateCounterClockWise(copyList, 18); break;
            case 5: resultList = rotateClockWise(copyList, 18); break;
            //index is 21 because the fourth subboard needs to be rotated and this subboard contains the same indices as the first subboard with 21 added each time
            case 6: resultList = rotateCounterClockWise(copyList, 21); break;
            case 7: resultList = rotateClockWise(copyList, 21); break;
        }
        //copy all elements of the resulting list to the original board
        for (int i = 0; i < DIM * DIM; i++) {
            fields[i] = resultList[i];
        }
    }

    /**
     * Does the opposite of rotate function
     *
     * @param   choice    index at which the sub-board starts
     * @see     #rotate(int)
     */
    /*@
    requires 0 <= choice && choice <= 7;
    */
    public void rotateBack(int choice) {
        //first creates copy of the current board
        Mark[] copyList = Arrays.copyOf(fields, fields.length);
        //create empty resulting list
        Mark[] resultList = new Mark[36];
        switch (choice) {
            //index is 0 because the first subboard needs to be rotated
            case 0: resultList = rotateClockWise(copyList, 0); break;
            case 1: resultList = rotateCounterClockWise(copyList, 0); break;
            //index is 3 because the second subboard needs to be rotated and this subboard contains the same indices as the first subboard with 3 added each time
            case 2: resultList = rotateClockWise(copyList, 3); break;
            case 3: resultList = rotateCounterClockWise(copyList, 3); break;
            //index is 18 because the third subboard needs to be rotated and this subboard contains the same indices as the first subboard with 18 added each time
            case 4: resultList = rotateClockWise(copyList, 18); break;
            case 5: resultList = rotateCounterClockWise(copyList, 18); break;
            //index is 21 because the fourth subboard needs to be rotated and this subboard contains the same indices as the first subboard with 21 added each time
            case 6: resultList = rotateClockWise(copyList, 21); break;
            case 7: resultList = rotateCounterClockWise(copyList, 21); break;
        }
        //copy all elements of the resulting list to the original board
        for (int i = 0; i < DIM * DIM; i++) {
            fields[i] = resultList[i];
        }
    }

    /**
     * Rotates chosen by index sub-board clockwise.
     *
     * @param   copyList    copy of the current board
     * @param   index       index at which sub-board starts
     * @return              the new list in which the sub-board is rotated
     *
     * @see     Mark
     */
    /*@
    requires 0 <= index && index <=3;
    */
    private Mark[] rotateClockWise(Mark[] copyList, int index) {
        Mark[] resultList = Arrays.copyOf(copyList, copyList.length);
        //hardcode of rotation of only the first subboard, by using index all other subboard can be rotated as well
        resultList[index] = copyList[12+index];
        resultList[1+index] = copyList[6+index];
        resultList[2+index] = copyList[index];
        resultList[6+index] = copyList[13+index];
        resultList[7+index] = copyList[7+index];
        resultList[8+index] = copyList[1+index];
        resultList[12+index] = copyList[14+index];
        resultList[13+index] = copyList[8+index];
        resultList[14+index] = copyList[2+index];
        return resultList;
    }

    /**
     * Rotates chosen by index sub-board counter-clockwise.
     *
     * @param   copyList    copy of the current board
     * @param   index       index at which sub-board starts
     * @return              the new list in which the sub-board is rotated
     *
     * @see     Mark
     */
    /*@
    requires 0 <= index && index <=3;
    */
    private Mark[] rotateCounterClockWise(Mark[] copyList, int index) {
        Mark[] resultList = Arrays.copyOf(copyList, copyList.length);
        //hardcode of rotation of only the first subboard, by using index all other subboard can be rotated as well
        resultList[index] = copyList[2+index];
        resultList[1+index] = copyList[8+index];
        resultList[2+index] = copyList[14+index];
        resultList[6+index] = copyList[1+index];
        resultList[7+index] = copyList[7+index];
        resultList[8+index] = copyList[13+index];
        resultList[12+index] = copyList[index];
        resultList[13+index] = copyList[6+index];
        resultList[14+index] = copyList[12+index];
        return resultList;
    }

    /**
     * Returns a list with all the indices of the board that are empty.
     *
     * @return          list of integer with indices of all empty cells
     *
     * @see             Board
     */
    //@ensures (\forall int i; isEmptyField(i) == true);
    public List<Integer> getEmptyCells() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < DIM * DIM; i++) {
            if (getField(i) == Mark.EMPTY) list.add(i);
        }
        return list;
    }

}
