package co.nxtgrid.token.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import co.nxtgrid.token.exceptions.InvalidMPLException;

class MaximumPowerLimitTest {

    @Test
    void acceptsMaximumEncodablePowerLimit() {
        assertDoesNotThrow(() -> new MaximumPowerLimit(18_201_624L));
    }

    @Test
    void rejectsAboveMaximumEncodablePowerLimit() {
        assertThrows(InvalidMPLException.class, () -> new MaximumPowerLimit(18_201_625L));
    }

    @Test
    void rejectsNegativePowerLimit() {
        assertThrows(InvalidMPLException.class, () -> new MaximumPowerLimit(-1L));
    }
}
