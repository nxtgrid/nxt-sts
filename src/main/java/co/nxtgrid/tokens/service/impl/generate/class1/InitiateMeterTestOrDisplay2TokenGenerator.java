package co.nxtgrid.tokens.service.impl.generate.class1;

import co.nxtgrid.token.domain.ManufacturerCode;
import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.token.class1.InitiateMeterTestOrDisplay2Token;

import java.util.Map;
import java.util.Optional;

public class InitiateMeterTestOrDisplay2TokenGenerator extends Class1Generator {

    public InitiateMeterTestOrDisplay2TokenGenerator(String requestID, TokenType tokenType) {
        super(requestID, tokenType);
    }

    public Optional<InitiateMeterTestOrDisplay2Token> generateInitiateMeterTestOrDisplay2Token(
            Map<String, String> params) throws Exception {
        BitString fourDigitManufactureCodeBitString = new BitString(0);
        fourDigitManufactureCodeBitString.setLength(16);
        ManufacturerCode fourDigitManufacturerCode = new ManufacturerCode(fourDigitManufactureCodeBitString);

        co.nxtgrid.token.generators.tokensgenerator.nativetoken.class1.InitiateMeterTestOrDisplay2TokenGenerator generator
                = new co.nxtgrid.token.generators.tokensgenerator.nativetoken.class1
                        .InitiateMeterTestOrDisplay2TokenGenerator(getRequestID(),
                                                                    getControl(params),
                                                                    fourDigitManufacturerCode);
        return Optional.of(generator.generate());
    }

}
