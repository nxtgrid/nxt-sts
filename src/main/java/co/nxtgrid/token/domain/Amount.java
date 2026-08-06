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

    /**
     * Maximum encodable STS transfer amount in kWh. The 16-bit amount field (exponent 0–3,
     * mantissa 0–16383) tops out at 18_201_624 tenths of a unit, i.e. 1_820_162.4 kWh.
     * Comparing against 18_201_624 here would be off by 10× (that constant is in tenths).
     */
    private static final double UNITS_PURCHASED_MIN_KWH = 0;
    private static final double UNITS_PURCHASED_MAX_KWH = 1_820_162.4;
    /** Max value after scaling kWh → tenths; must fit STS amount encoding (exp ≤ 3). */
    private static final long MAX_AMOUNT_TENTHS = 18_201_624L;

    public Amount(double unitsPurchased)
        throws InvalidUnitsPurchasedException, InvalidRangeException, InvalidBitStringException {
        if (unitsPurchased < UNITS_PURCHASED_MIN_KWH
                || unitsPurchased > UNITS_PURCHASED_MAX_KWH)
            throw new InvalidUnitsPurchasedException(
                "kwh must be between 0 and 1820162.4 (STS maximum)");

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
        double refactoredAmountBits = unitsPurchased < 1
            ? (int) Math.ceil(unitsPurchased * 10)
            : (int) (unitsPurchased * 10);
        if (refactoredAmountBits > MAX_AMOUNT_TENTHS) {
            throw new InvalidUnitsPurchasedException(
                "kwh must be between 0 and 1820162.4 (STS maximum)");
        }
        BitString generatedAmountBitString = Utils.convertToBitString(refactoredAmountBits) ;
        generatedAmountBitString.setLength(NO_OF_BITS);
        setBitString(generatedAmountBitString);
    }

    @Override
    public String toString() {
        return String.format("%" + NO_OF_BITS + "s", Long.toBinaryString(amountBitString.getValue())).replace(' ', '0');
    }
}
