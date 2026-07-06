package co.nxtgrid.api;

final class StsUtils {

    private StsUtils() {
    }

    static byte[] convertHexStringToReversedByteArray(String hexString) {
        if (hexString == null || hexString.length() % 2 != 0) {
            throw new IllegalArgumentException("Invalid hexadecimal string.");
        }

        int length = hexString.length();
        byte[] byteArray = new byte[length / 2];

        for (int i = 0; i < byteArray.length; i++) {
            int startIndex = length - 2 * (i + 1);
            String hexPair = hexString.substring(startIndex, startIndex + 2);
            byteArray[i] = (byte) Integer.parseInt(hexPair, 16);
        }

        return byteArray;
    }
}
