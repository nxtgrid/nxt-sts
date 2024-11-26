package co.nxtgrid.token.domain.tokensubclass.class2;

import co.nxtgrid.token.domain.tokensubclass.TokenSubClass;
import co.nxtgrid.token.exceptions.InvalidRangeException;

public class SetMaximumPowerLimitTokenSubClass extends TokenSubClass {

    public SetMaximumPowerLimitTokenSubClass() throws InvalidRangeException {
        super(0x0L, "SetMaximumPowerLimitTokenSubClass");
    }
}
