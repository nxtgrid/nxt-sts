package co.nxtgrid.token.domain.tokensubclass.class2;

import co.nxtgrid.token.domain.tokensubclass.TokenSubClass;
import co.nxtgrid.token.exceptions.InvalidRangeException;

public class SetTariffRateTokenSubClass extends TokenSubClass {

    public SetTariffRateTokenSubClass() throws InvalidRangeException {
        super(0x2L, "SetTariffRateTokenSubClass");
    }
}
