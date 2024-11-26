package co.nxtgrid.token.domain.tokenclass.class1;

import co.nxtgrid.token.domain.tokenclass.TokenClass;
import co.nxtgrid.token.exceptions.InvalidRangeException;

public abstract class Class1TokenClass extends TokenClass {

    public Class1TokenClass(String name) throws InvalidRangeException {
        super(0x1l, name);
    }
}
