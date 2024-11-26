package co.nxtgrid.token.domain;

import co.nxtgrid.token.exceptions.InvalidKeyTypeException;
import co.nxtgrid.token.miscellaneous.Strings;

public class KeyType implements Entity {

    /**
     * 0 - DITK
     * 1 - DDTK
     * 2 - DUTK
     * 3 - DCTK
     */
    private int keyType;
    private final String NAME = "Key Type";

    public KeyType(int keyType)
            throws InvalidKeyTypeException {
        setValue(keyType);
    }

    public int getValue() {
        return keyType;
    }

    public void setValue(int keyType)
            throws InvalidKeyTypeException {
        if (keyType < 0 || keyType > 3)
            throw new InvalidKeyTypeException(Strings.INVALID_KEY_TYPE);
        this.keyType = keyType;
    }

    public String getName() {
        return NAME;
    }
}
