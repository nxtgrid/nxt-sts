package co.nxtgrid.tokens.service.impl.generate.class2;

import co.nxtgrid.hsm.prism.impl.exceptions.UnsupportedTokenTypeException;
import co.nxtgrid.token.domain.token.class2.ClearCreditToken;

import java.util.Map;
import java.util.Optional;

public class ClearCreditTokenGenerator extends Class2Generator {

    public ClearCreditTokenGenerator(String requestID, TokenType tokenType) {
        super(requestID, tokenType);
    }

    public Optional<ClearCreditToken> generateClearCreditToken(Map<String, String> params) throws Exception {
        if (getTokenType() == TokenType.NATIVE) {
            co.nxtgrid.token.generators.tokensgenerator.nativetoken.class2.ClearCreditTokenGenerator generator
                    = new co.nxtgrid.token.generators.tokensgenerator.nativetoken.class2
                            .ClearCreditTokenGenerator(getRequestID(),
                                                        getRandomNo(params),
                                                        getTokenIdentifier(params),
                                                        getRegister(params),
                                                        generateDecoderKey(params),
                                                        getEncryptionAlgorithm(params));
            return Optional.of(generator.generate());

        } else if (getTokenType() == TokenType.PRISM_THRIFT) {
            co.nxtgrid.token.generators.tokensgenerator.prism.class2.ClearCreditTokenGenerator generator
                    = new co.nxtgrid.token.generators.tokensgenerator.prism.class2
                            .ClearCreditTokenGenerator(getRequestID(),
                                                        getHost(params),
                                                        getPort(params),
                                                        getRealm(params),
                                                        getUsername(params),
                                                        getPassword(params),
                                                        getIndividualAccountIdentificationNumber(params),
                                                        getEncryptionAlgorithm(params),
                                                        getTokenCarrierType(params),
                                                        getSupplyGroupCode(params),
                                                        getKeyRevisionNumber(params),
                                                        getKeyExpiryNumber(params),
                                                        getTariffIndex(params));
            return Optional.of(generator.generate().get(0));

        }
        throw new UnsupportedTokenTypeException();
    }
}
