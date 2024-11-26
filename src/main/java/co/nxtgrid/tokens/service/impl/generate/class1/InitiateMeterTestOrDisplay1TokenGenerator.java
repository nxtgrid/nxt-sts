package co.nxtgrid.tokens.service.impl.generate.class1;

import co.nxtgrid.hsm.prism.impl.exceptions.UnsupportedTokenTypeException;
import co.nxtgrid.token.domain.ManufacturerCode;
import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.token.class1.InitiateMeterTestOrDisplay1Token;


import java.util.Map;
import java.util.Optional;

public class InitiateMeterTestOrDisplay1TokenGenerator extends Class1Generator {

    public InitiateMeterTestOrDisplay1TokenGenerator(String requestID, TokenType tokenType) {
        super(requestID, tokenType);
    }

    public Optional<InitiateMeterTestOrDisplay1Token>  generateInitiateMeterTestOrDisplay1Token(
            Map<String, String> params) throws Exception {
        if (getTokenType() == TokenType.NATIVE) {
            BitString twoDigitManufactureCodeBitString = new BitString(0);
            twoDigitManufactureCodeBitString.setLength(8);
            ManufacturerCode twoDigitManufacturerCode = new ManufacturerCode(twoDigitManufactureCodeBitString);

            co.nxtgrid.token.generators.tokensgenerator.nativetoken.class1
                    .InitiateMeterTestOrDisplay1TokenGenerator generator
                    = new co.nxtgrid.token.generators.tokensgenerator.nativetoken.
                            class1.InitiateMeterTestOrDisplay1TokenGenerator(getRequestID(),
                                                                            getControl(params),
                                                                            twoDigitManufacturerCode);
            return Optional.of(generator.generate());

        } else if (getTokenType() == TokenType.PRISM_THRIFT) {
            co.nxtgrid.token.generators.tokensgenerator.prism.class1
                    .InitiateMeterTestOrDisplay1TokenGenerator generator
                    = new co.nxtgrid.token.generators.tokensgenerator.prism.class1
                            .InitiateMeterTestOrDisplay1TokenGenerator(getRequestID(),
                                                                        getHost(params),
                                                                        getPort(params),
                                                                        getRealm(params),
                                                                        getUsername(params),
                                                                        getPassword(params),
                                                                        getIndividualAccountIdentificationNumber(params),
                                                                        getControl(params),
                                                                        getManufacturerCode(params));
            return Optional.of(generator.generate().get(0));

        }
        throw new UnsupportedTokenTypeException();
    }
}
