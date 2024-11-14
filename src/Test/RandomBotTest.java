package Test;

import Game.Board;

import Game.Mark;
import Player.BotPlayer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test that checks whether the random bot performs correct by checking if it fills up the board.
 */
public class RandomBotTest {

    private Board board;
    private BotPlayer botPlayer;

    @BeforeEach
    public void setUp() {
        board = new Board();
        botPlayer = new BotPlayer(Mark.XX);
    }

    /**
     * Checks that after the move the amount of an empty cells decrease by 1.
     */
    @Test
    public void randomMoveTest() {
        List<Integer> emptyCells = board.getEmptyCells();
        for (int i = 36; i > 0; i--) {
            assertEquals(i, emptyCells.size());
            board.setField(Integer.parseInt(botPlayer.determineMove(board)[1]), Mark.XX);
            emptyCells = board.getEmptyCells();
        }
        assertTrue(board.isFull());
    }

}
