package co.nxtgrid.token.domain;

import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.exceptions.InvalidKeyExpiryNumberException;
import co.nxtgrid.token.miscellaneous.Strings;

public class KeyExpiryNumber implements Entity {

    private final String NAME = "Key Expiry Number";
    private long keyExpiryNumber;

    public KeyExpiryNumber(long keyExpiryNumber)
        throws InvalidKeyExpiryNumberException {
        setValue(keyExpiryNumber);
    }

    public KeyExpiryNumber(BitString keyExpiryNumberBitString)
            throws InvalidKeyExpiryNumberException {
        setValue(keyExpiryNumberBitString.getValue());
    }

    public long getValue() {
        return keyExpiryNumber;
    }

    public void setValue(long keyExpiryNumber)
        throws InvalidKeyExpiryNumberException {
        if (keyExpiryNumber < 0 || keyExpiryNumber > 255)
            throw new InvalidKeyExpiryNumberException(Strings.INVALID_KEY_EXPIRY_NUMBER);
        this.keyExpiryNumber = keyExpiryNumber;
    }

    public String getName() {
        return NAME;
    }
}
