package co.nxtgrid.token.domain;

import co.nxtgrid.token.exceptions.InvalidDrnCheckDigitException;
import co.nxtgrid.token.miscellaneous.Strings;

public class DecoderReferenceNumberCheckDigit implements Entity {

    private long drnCheckDigit ;
    private final String NAME = "DecoderReferenceNumberCheckDigit";

    public DecoderReferenceNumberCheckDigit(long drnCheckDigit)
        throws InvalidDrnCheckDigitException {
        setValue(drnCheckDigit);
    }

    public long getValue() {
        return drnCheckDigit;
    }

    public void setValue(long drnCheckDigit)
        throws InvalidDrnCheckDigitException {
        if (drnCheckDigit > 9)
            throw new InvalidDrnCheckDigitException(Strings.INVALID_DRN_CHECK_DIGIT);
        this.drnCheckDigit = drnCheckDigit;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
