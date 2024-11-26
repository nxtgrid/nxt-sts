package co.nxtgrid.token.domain.keys.vending;

import co.nxtgrid.token.exceptions.InvalidKeyDataException;

public class VendingUniqueDESKey extends VendingKey {

    private final String NAME = "Vending Unique DES Key";

    public VendingUniqueDESKey(byte[] keyData)
        throws InvalidKeyDataException {
        super(keyData);
    }

    public String getName() {
        return NAME;
    }
}
