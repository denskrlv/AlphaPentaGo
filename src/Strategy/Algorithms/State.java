package Strategy.Algorithms;

import Game.Board;
import Game.Mark;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents the state of the particular node. The state includes the position of the board, the playing mark,
 * number of visits and win score.
 * <p>
 * The <code>State</code> contains eight methods to set or get the board, mark, total amount of visits and win score.
 * <p>
 * The <code>State</code> contains one method to return the opponent mark of the current state.
 * <p>
 * The <code>State</code> contains one method get all possible moves that can be made.
 * <p>
 * The <code>State</code> contains three utility methods which increments value of visits, toggle playing mark and
 * add score to the node.
 * <p>
 * The <code>State</code> contains one method which plays a random game.
 *
 * @author  Denis Krylov
 * @since   2.0
 * @see     Board
 * @see     Mark
 * @see     Node
 * @see     MonteCarloTreeSearch
 */
public class State {

    private Board board;
    private Mark mark;
    private int visitCount;
    private double winScore;

    public State() {
        board = new Board();
    }

    public State(State state) {
        this.board = state.board.deepCopy();
        this.mark = state.getMark();
        this.visitCount = state.getVisitCount();
        this.winScore = state.getWinScore();
    }

    public State(Board board, Mark mark) {
        this.board = board.deepCopy();
        this.mark = mark;
    }

    /**
     * Returns the board of the particular state.
     *
     * @return      board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Sets the board for the particular state.
     *
     * @param   board   selected board
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Returns the mark of the particular state.
     *
     * @return      Mark
     */
    public Mark getMark() {
        return mark;
    }

    /**
     * Sets the mark for the particular state.
     *
     * @param   mark   selected mark
     */
    public void setMark(Mark mark) {
        this.mark = mark;
    }

    /**
     * Returns the opponent's mark.
     *
     * @return      opponent's mark
     */
    public Mark getOpponent() {
        return mark.other();
    }

    /**
     * Returns the amount of visits of the particular state.
     *
     * @return      the amount of visits of the state
     */
    public int getVisitCount() {
        return visitCount;
    }

    /**
     * Sets the amount of visits of the particular state.
     *
     * @param   visitCount  the amount of visits of the state
     */
    public void setVisitCount(int visitCount) {
        this.visitCount = visitCount;
    }

    /**
     * Returns the win score of the particular state.
     *
     * @return      win score of the state
     */
    public double getWinScore() {
        return winScore;
    }

    /**
     * Sets the win score for particular state.
     *
     * @param   winScore    win score of the state
     */
    public void setWinScore(double winScore) {
        this.winScore = winScore;
    }

    /**
     * Returns all possible states of the game. Puts a mark on an empty cell and rotate the board. Add each state
     * to the array and return it.
     *
     * @return      array of all possible states.
     */
    public List<State> getAllPossibleStates() {
        List<Board> uniqueBoards = new ArrayList<>();
        List<Integer> emptyCells = board.getEmptyCells();
        List<State> superPositions = new ArrayList<>();
        boolean isUnique = true;

        for (Integer emptyCell : emptyCells) {
            for (int r = 0; r < 8; r++) {
                Board boardCopy = board.deepCopy();
                boardCopy.setField(emptyCell, mark.other());
                boardCopy.rotate(r);
                for (Board ub : uniqueBoards) {
                    if (Arrays.equals(boardCopy.getFields(), ub.getFields())) {
                        isUnique = false;
                        break;
                    }
                }
                if (isUnique) {
                    uniqueBoards.add(boardCopy);
                }
                isUnique = true;
            }
        }

        for (Board ub : uniqueBoards) {
            State newState = new State(ub, mark);
            newState.setMark(mark.other());
            superPositions.add(newState);
        }

        return superPositions;
    }

    /**
     * Increment the number of visits to a particular state.
     */
    public void incrementVisit() {
        this.visitCount++;
    }

    /**
     * Adds the win score to the particular state if the score is not the minimum possible.
     *
     * @param   score   score to add
     */
    public void addScore(double score) {
        if (this.winScore != Integer.MIN_VALUE) {
            this.winScore += score;
        }
    }

    /**
     * Changes the mark to the opposite one. Very useful to determine the opponent player.
     */
    public void toggleMark() {
        this.mark = mark.other();
    }

    /**
     * Makes a random move from the current state. It's useful to simulate a random game in the MCTS.
     */
    public void randomPlay() {
        List<Integer> emptyCells = this.board.getEmptyCells();
        int totalPossibilities = emptyCells.size();
        int selectRandomField = (int) (Math.random() * totalPossibilities);
        int selectRandomRotation = (int) (Math.random() * 8);
        this.board.setField(emptyCells.get(selectRandomField), this.mark);
        this.board.rotate(selectRandomRotation);
    }

}
