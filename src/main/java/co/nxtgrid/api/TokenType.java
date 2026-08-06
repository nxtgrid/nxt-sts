package co.nxtgrid.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * REST {@code type} values for {@code POST /token}.
 *
 * <p>Electricity kWh credit is {@link #TOP_UP_KWH}. The wire value {@code TOP_UP} is still
 * accepted for older callers and is normalized to {@link #TOP_UP_KWH} at deserialize time
 * (see {@link TokenTypeDeserializer}).
 */
@JsonDeserialize(using = TokenTypeDeserializer.class)
public enum TokenType {
    /** Class 0 / subclass 0 — transfer electricity credit in kWh. */
    TOP_UP_KWH,
    /** Class 2 — clears existing credit balance on the meter. */
    CLEAR_CREDIT,
    /** Class 2 — clears the tamper condition flag on the meter. */
    CLEAR_TAMPER,
    /** Class 2 — sets the maximum power draw limit on the meter. */
    SET_POWER_LIMIT
}
