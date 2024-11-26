package co.nxtgrid.token.domain.tokensubclass.class0;

import co.nxtgrid.token.domain.tokensubclass.TokenSubClass;
import co.nxtgrid.token.exceptions.InvalidRangeException;

public class TimeTokenSubClass extends TokenSubClass {

    public TimeTokenSubClass() throws InvalidRangeException {
        super(0x3L, "Time");
    }
}
