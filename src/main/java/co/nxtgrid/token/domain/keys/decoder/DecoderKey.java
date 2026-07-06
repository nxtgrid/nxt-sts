package co.nxtgrid.token.domain.keys.decoder;

import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.keys.Key;
import co.nxtgrid.token.generators.utils.Utils;
import org.bouncycastle.util.encoders.Hex;

public class DecoderKey extends Key {

    private final String NAME = "Decoder Key";

    public DecoderKey() {}

    public DecoderKey(byte[] decoderKeyData) {
        super(decoderKeyData);
    }

    /**
     * Builds a decoder key from its hexadecimal string representation. The STA loads the decoder
     * key in reversed byte order, so the hex pairs are decoded last-pair-first.
     */
    public static DecoderKey fromHex(String hexString) {
        if (hexString == null || hexString.length() % 2 != 0) {
            throw new IllegalArgumentException("Invalid hexadecimal string.");
        }

        int length = hexString.length();
        byte[] keyData = new byte[length / 2];
        for (int i = 0; i < keyData.length; i++) {
            int startIndex = length - 2 * (i + 1);
            String hexPair = hexString.substring(startIndex, startIndex + 2);
            keyData[i] = (byte) Integer.parseInt(hexPair, 16);
        }

        return new DecoderKey(keyData);
    }

    public String getName() {
        return NAME;
    }

    public String bitsToString() {
        return Utils.convertByteArrToString(keyData);
    }

    public String bitsToStringReversed() {
        return Utils.convertByteArrToStringReversed(keyData);
    }

    public BitString getBitString() {
        return new BitString(Utils.bytesToLong(keyData));
    }

    @Override
    public String toString() {
        return  new String(Hex.encode(keyData));
    }
}

