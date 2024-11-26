package co.nxtgrid.token.domain.tokensubclass.class2;

import co.nxtgrid.token.domain.tokensubclass.TokenSubClass;
import co.nxtgrid.token.exceptions.InvalidRangeException;

public class ClearCreditTokenSubClass extends TokenSubClass {

    public ClearCreditTokenSubClass() throws InvalidRangeException {
        super(0x1L, "ClearCreditTokenSubClass");
    }
}
