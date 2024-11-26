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

