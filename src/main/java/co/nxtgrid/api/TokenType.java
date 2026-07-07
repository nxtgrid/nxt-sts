package co.nxtgrid.api;

public enum TokenType {
    /** Class 0 — transfers electricity credit to the meter. */
    TOP_UP,
    /** Class 2 — clears existing credit balance on the meter. */
    CLEAR_CREDIT,
    /** Class 2 — clears the tamper condition flag on the meter. */
    CLEAR_TAMPER,
    /** Class 2 — sets the maximum power draw limit on the meter. */
    SET_POWER_LIMIT
}
