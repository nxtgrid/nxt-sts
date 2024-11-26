package co.nxtgrid.token.domain.tokenclass.class2;

import co.nxtgrid.token.domain.tokenclass.TokenClass;
import co.nxtgrid.token.exceptions.InvalidRangeException;

public abstract class Class2TokenClass extends TokenClass {

    public Class2TokenClass(String name) throws InvalidRangeException {
        super(0x2l, name);
    }
}
