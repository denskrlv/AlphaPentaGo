package Test;

import Game.Board;
import Game.Mark;
import Strategy.Algorithms.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Tests that checks how states work in MCTS algorithm.
 */
public class StateTest {

    private Board board;
    private final Mark mark = Mark.XX;

    @BeforeEach
    public void setUp() {
        board = new Board();
    }

    /**
     * Checks that all possible states return all states on the board without duplicates.
     */
    @Test
    public void testGetAllPossibleStates() {
        State state = new State(board, mark);
        List<State> possibleStates = state.getAllPossibleStates();
        assertEquals(36, possibleStates.size());
    }

    /**
     * Checks that after a random move the amount of empty cells on the board decreased by 1.
     */
    @Test
    public void testRandomPlay() {
        State state = new State(board, mark);
        state.randomPlay();
        assertTrue(state.getBoard().getEmptyCells().size() == board.getEmptyCells().size() - 1);
    }

}
