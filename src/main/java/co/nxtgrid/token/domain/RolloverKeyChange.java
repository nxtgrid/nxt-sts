package co.nxtgrid.token.domain;

import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.exceptions.InvalidRollOverKeyChangeException;
import co.nxtgrid.token.miscellaneous.Strings;

public class RolloverKeyChange implements Entity {

    public final String NAME = "Roll Over Key Change (RO)";
    private BitString rollOverKeyChange;

    public RolloverKeyChange(BitString rollOverKeyChange)
        throws InvalidRollOverKeyChangeException {
        setBitString(rollOverKeyChange);
    }

    public String getName() {
        return NAME;
    }

    public BitString getBitString() {
        return rollOverKeyChange;
    }

    public void setBitString(BitString rollOverKeyChange)
        throws InvalidRollOverKeyChangeException {
        if(rollOverKeyChange.getLength() != 1)
            throw new InvalidRollOverKeyChangeException(Strings.INVALID_ROLL_OVER_KEY_CHANGE);
        this.rollOverKeyChange = rollOverKeyChange;
    }
}
