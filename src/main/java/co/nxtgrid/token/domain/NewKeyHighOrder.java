package co.nxtgrid.token.domain;

import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.exceptions.InvalidNKHOException;
import co.nxtgrid.token.miscellaneous.Strings;

public class NewKeyHighOrder implements Entity {

    private final String NAME = "NewKeyHighOrder";
    private BitString nkhoBitString ;

    public NewKeyHighOrder(BitString nkhoBitString)
        throws InvalidNKHOException {
        setBitString(nkhoBitString);
    }

    public String getName() {
        return NAME;
    }


    public BitString getBitString() {
        return nkhoBitString;
    }

    public void setBitString(BitString nkhoBitString)
        throws InvalidNKHOException {
        if (nkhoBitString.getLength() != 32)
            throw new InvalidNKHOException(Strings.INVALID_NKHO);
        this.nkhoBitString = nkhoBitString;
    }
}
