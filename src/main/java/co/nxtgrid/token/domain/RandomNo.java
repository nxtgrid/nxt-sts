package co.nxtgrid.token.domain;

import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.exceptions.InvalidRNDNoException;
import co.nxtgrid.token.exceptions.InvalidRangeException;
import co.nxtgrid.token.miscellaneous.Strings;

public class RandomNo implements Entity {

    private final String NAME = "RandomNo";
    private BitString rndBitString = new BitString();
    private static final int NO_OF_BITS = 4;

    public RandomNo(BitString randomNo)
            throws InvalidRangeException {
        setBitString(randomNo);
    }

    public String getName() {
        return NAME ;
    }

    public BitString getBitString() {
        return rndBitString;
    }

    public void setBitString(BitString bitString)
            throws InvalidRangeException {
        if (bitString.getValue() > 15 || bitString.getLength() != NO_OF_BITS)
            throw new InvalidRangeException(Strings.BIT_STRING_SIZE_ERROR) ;
        this.rndBitString = bitString;
    }

    public String toString () {
        return getBitString().toString() ;
    }

    public void generateRandomBits() throws InvalidRangeException {
        int randomVal = (int )(Math.random() * 15 + 0);
        setBitString(new BitString(randomVal).extractBits(0, 4)) ;
    }

    public static RandomNo getRNDNo(BitString bitString)
            throws InvalidRNDNoException, InvalidRangeException {
        if (bitString.getLength() != NO_OF_BITS)
            throw new InvalidRNDNoException(Strings.BIT_STRING_SIZE_ERROR ) ;
        return new RandomNo(bitString);
    }

    public String bitsToString() {
        return String.format("%" + NO_OF_BITS + "s", Long.toBinaryString(rndBitString.getValue())).replace(' ', '0');
    }
}

