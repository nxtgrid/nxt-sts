package co.nxtgrid.token.domain.keys.vending;

import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.keys.Key;
import co.nxtgrid.token.generators.utils.Utils;

public abstract class VendingKey extends Key {

    public VendingKey(){}

    public VendingKey(byte[] keyData) {
        super(keyData);
    }

    public String bitsToString() {
        return new String(keyData);
    }

    public BitString getBitString() {
        return new BitString(Utils.bytesToLong(keyData));
    }
}
