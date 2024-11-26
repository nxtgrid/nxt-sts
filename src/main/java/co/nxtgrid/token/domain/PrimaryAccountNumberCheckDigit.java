package co.nxtgrid.token.domain;

import co.nxtgrid.token.exceptions.InvalidPanCheckDigitException;
import co.nxtgrid.token.miscellaneous.Strings;

public class PrimaryAccountNumberCheckDigit implements Entity {

    private int panCheckDigit = 0;
    private final String NAME = "PrimaryAccountNumberCheckDigit";

    public PrimaryAccountNumberCheckDigit(int panCheckDigit)
        throws InvalidPanCheckDigitException {
        setValue(panCheckDigit);
    }

    public long getValue() {
        return panCheckDigit;
    }

    public void setValue(int panCheckDigit)
       throws InvalidPanCheckDigitException {
        if (panCheckDigit > 9)
            throw new InvalidPanCheckDigitException(Strings.INVALID_PAN_CHECK_DIGIT);
        this.panCheckDigit = panCheckDigit ;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
