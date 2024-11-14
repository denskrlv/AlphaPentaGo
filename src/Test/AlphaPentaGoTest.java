package Test;

import Game.Board;
import Game.Mark;
import Player.BotPlayer;
import Strategy.AlphaPentaGo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests AlphaPentaGo decisions in different game situations.
 */
public class AlphaPentaGoTest {

    private Board board;
    private BotPlayer botPlayer;

    @BeforeEach
    public void setUp() {
        board = new Board();
        botPlayer = new BotPlayer(Mark.XX, new AlphaPentaGo(5));
    }

    @Test
    public void testGetEmptyCells() {
        List<Integer> oldEmptyCells = board.getEmptyCells();
        board.setField(0, Mark.XX);
        List<Integer> emptyCells = board.getEmptyCells();
        assertTrue(oldEmptyCells.size() > emptyCells.size());
        board.setField(10, Mark.XX);
        board.setField(35, Mark.XX);
        List<Integer> newEmptyCells = board.getEmptyCells();
        assertNotEquals(newEmptyCells.get(newEmptyCells.size()-1), 35);
        assertTrue(oldEmptyCells.size() - newEmptyCells.size() == 3);
        assertTrue(emptyCells.size() - newEmptyCells.size() == 2);
    }


    @Test
    public void findBestMoveThree() {
        board.setField(12, Mark.XX);
        board.setField(13, Mark.XX);
        board.setField(14, Mark.XX);
        board.setField(20, Mark.XX);
        board.setField(26, Mark.XX);
        board.setField(18, Mark.OO);
        board.setField(19, Mark.OO);
        board.setField(17, Mark.OO);
        board.setField(33, Mark.OO);
        board.setField(34, Mark.OO);
        String[] move = botPlayer.determineMove(board);
        assertEquals(0, Integer.parseInt(move[2]));
    }

    @Test
    public void findBestMoveFour() {
        board.setField(0, Mark.OO);
        board.setField(7, Mark.OO);
        board.setField(14, Mark.OO);
        board.setField(21, Mark.OO);
        String[] move = botPlayer.determineMove(board);
        assertEquals(28, Integer.parseInt(move[1]));
    }

    @Test
    public void findBestMoveFive() {
        board.setField(0, Mark.XX);
        board.setField(7, Mark.XX);
        board.setField(14, Mark.XX);
        board.setField(21, Mark.XX);
        String[] move = botPlayer.determineMove(board);
        assertEquals(28, Integer.parseInt(move[1]));
    }

    @Test
    public void findBestMoveSix() {
        board.setField(1, Mark.OO);
        board.setField(15, Mark.OO);
        board.setField(22, Mark.OO);
        board.setField(29, Mark.OO);
        board.setField(0, Mark.XX);
        board.setField(6, Mark.XX);
        board.setField(12, Mark.XX);
        String[] move = botPlayer.determineMove(board);
        assertEquals(8, Integer.parseInt(move[1]));
    }

    @Test
    public void findBestMoveEight() {
        board.setField(7, Mark.XX);
        board.setField(14, Mark.XX);
        board.setField(21, Mark.XX);
        board.setField(28, Mark.XX);
        board.setField(4, Mark.OO);
        board.setField(16, Mark.OO);
        board.setField(27, Mark.OO);
        board.setField(34, Mark.OO);
        board.setField(6, Mark.OO);
        String[] move = botPlayer.determineMove(board);
        assertTrue(Integer.parseInt(move[1]) == 0 || Integer.parseInt(move[1]) == 35);
    }


}
