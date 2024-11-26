package co.nxtgrid.ca.keys.utils;

public class HexByteUtils {

    public static byte[] hexStringToByteArr(String hexString) {
        byte[] val = new byte[hexString.length() / 2];
        for (int i = 0; i < val.length; i++) {
            int index = i * 2;
            int j = Integer.parseInt(hexString.substring(index, index + 2), 16);
            val[i] = (byte) j;
        }
        return val;
    }
}
