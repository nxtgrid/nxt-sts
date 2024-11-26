package co.nxtgrid.token.domain.tokensubclass.class2;

import co.nxtgrid.token.domain.tokensubclass.TokenSubClass;
import co.nxtgrid.token.exceptions.InvalidRangeException;

public class SetMaximumPhasePowerUnbalanceLimitTokenSubClass extends TokenSubClass {

    public SetMaximumPhasePowerUnbalanceLimitTokenSubClass() throws InvalidRangeException {
        super(0x6L, "SetMaximumPhasePowerUnbalanceLimitTokenSubClass");
    }
}
