package co.nxtgrid.token.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import co.nxtgrid.token.exceptions.InvalidUnitsPurchasedException;

class AmountTest {

    @Test
    void acceptsMaximumEncodableKwh() {
        assertDoesNotThrow(() -> new Amount(1_820_162.4));
    }

    @Test
    void rejectsAboveMaximumEncodableKwh() {
        assertThrows(InvalidUnitsPurchasedException.class, () -> new Amount(1_820_163));
    }

    @Test
    void rejectsFormerIncorrectGuardBand() {
        // Previously accepted (guard was 18_201_624 kWh) but not STS-encodable.
        assertThrows(InvalidUnitsPurchasedException.class, () -> new Amount(2_000_000));
    }
}
