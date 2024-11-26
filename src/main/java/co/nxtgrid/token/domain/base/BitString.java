package co.nxtgrid.token.domain.base;

import co.nxtgrid.token.exceptions.*;
import co.nxtgrid.token.miscellaneous.Strings;

import java.util.stream.IntStream;

/**
 * This class defines a set of 1s and 0s that make up a bit string.
 * The least significant bit is on the right of the bitstring while
 * the most significant value of the bitstring is on the left
 *
 * most significant <------------------> least significant
 *
 * Created by rmbitiru on 7/3/15.
 */
public class BitString implements Comparable<BitString>, Cloneable {

    public enum Direction {
        LEFT, RIGHT
    }

    public static final int SAME = 0;
    public static final int LESS_THAN = -1;
    public static final int GREATER_THAN = 1;

    private long value = 0;
    private int length = 64;
    private static final int MAX_NO_BITS = 64;

    public BitString() {}

    public BitString(String bitString)
        throws InvalidBitStringException {
        setValue(bitString);
        setLength(bitString.length());
    }

    public BitString(long value) {
        setValue(value);
    }

    public BitString(long bitstring, int length) {
        setValue(bitstring);
        setLength(length);
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public void setValue(String value)
        throws InvalidBitStringException {
        if (!value.matches("[0,1]{1,64}"))
            throw new InvalidBitStringException(Strings.INVALID_BITSTRING);
        this.value = Long.parseUnsignedLong(value, 2);
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int compareTo(BitString bitString)
            throws IllegalComparisonError {
        if (getLength() != bitString.getLength()) throw new IllegalComparisonError(Strings.ILLEGAL_BITSTRING_LENGTH_COMPARISON);
        if (this.value == bitString.getValue())
            return SAME;
        else if (this.value < bitString.getValue())
            return LESS_THAN;
        else return GREATER_THAN;
    }

    /**
     * Concatenates BitStrings from the right to the left.
     *
     * @param bitStrings BitStrings to be concatenated
     * @return Concatenated BitString
     */
    public BitString concat(BitString... bitStrings)
            throws BitConcatOverflowError {
        long concatString = value;
        int noOfBits = getLength();
        int previousShift = noOfBits;
        for (BitString bs : bitStrings)
            noOfBits += bs.getLength();

        if (noOfBits > MAX_NO_BITS) throw new BitConcatOverflowError(Strings.BIT_CONCAT_OVERFLOW_ERROR);

        for (BitString currBitString : bitStrings) {
            long rotated = currBitString.getValue() << previousShift;
            concatString |= rotated;
            previousShift += currBitString.getLength();
        }

        return new BitString(concatString, noOfBits);
    }

    public void setBitsRange(int startIndex, int endIndex, char[] replacementBits)
            throws InvalidRangeException {
        if (replacementBits.length != endIndex - startIndex + 1)
            throw new InvalidRangeException(Strings.BITS_SET_LENGTH_DIFF_RANGE);

        for (int replacementBitsCounter = 0; startIndex <= endIndex;
             startIndex++, replacementBitsCounter++) {
            char v = replacementBits[replacementBitsCounter];
            setBit(startIndex, v);
        }
    }

    public BitString extractBits(int startIndex, int noOfBits)
            throws InvalidRangeException {

        if (noOfBits <= 0)
            throw new InvalidRangeException(Strings.EXTRACT_ONE_LESS_THAN_BITS);

        if (startIndex + noOfBits > getLength())
            throw new InvalidRangeException(Strings.TOO_LONG_BIT_RANGE);

        long allOnes = ~0;
        long mask = (allOnes << MAX_NO_BITS - noOfBits) >>> (MAX_NO_BITS - startIndex - noOfBits);
        long extracted = (value & mask) << (length - (startIndex + noOfBits)) >>> (length - noOfBits);

        BitString extractedBitString = new BitString(extracted);
        extractedBitString.setLength(noOfBits);

        return extractedBitString;
    }

    public Nibble getNibble(int nibblePosition)
            throws InvalidNibbleBitStringException, NibbleOutOfRangeException, InvalidRangeException {
        final int SIZE_OF_NIBBLE = 4;
        final int NO_OF_NIBBLES = getLength() / SIZE_OF_NIBBLE;
        if (nibblePosition < NO_OF_NIBBLES && nibblePosition >= 0) {
            int startNibbleIndex = nibblePosition * 4;
            BitString extractedBits = extractBits(startNibbleIndex, SIZE_OF_NIBBLE);
            return new Nibble(extractedBits);
        } else
            throw new NibbleOutOfRangeException(Strings.NIBBLE_OUT_OF_RANGE);
    }

    public void setNibble(int nibblePosition, Nibble substituteNibble)
            throws NibbleOutOfRangeException, InvalidRangeException {
        final int SIZE_OF_NIBBLE = 4;
        final int NO_OF_NIBBLES = length / SIZE_OF_NIBBLE;

        if (nibblePosition < NO_OF_NIBBLES && nibblePosition >= 0) {
            int startIndex = nibblePosition * 4;
            int endIndex = startIndex + 3;
            setBitsRange(startIndex, endIndex, substituteNibble.getCharArray());
        } else
            throw new NibbleOutOfRangeException(Strings.NIBBLE_OUT_OF_RANGE);
    }

    public Bit getBit(int position) {
        long mask = (1L << position);
        long obtainedVal = (value & mask) >>> position;
        if (obtainedVal == 0)
            return new Bit('0');
        else
            return new Bit('1');
    }

    public void setBit(int index, char val) {
        if (val == '1')
            setBit(index);
        else if (val == '0')
            clearBit(index);
    }

    public void setBit(int position) {
        value |= (1L << position);
    }

    public void clearBit(int position) {
        value &= ~(1L << position) ;
    }

    public void setBitsRange(int fromIndex, int toIndex)
            throws InvalidRangeException {
        if (toIndex < fromIndex || fromIndex - toIndex > MAX_NO_BITS)
            throw new InvalidRangeException(Strings.INVALID_RANGE_SET);
        while (fromIndex <= toIndex) {
            setBit(fromIndex);
            fromIndex++;
        }
    }

    public void clearBitRange(int fromIndex, int toIndex)
            throws InvalidRangeException {
        if (toIndex < fromIndex || fromIndex - toIndex > MAX_NO_BITS)
            throw new InvalidRangeException(Strings.INVALID_RANGE_SET);
        while (fromIndex <= toIndex) {
            clearBit(fromIndex);
            fromIndex++;
        }
    }

    public static BitString rotate(BitString bitString, Direction direction, int shiftSteps)
            throws InvalidRangeException, CloneNotSupportedException {

        BitString rotatedBits = (BitString) bitString.clone();

        if (bitString.getLength() == MAX_NO_BITS) {
            long bits = rotatedBits.getValue();
            if (direction == Direction.RIGHT)
                bits = Long.rotateRight(bits, shiftSteps);
            else if (direction == Direction.LEFT)
                bits = Long.rotateLeft(bits, shiftSteps);
            rotatedBits = new BitString(bits, bitString.getLength());
        } else {
            if (direction == Direction.RIGHT) {
                BitString extractedBits = bitString.extractBits(0, shiftSteps);
                rotatedBits.setBitsRange(bitString.getLength() - (shiftSteps % bitString.length),
                        bitString.getLength() - 1, extractedBits.getBits());
                BitString translatedBits = bitString.extractBits((shiftSteps % bitString.length), bitString.getLength() - (shiftSteps % bitString.length));
                rotatedBits.setBitsRange(0, bitString.getLength() - (shiftSteps % bitString.length) - 1, translatedBits.getBits());
            } else if (direction == Direction.LEFT) {
                BitString extractedBits = bitString.extractBits(bitString.getLength() - shiftSteps, shiftSteps).flip();
                rotatedBits.setBitsRange(0, shiftSteps - 1, extractedBits.getBits());
                BitString translatedBits = bitString.extractBits(0, bitString.getLength() - shiftSteps);
                rotatedBits.setBitsRange(shiftSteps, bitString.getLength() - 1, translatedBits.getBits());
            }
        }
        return rotatedBits;
    }

    public String toString() {
        return Long.toBinaryString(value);
    }

    public String toHexString() {
        return Long.toHexString(value);
    }

    public char[] getBits() {
        char[] bits = new char[getLength()];
        IntStream.range(0, getLength()).forEach(bitCounter -> bits[bitCounter] = getBit(bitCounter).getValue());
        return bits;
    }

    public char[] toCharArr() {
        byte[] result = new byte[8];
        for (int i = 7; i >= 0; i--) {
            result[i] = (byte)(value & 0xFF);
            value >>= 8;
        }
        char[] res = new char[8];
        for (int i = 0; i < res.length; i++) {
            res[i] = (char) ((result[i] & 0xF0) | (result[i] & 0x0F));
        }
        return res;
    }

    public BitString flip()
            throws CloneNotSupportedException {
        char[] bits = getBits();
        BitString  flippedBitString = (BitString) this.clone();
        for (int forwardCounter = 0, bitsCounter = getLength() - 1; bitsCounter >= 0;
             bitsCounter--, forwardCounter++) {
            flippedBitString.setBit(bitsCounter, bits[forwardCounter]);
        }
        return flippedBitString;
    }
}
