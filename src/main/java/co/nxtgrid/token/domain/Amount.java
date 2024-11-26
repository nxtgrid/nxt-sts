package co.nxtgrid.token.domain;

import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.exceptions.InvalidBitStringException;
import co.nxtgrid.token.exceptions.InvalidRangeException;
import co.nxtgrid.token.exceptions.InvalidUnitsPurchasedException;
import co.nxtgrid.token.generators.utils.Utils;
import co.nxtgrid.token.miscellaneous.Strings;

public class Amount implements Entity {

    private final String NAME = "Amount";
    private double unitsPurchased;
    private BitString amountBitString = new BitString();
    private final int NO_OF_BITS = 16 ;

    public Amount() {}

    public Amount(double unitsPurchased)
        throws InvalidUnitsPurchasedException, InvalidRangeException, InvalidBitStringException {
        final int UNITS_PURCHASED_MIN = 0;
        final int UNITS_PURCHASED_MAX = 18201624;

        if (unitsPurchased < UNITS_PURCHASED_MIN
                || unitsPurchased > UNITS_PURCHASED_MAX)
            throw new InvalidUnitsPurchasedException("Invalid number of units purchased!");

        setAmountPurchased(unitsPurchased);
        generateAmountBitString() ;
    }

    public String getName() {
        return NAME;
    }

    public int getLength() {
        return NO_OF_BITS;
    }

    public BitString getBitString() {
        return amountBitString;
    }

    public void setBitString(BitString bitString) throws InvalidRangeException {
        if (bitString.getLength() == NO_OF_BITS)
            this.amountBitString = bitString;
        else
            throw new InvalidRangeException(Strings.BIT_STRING_SIZE_ERROR) ;
    }

    public double getAmountPurchased() {
        return unitsPurchased;
    }

    private void setAmountPurchased(double unitsPurchased) {
        this.unitsPurchased = unitsPurchased;
    }

    private void generateAmountBitString()
     throws InvalidUnitsPurchasedException, InvalidRangeException, InvalidBitStringException {
        double refactoredAmountBits = unitsPurchased < 1 ? (int) Math.ceil(unitsPurchased * 10) : (int) (unitsPurchased * 10);
        BitString generatedAmountBitString = Utils.convertToBitString(refactoredAmountBits) ;
        generatedAmountBitString.setLength(NO_OF_BITS);
        setBitString(generatedAmountBitString);
    }

    @Override
    public String toString() {
        return String.format("%" + NO_OF_BITS + "s", Long.toBinaryString(amountBitString.getValue())).replace(' ', '0');
    }
}
