package co.nxtgrid.token.generators.utils;

public class LuhnAlgorithm {

    public static long generateCheckDigit (long value) {
        int sum = 0;
        final int MODULUS = 10;
        boolean alternate = true ;
        while(value > 0) {
            long digit = value % MODULUS;
            value /= MODULUS;
            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit = (digit % MODULUS) + 1;
                }
            }
            sum += digit;
            alternate = !alternate;
        }
        long upperBound = (long) (Math.ceil((double) sum / MODULUS) * MODULUS) ;
        return (upperBound - sum);
    }
}
