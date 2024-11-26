package co.nxtgrid.token.domain;

import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.exceptions.InvalidMppulException;
import co.nxtgrid.token.generators.utils.Utils;
import co.nxtgrid.token.miscellaneous.Strings;

public class MaximumPhasePowerUnbalanceLimit implements Entity {

    private final String NAME = "MaximumPhasePowerUnbalanceLimit";
    private BitString mppulBitString;

    public MaximumPhasePowerUnbalanceLimit(long mppulBitString)
        throws InvalidMppulException {
        BitString mppulBitStringBitString = Utils.convertToBitString(mppulBitString);
        mppulBitStringBitString.setLength(16);
        setMppulBitString(mppulBitStringBitString);
    }

    public String getName() {
        return NAME;
    }

    public BitString getMppulBitString() {
        return mppulBitString;
    }

    public void setMppulBitString(BitString mppulBitString)
        throws InvalidMppulException {
        if (mppulBitString.getLength() != 16)
            throw new InvalidMppulException(Strings.INVALID_MPPU);
        this.mppulBitString = mppulBitString;
    }
}
