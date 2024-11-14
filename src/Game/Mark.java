package Game;

/**
 * Possible marks that players can use in the game.
 */
public enum Mark {

    /**
     * Empty mark
     */
    EMPTY,
    /**
     * X mark
     */
    XX,
    /**
     * O mark
     */
    OO;

    /**
     * Checks if current mark is X or O and returns the opposite mark.
     *
     * @return      the opposite mark
     */
    public Mark other() {
        if (this == XX) {
            return OO;
        } else if (this == OO) {
            return XX;
        } else {
            return EMPTY;
        }
    }
}
