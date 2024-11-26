package co.nxtgrid.token.domain.tokensubclass.class2;

import co.nxtgrid.token.domain.tokensubclass.TokenSubClass;
import co.nxtgrid.token.exceptions.InvalidRangeException;

public class SetWaterMeterFactorTokenSubClass extends TokenSubClass {

    public SetWaterMeterFactorTokenSubClass() throws InvalidRangeException {
        super(0x7L, "SetWaterMeterFactorTokenSubClass");
    }
}
