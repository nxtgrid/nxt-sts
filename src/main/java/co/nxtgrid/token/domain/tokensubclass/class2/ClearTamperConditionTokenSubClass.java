package co.nxtgrid.token.domain.tokensubclass.class2;

import co.nxtgrid.token.domain.tokensubclass.TokenSubClass;
import co.nxtgrid.token.exceptions.InvalidRangeException;

public class ClearTamperConditionTokenSubClass extends TokenSubClass {

    public ClearTamperConditionTokenSubClass() throws InvalidRangeException {
        super(0x5L, "ClearTamperConditionTokenSubClass");
    }
}
