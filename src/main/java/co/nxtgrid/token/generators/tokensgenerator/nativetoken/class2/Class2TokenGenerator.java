package co.nxtgrid.token.generators.tokensgenerator.nativetoken.class2;

import co.nxtgrid.token.domain.token.Token;
import co.nxtgrid.token.generators.tokensgenerator.nativetoken.TokenGenerator;

public abstract class Class2TokenGenerator<T extends Token> extends TokenGenerator<T>{

    public Class2TokenGenerator(String requestID) {
        super(requestID);
    }
}
