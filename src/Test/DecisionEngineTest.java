package Test;

import Game.Board;
import Game.Mark;
import Player.BotPlayer;
import Strategy.AlphaPentaGo;
import Strategy.DecisionEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests extractMove function in the Decision Engine to see if Decision Engine can understand the move from 2 different
 * board positions.
 */
public class DecisionEngineTest {

    private Board board;
    private BotPlayer botPlayer;
    private DecisionEngine de;

    @BeforeEach
    public void setUp() {
        board = new Board();
        de = new DecisionEngine(board, Mark.XX);
        botPlayer = new BotPlayer(Mark.XX, new AlphaPentaGo(5));
    }

    @Test
    public void testExtractMove() {
        List<Integer> extractedMove;
        Board copyBoard = board.deepCopy();
        copyBoard.setField(7, Mark.XX);
        extractedMove = de.extractMove(board, copyBoard);
        assertEquals(7, extractedMove.get(0));
        copyBoard.setField(13, Mark.OO);
        Board anotherCopyBoard = copyBoard.deepCopy();
        anotherCopyBoard.setField(8, Mark.XX);
        anotherCopyBoard.rotate(0);
        extractedMove = de.extractMove(copyBoard, anotherCopyBoard);
        assertTrue(extractedMove.get(0) == 8 && extractedMove.get(1) == 0);
    }

}
