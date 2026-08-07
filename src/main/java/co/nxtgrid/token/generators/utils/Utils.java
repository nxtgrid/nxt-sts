package co.nxtgrid.token.generators.utils;

import co.nxtgrid.token.domain.KeyExpiryNumber;
import co.nxtgrid.token.domain.TokenIdentifier;
import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.exceptions.InvalidBitException;
import co.nxtgrid.token.exceptions.InvalidBitStringException;
import co.nxtgrid.token.exceptions.InvalidUnitsPurchasedBitsException;
import co.nxtgrid.token.exceptions.InvalidVendingOrDecoderKeyException;
import co.nxtgrid.token.miscellaneous.Strings;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Utils {

    public static void validateTokenIdentifier(TokenIdentifier tokenIdentifier, KeyExpiryNumber keyExpiryNumber)
            throws InvalidVendingOrDecoderKeyException {
        if(tokenIdentifier.getDifferenceFromBaseTimeInMinutes()  > keyExpiryNumber.getValue() * 65535)
            throw new InvalidVendingOrDecoderKeyException(Strings.INVALID_VENDING_OR_DECODER_KEY_EXCEPTION);
    }

    public static BitString convertToBitString(double unitsPurchased) {

        int exponent;

        /** Use of a double is important to ensure that
         * the generated values (used for the Amount)
         * field are rounded up. Losses incurred are
         * discussed in IEC 62055-41 v2.0 page #41
         * (Maximum error due to rounding)
         */
        double mantissa = unitsPurchased;
        for (exponent = 0; exponent <= 3; exponent++) {
            if (mantissa < Math.pow(2, 14)) break;
            mantissa -= Math.pow(2, 14);
            mantissa /= 10;

        }
        long encoded = (exponent << 14) + (long) Math.ceil(mantissa);
        // Exponent is only 2 bits in the STS amount field; values that need exp > 3
        // (or otherwise exceed 16 bits) are not representable — reject rather than
        // emit bits the decoder will refuse (see convertToDouble).
        if (exponent > 3 || encoded > 0xFFFFL) {
            throw new IllegalArgumentException(
                "amount exceeds the maximum encodable STS 16-bit value");
        }
        return new BitString(encoded);
    }

    public static double convertToDouble(BitString amountBitString)
            throws InvalidUnitsPurchasedBitsException, InvalidBitStringException {

        double unitsPurchased ;
        final int NO_OF_BITS = 16;

//        amountBitString = new BitString("100000000000000");
//        amountBitString.setLength(16);

        if (amountBitString.getLength() == NO_OF_BITS) {

            long unitsPurchasedBits = amountBitString.getValue();
            long mantissa = unitsPurchasedBits & 0x3FFF;
            long exponent = unitsPurchasedBits >> 14;

            if (exponent > 3)
                throw new IndexOutOfBoundsException("exponent value too large");

            unitsPurchased = mantissa * Math.pow(10, exponent);

            for (int i = 1; i <= exponent; i++)
                unitsPurchased += Math.pow(2, 14) * Math.pow(10, i - 1);

            return unitsPurchased/10;

        } else
            throw new InvalidUnitsPurchasedBitsException(Strings.INVALID_NO_UNITS) ;
    }

    public static long bytesToLong(byte[] b) {
        long result = 0;
        for (int i = 0; i < 8; i++) {
            result <<= 8;
            result |= (b[i] & 0xFF);
        }
        return result;
    }

    public static byte[] combine(byte[] arr1, byte[] arr2)
            throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write( arr1 );
        outputStream.write( arr2 );
        return outputStream.toByteArray( );
    }

    public static int getNoOfDigits(long value) {
        int noOfDigits = 0;
        while (value > 0) {
            value /= 10;
            noOfDigits++;
        }
        return noOfDigits;
    }

    public static byte[] longToBytes(long bitString) {
        final byte result[] = new byte[7];
        for (int i = 6; i >= 0; i--) {
            result[i] = (byte)(bitString & 0xFF);
            bitString >>= 8;
        }
        return result;
    }

    public static String convertByteArrToString(byte[] val) {
        int len = val.length ;
        String r = "" ;
        for (int i = len - 1 ; i >= 0; i--) {
            byte b = val[i] ;
            r += String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
        }
        return r;
    }

    public static String convertByteArrToStringReversed(byte[] val) {
        String r = "" ;
        for (int i = 0 ; i < val.length; i++) {
            byte b = val[i] ;
            r += String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
        }
        return r;
    }

    public static String convertStringArrToString(String[] args) {
        String concat = "" ;
        for(String s : args)
            concat += s + " " ;
        return concat ;
    }

    public static long convertBitStringToLong(String input)
            throws InvalidBitException {
        long result = 0;
        for (int strElemsCounter = 0; strElemsCounter < input.length(); ++strElemsCounter) {
            char c = input.charAt(input.length() - 1 - strElemsCounter);
            switch (c) {
                case '1':
                    result |= (1L << strElemsCounter);
                    break;
                case '0':
                    break;
                default:
                    throw new InvalidBitException(String.format(Strings.INVALID_BIT, c));
            }
        }
        return result;
    }
}
