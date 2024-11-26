package co.nxtgrid.token.domain;

import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.exceptions.InvalidNewKeyMiddleOrder2Exception;
import co.nxtgrid.token.miscellaneous.Strings;

public class NewKeyMiddleOrder2 implements Entity {

    public final String NAME = "NewKeyLowOrder2";
    private BitString nkmo2BitString;

    public NewKeyMiddleOrder2(BitString nkmo2BitString)
        throws InvalidNewKeyMiddleOrder2Exception {
        setNkmo2BitString(nkmo2BitString);
    }

    public String getName() {
        return NAME;
    }


    public BitString getBitString() {
        return nkmo2BitString;
    }

    public void setNkmo2BitString(BitString nkmo2BitString)
        throws InvalidNewKeyMiddleOrder2Exception {
        if (nkmo2BitString.getLength() != 32)
            throw new InvalidNewKeyMiddleOrder2Exception(Strings.INVALID_NEW_KEY_MIDDLE_ORDER_2_EXCEPTION);
        this.nkmo2BitString = nkmo2BitString;
    }
}
