package co.nxtgrid.token.domain;

import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.exceptions.InvalidBitStringException;
import co.nxtgrid.token.exceptions.InvalidMPLException;
import co.nxtgrid.token.exceptions.InvalidRangeException;
import co.nxtgrid.token.exceptions.InvalidUnitsPurchasedException;
import co.nxtgrid.token.generators.utils.Utils;
import co.nxtgrid.token.miscellaneous.Strings;

public class MaximumPowerLimit implements Entity {

    private final String NAME = "Maximum Power Limit";
    private long maximumPowerLimit;
    private BitString maximumPowerLimitBitString;
    private final int NO_OF_BITS = 16;

    public MaximumPowerLimit(long maximumPowerLimit)
            throws InvalidMPLException, InvalidUnitsPurchasedException,
            InvalidRangeException, InvalidBitStringException {
        setMaximumPowerLimit(maximumPowerLimit);
        generateMPLBitString();
    }

    public String getName() {
        return NAME;
    }

    public int getLength() {
        return NO_OF_BITS;
    }

    public BitString getBitString() {
        return maximumPowerLimitBitString;
    }

    public void setBitString(BitString maximumPowerLimitBitString)
        throws InvalidMPLException {
        if (maximumPowerLimitBitString.getLength() != NO_OF_BITS)
            throw new InvalidMPLException(Strings.INVALID_MPL_SIZE_ERROR);
        this.maximumPowerLimitBitString = maximumPowerLimitBitString;
    }

    public long getMaximumPowerLimit() {
        return maximumPowerLimit;
    }

    private void setMaximumPowerLimit(long maximumPowerLimit) {
        this.maximumPowerLimit = maximumPowerLimit;
    }

    private void generateMPLBitString()
            throws InvalidUnitsPurchasedException, InvalidRangeException, InvalidMPLException, InvalidBitStringException {
        BitString generatedAmountBitString = Utils.convertToBitString(maximumPowerLimit);
        generatedAmountBitString.setLength(NO_OF_BITS);
        setBitString(generatedAmountBitString);
    }

    @Override
    public String toString() {
        return String.format("%" + NO_OF_BITS + "s", Long.toBinaryString(maximumPowerLimitBitString.getValue())).replace(' ', '0');
    }
}
