package co.nxtgrid.token.domain.rate;

import co.nxtgrid.token.domain.Entity;
import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.miscellaneous.Strings;

public class Rate implements Entity {

    public final String NAME = "Rate";
    private BitString rateBitString ;

    public Rate(BitString rateBitString)
        throws InvalidRateException {
        setRateBitString(rateBitString);
    }

    public String getName() {
        return NAME;
    }


    public BitString getRateBitString() {
        return rateBitString;
    }

    public void setRateBitString(BitString rateBitString)
        throws InvalidRateException {
        if(rateBitString.getLength() != 16)
            throw new InvalidRateException(Strings.INVALID_RATE);
        this.rateBitString = rateBitString;
    }

    @Override
    public String toString() {
        return rateBitString.toString();
    }
}
