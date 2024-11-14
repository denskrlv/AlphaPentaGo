package Test;

import Game.Board;

import Game.Mark;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test that checks whether the board performs like it should. All methods are systematically covered.
 */
public class BoardTest {

    private Board board;

    /**
     * Initializes board
     */
    @BeforeEach
    public void setUp() {
        board = new Board();
    }

    /**
     * Test whether index method works
     */
    @Test
    public void testIndex() {
        int index = 0;
        for (int i = 0; i < Board.DIM; i++) {
            for (int j = 0; j < Board.DIM; j++) {
                assertEquals(index, board.index(i, j));
                index += 1;
            }
        }
    }

    /**
     * Test whether isField method works by using index
     */
    @Test
    public void testIsFieldIndex() {
        assertFalse(board.isField(-1));
        assertTrue(board.isField(0));
        assertTrue(board.isField(Board.DIM * Board.DIM - 1));
        assertFalse(board.isField(Board.DIM * Board.DIM));
    }

    /**
     * Test whether isField method works by uwing row and col
     */
    @Test
    public void testIsFieldRowCol() {
        assertFalse(board.isField(-1, 0));
        assertFalse(board.isField(0, -1));
        assertTrue(board.isField(0, 0));
        assertTrue(board.isField(5, 5));
        assertFalse(board.isField(5, 6));
        assertFalse(board.isField(6, 5));
    }

    /**
     * Test whether setField and getField methods work by using index
     */
    @Test
    public void testSetAndGetFieldIndex() {
        board.setField(0, Mark.XX);
        assertEquals(Mark.XX, board.getField(0));
        assertEquals(Mark.EMPTY, board.getField(1));
    }

    /**
     * Test whether setField and getField methods work by using row and col
     */
    @Test
    public void testSetAndGetFieldRowCol() {
        board.setField(0, 0, Mark.XX);
        assertEquals(Mark.XX, board.getField(0, 0));
        assertEquals(Mark.EMPTY, board.getField(0, 1));
        assertEquals(Mark.EMPTY, board.getField(1, 0));
        assertEquals(Mark.EMPTY, board.getField(1, 1));
    }

    /**
     * Test whether board initializes to all empty fields
     */
    @Test
    public void testSetup() {
        for (int i = 0; i < Board.DIM * Board.DIM; i++) {
            assertEquals(Mark.EMPTY, board.getField(i));
        }
    }

    /**
     * Test whether reset method works
     */
    @Test
    public void testReset() {
        board.setField(0, Mark.XX);
        board.setField(Board.DIM * Board.DIM - 1, Mark.OO);
        board.reset();
        assertEquals(Mark.EMPTY, board.getField(0));
        assertEquals(Mark.EMPTY, board.getField(Board.DIM * Board.DIM - 1));
    }

    /**
     * Test whether deepCopy method works
     */
    @Test
    public void testDeepCopy() {
        board.setField(0, Mark.XX);
        board.setField(Board.DIM * Board.DIM - 1, Mark.OO);
        Board deepCopyBoard = board.deepCopy();

        // First test if all the fields are the same
        for (int i = 0; i < Board.DIM * Board.DIM; i++) {
            assertEquals(board.getField(i), deepCopyBoard.getField(i));
        }

        // Check if a field in the deepcopied board the original remains the same
        deepCopyBoard.setField(0, Mark.OO);

        assertEquals(Mark.XX, board.getField(0));
        assertEquals(Mark.OO, deepCopyBoard.getField(0));
    }

    /**
     * Test whether isEmptyField method works by using index
     */
    @Test
    public void testIsEmptyFieldIndex() {
        board.setField(0, Mark.XX);
        assertFalse(board.isEmptyField(0));
        assertTrue(board.isEmptyField(1));
    }

    /**
     * Test whether isEmptyField method works by using row and col
     */
    @Test
    public void testIsEmptyFieldRowCol() {
        board.setField(0, 0, Mark.XX);
        assertFalse(board.isEmptyField(0, 0));
        assertTrue(board.isEmptyField(0, 1));
        assertTrue(board.isEmptyField(1, 0));
    }

    /**
     * Test whether isFull method works
     */
    @Test
    public void testIsFull() {
        for (int i = 0; i < Board.DIM * Board.DIM - 1; i++) {
            board.setField(i, Mark.XX);
        }
        assertFalse(board.isFull());

        board.setField(Board.DIM * Board.DIM - 1, Mark.XX);
        assertTrue(board.isFull());
    }
    // helllo

    /**
     * Test whether there can be no winner while the game is over
     */
    @Test
    public void testGameOverAndHasNoWinnerFullBoard() {
        for (int i = 0; i < board.DIM; i+=2) {
            for (int j = 0; j < board.DIM; j++) {
                if (j < 3) {
                    board.setField(i, j, Mark.XX);
                } else {
                    board.setField(i, j, Mark.OO);
                }
            }
        }
        for (int i = 1; i < board.DIM; i+=2) {
            for (int j = 0; j < board.DIM; j++) {
                if (j < 3) {
                    board.setField(i, j, Mark.OO);
                } else {
                    board.setField(i, j, Mark.XX);
                }
            }
        }

        assertTrue(board.gameOver());
        assertFalse(board.hasWinner());
    }

    /**
     * Test whether there is a row of 5 marks
     */
    @Test
    public void testHasRow() {
        board.setField(0, 0, Mark.XX);
        board.setField(0, 1, Mark.XX);
        board.setField(0, 2, Mark.XX);
        board.setField(0, 3, Mark.XX);
        assertFalse(board.hasRow(Mark.XX, 5));
        assertFalse(board.hasRow(Mark.OO, 5));

        board.setField(0, 4, Mark.XX);
        assertTrue(board.hasRow(Mark.XX, 5));
        assertFalse(board.hasRow(Mark.OO, 5));

        board.reset();

        board.setField(5, 0, Mark.OO);
        board.setField(5, 1, Mark.OO);
        board.setField(5, 2, Mark.OO);
        board.setField(5, 3, Mark.OO);
        assertFalse(board.hasRow(Mark.OO, 5));
        assertFalse(board.hasRow(Mark.XX, 5));

        board.setField(5, 4, Mark.OO);
        assertTrue(board.hasRow(Mark.OO, 5));
        assertFalse(board.hasRow(Mark.XX, 5));
    }

    /**
     * Test whether there is a column of 5 marks
     */
    @Test
    public void testHasColumn() {
        board.setField(0, 0, Mark.XX);
        board.setField(1, 0, Mark.XX);
        board.setField(2, 0, Mark.XX);
        board.setField(3, 0, Mark.XX);
        assertFalse(board.hasColumn(Mark.XX, 5));
        assertFalse(board.hasColumn(Mark.OO, 5));

        board.setField(4, 0, Mark.XX);
        assertTrue(board.hasColumn(Mark.XX, 5));
        assertFalse(board.hasColumn(Mark.OO, 5));

        board.reset();

        board.setField(0, 2, Mark.OO);
        board.setField(1, 2, Mark.OO);
        board.setField(2, 2, Mark.OO);
        board.setField(3, 2, Mark.OO);
        assertFalse(board.hasColumn(Mark.XX, 5));
        assertFalse(board.hasColumn(Mark.OO, 5));

        board.setField(4, 2, Mark.OO);
        assertTrue(board.hasColumn(Mark.OO, 5));
        assertFalse(board.hasColumn(Mark.XX, 5));
    }

    /**
     * Test whether there is a diagonal of 5 marks, downward
     */
    @Test
    public void testHasDiagonalDown() {
        board.setField(0, 0, Mark.XX);
        board.setField(1, 1, Mark.XX);
        board.setField(2, 2, Mark.XX);
        board.setField(3, 3, Mark.XX);
        assertFalse(board.hasDiagonal(Mark.XX, 5));
        assertFalse(board.hasDiagonal(Mark.OO, 5));

        board.setField(4, 4, Mark.XX);
        assertTrue(board.hasDiagonal(Mark.XX, 5));
        assertFalse(board.hasDiagonal(Mark.OO, 5));
    }

    /**
     * Test whether there is a diagonal of 5 marks, upward
     */
    @Test
    public void testHasDiagonalUp() {
        board.setField(0, 5, Mark.XX);
        board.setField(1, 4, Mark.XX);
        board.setField(2, 3, Mark.XX);
        board.setField(3, 2, Mark.XX);
        assertFalse(board.hasDiagonal(Mark.XX, 5));
        assertFalse(board.hasDiagonal(Mark.OO, 5));

        board.setField(4, 1, Mark.XX);
        assertTrue(board.hasDiagonal(Mark.XX, 5));
        assertFalse(board.hasDiagonal(Mark.OO, 5));
    }

    /**
     * Test whether winner is chosen correctly
     */
    @Test
    public void testIsWinner() {
        board.setField(0, Mark.XX);
        board.setField(1, Mark.XX);
        board.setField(2, Mark.XX);
        board.setField(3, Mark.XX);
        assertFalse(board.isWinner(Mark.XX));
        assertFalse(board.isWinner(Mark.OO));

        board.setField(4, Mark.XX);
        assertTrue(board.isWinner(Mark.XX));
        assertFalse(board.isWinner(Mark.OO));

        board.setField(0, 0, Mark.OO);
        board.setField(1, 1, Mark.OO);
        board.setField(2, 2, Mark.OO);
        board.setField(3, 3, Mark.OO);
        assertFalse(board.isWinner(Mark.XX));
        assertFalse(board.isWinner(Mark.OO));

        board.setField(4, 4, Mark.OO);
        assertFalse(board.isWinner(Mark.XX));
        assertTrue(board.isWinner(Mark.OO));
    }

    /**
     * Test whether there is a winner when there is a row of 5 marks
     */
    @Test
    public void testHasWinnerRow() {
        board.setField(1, Mark.XX);
        board.setField(2, Mark.XX);
        board.setField(3, Mark.XX);
        assertFalse(board.hasWinner());

        board.setField(0, Mark.XX);
        board.setField(4, Mark.XX);
        assertTrue(board.hasWinner());

        board.setField(5, Mark.XX);
        assertTrue(board.hasWinner());

        board.reset();

        board.setField(18, Mark.XX);
        board.setField(19, Mark.XX);
        board.setField(23, Mark.XX);
        assertFalse(board.hasWinner());

        board.setField(22, Mark.XX);
        assertFalse(board.hasWinner());

        board.setField(20, Mark.XX);
        assertFalse(board.hasWinner());

        board.setField(21, Mark.XX);
        assertTrue(board.hasWinner());
    }

    /**
     * Test whether there is a winner when there is a column of 5 marks
     */
    @Test
    public void testHasWinnerColumn() {
        board.setField(1, Mark.XX);
        board.setField(7, Mark.XX);
        board.setField(13, Mark.XX);
        assertFalse(board.hasWinner());

        board.setField(19, Mark.XX);
        board.setField(25, Mark.XX);
        assertTrue(board.hasWinner());

        board.setField(31, Mark.XX);
        assertTrue(board.hasWinner());

        board.reset();

        board.setField(5, Mark.XX);
        board.setField(11, Mark.XX);
        board.setField(35, Mark.XX);
        assertFalse(board.hasWinner());

        board.setField(29, Mark.XX);
        assertFalse(board.hasWinner());

        board.setField(23, Mark.XX);
        assertFalse(board.hasWinner());

        board.setField(17, Mark.XX);
        assertTrue(board.hasWinner());
    }

    /**
     * Test whether there is a winner when there is a diagonal of 5 marks
     */
    @Test
    public void testHasWinnerDiagonal() {
        board.setField(1, 1, Mark.XX);
        board.setField(2, 2, Mark.XX);
        board.setField(3, 3, Mark.XX);
        board.setField(4, 4, Mark.XX);
        assertFalse(board.hasWinner());

        board.setField(5, 5, Mark.XX);
        assertTrue(board.hasWinner());

        board.reset();

        board.setField(0, 5, Mark.XX);
        board.setField(1, 4, Mark.XX);
        board.setField(2, 3, Mark.XX);
        board.setField(3, 2, Mark.XX);
        assertFalse(board.hasWinner());

        board.setField(4, 1, Mark.XX);
        assertTrue(board.hasWinner());
    }

    /**
     * Test whether rotation method works in a clockwise way
     */
    @Test
    public void testRotationClockWise() {
        board.setField(1, 1, Mark.XX);
        board.setField(0, 0, Mark.XX);
        board.rotate(1);
        board.setField(0, 4, Mark.XX);
        board.setField(1, 5, Mark.XX);
        board.rotate(3);
        board.setField(5, 0, Mark.XX);
        board.setField(5, 1, Mark.XX);
        board.rotate(5);
        board.setField(5, 5, Mark.XX);
        board.setField(4, 5, Mark.XX);
        board.rotate(7);
        assertEquals(Mark.XX, board.getField(1, 1));
        assertEquals(Mark.XX, board.getField(0, 2));
        assertNotEquals(Mark.EMPTY, board.getField(1, 1));
        assertEquals(Mark.EMPTY, board.getField(0, 0));
        assertEquals(Mark.XX, board.getField(1, 5));
        assertEquals(Mark.XX, board.getField(2, 4));
        assertEquals(Mark.EMPTY, board.getField(0, 4));
        assertNotEquals(Mark.EMPTY, board.getField(1, 5));
        assertEquals(Mark.XX, board.getField(3, 0));
        assertEquals(Mark.XX, board.getField(4, 0));
        assertEquals(Mark.EMPTY, board.getField(5, 0));
        assertEquals(Mark.EMPTY, board.getField(5, 1));
        assertEquals(Mark.XX, board.getField(5, 3));
        assertEquals(Mark.XX, board.getField(5, 4));
        assertEquals(Mark.EMPTY, board.getField(5, 5));
        assertEquals(Mark.EMPTY, board.getField(4, 5));
    }

    /**
     * Test whether rotation method works in a counterclockwise way
     */
    @Test
    public void testRotationCounterClockWise() {
        board.setField(1, 1, Mark.XX);
        board.setField(0, 0, Mark.XX);
        board.rotate(0);
        board.setField(0, 4, Mark.XX);
        board.setField(1, 5, Mark.XX);
        board.rotate(2);
        board.setField(5, 0, Mark.XX);
        board.setField(5, 1, Mark.XX);
        board.rotate(4);
        board.setField(5, 5, Mark.XX);
        board.setField(4, 5, Mark.XX);
        board.rotate(6);
        assertEquals(Mark.EMPTY, board.getField(0, 0));
        assertNotEquals(Mark.EMPTY, board.getField(1, 1));
        assertEquals(Mark.XX, board.getField(1, 1));
        assertEquals(Mark.XX, board.getField(2, 0));
        assertNotEquals(Mark.EMPTY, board.getField(0, 4));
        assertEquals(Mark.EMPTY, board.getField(1, 5));
        assertEquals(Mark.XX, board.getField(1, 3));
        assertEquals(Mark.XX, board.getField(0, 4));
        assertEquals(Mark.EMPTY, board.getField(5, 0));
        assertEquals(Mark.EMPTY, board.getField(5, 1));
        assertEquals(Mark.XX, board.getField(5, 2));
        assertEquals(Mark.XX, board.getField(4, 2));
        assertEquals(Mark.EMPTY, board.getField(5, 5));
        assertEquals(Mark.EMPTY, board.getField(4, 5));
        assertEquals(Mark.XX, board.getField(3, 5));
        assertEquals(Mark.XX, board.getField(3, 4));
    }
}
