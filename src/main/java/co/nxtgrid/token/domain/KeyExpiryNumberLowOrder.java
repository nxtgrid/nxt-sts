package co.nxtgrid.token.domain;

import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.exceptions.InvalidKenloException;
import co.nxtgrid.token.miscellaneous.Strings;

public class KeyExpiryNumberLowOrder implements Entity {

    public final String NAME = "SupplyGroupCodeLowOrder";
    private BitString kenloBitString ;

    public KeyExpiryNumberLowOrder(BitString kenloBitString)
        throws InvalidKenloException {
        setKenloBitString(kenloBitString);
    }

    public BitString getKenloBitString() {
        return kenloBitString;
    }

    public void setKenloBitString(BitString kenloBitString)
        throws InvalidKenloException {
        if (kenloBitString.getLength() != 4)
            throw new InvalidKenloException(Strings.INVALID_KENLO) ;
        this.kenloBitString = kenloBitString;
    }

    public String getName() {
        return NAME;
    }
}
