package co.nxtgrid.token.domain;

import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.exceptions.InvalidNewKeyMiddleOrder1Exception;
import co.nxtgrid.token.miscellaneous.Strings;

public class NewKeyMiddleOrder1 implements Entity {

    private final String NAME = "NewKeyHighOrder1";
    private BitString newKeyMiddleOrder1BitString;

    public NewKeyMiddleOrder1(BitString newKeyMiddleOrder1BitString)
        throws InvalidNewKeyMiddleOrder1Exception {
        setBitString(newKeyMiddleOrder1BitString);
    }

    public String getName() {
        return NAME;
    }


    public BitString getBitString() {
        return newKeyMiddleOrder1BitString;
    }

    public void setBitString(BitString newKeyMiddleOrder1BitString)
        throws InvalidNewKeyMiddleOrder1Exception {
        if (newKeyMiddleOrder1BitString.getLength() != 32)
            throw new InvalidNewKeyMiddleOrder1Exception(Strings.INVALID_NEW_KEY_MIDDLE_ORDER_1_EXCEPTION);
        this.newKeyMiddleOrder1BitString = newKeyMiddleOrder1BitString;
    }
}
