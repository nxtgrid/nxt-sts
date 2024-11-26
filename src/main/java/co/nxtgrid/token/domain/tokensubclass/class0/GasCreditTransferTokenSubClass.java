package co.nxtgrid.token.domain.tokensubclass.class0;

import co.nxtgrid.token.domain.tokensubclass.TokenSubClass;
import co.nxtgrid.token.exceptions.InvalidRangeException;

public class GasCreditTransferTokenSubClass extends TokenSubClass {

    public GasCreditTransferTokenSubClass() throws InvalidRangeException {
        super(0x2L, "Gas");
    }
}
