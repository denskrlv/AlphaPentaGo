package Player;

import Game.Board;
import Game.Mark;
import Strategy.Strategy;
import Strategy.RandomBot;

/**
 * Extends the AbstractPlayer class, implements the determineMove method by using the Monte Carlo Search Tree Algorithms.
 */
public class BotPlayer extends AbstractPlayer {

    private Mark mark;
    private Strategy strategy;

    /**
     * Creates a BotPlayer which plays a game instead of a user with preferable strategy and mark.
     *
     * @param   mark        Mark object (EMPTY, XX or OO)
     * @param   strategy    preferable strategy (AlphaPentaGo or RandomBot)
     */
    public BotPlayer(Mark mark, Strategy strategy) {
        super(strategy.getName(), mark);
        this.mark = mark;
        this.strategy = strategy;
    }

    /**
     * Creates a BotPlayer which plays a game instead of a user with preferable mark.
     *
     * @param   mark    Mark object (EMPTY, XX or OO)
     */
    public BotPlayer(Mark mark) {
        super("RandomBot", mark);
        this.mark = mark;
        this.strategy = new RandomBot();
    }


    @Override
    public String[] determineMove(Board board) {
        return strategy.determineMove(board, mark);
    }

    /**
     * Returns the mark of a current player.
     *
     * @return  Mark object (EMPTY, XX or OO)
     */
    @Override
    public Mark getMark() {
        return mark;
    }

    /**
     * Returns the name of the current player.
     *
     * @return  Mark object (EMPTY, XX or OO)
     */
    @Override
    public String getName() {
        return super.getName();
    }

    /**
     * Sets the mark.
     *
     * @param   mark    Mark object (EMPTY, XX or OO)
     */
    public void setMark(Mark mark) {
        this.mark = mark;
    }

    /**
     * Sets the strategy.
     *
     * @param   strategy    preferable strategy (AlphaPentaGo or RandomBot)
     */
    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

}
