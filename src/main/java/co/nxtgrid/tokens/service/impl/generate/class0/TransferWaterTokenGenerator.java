package co.nxtgrid.tokens.service.impl.generate.class0;

import co.nxtgrid.hsm.prism.impl.exceptions.UnsupportedTokenTypeException;
import co.nxtgrid.token.domain.token.class0.TransferWaterCreditToken;
import co.nxtgrid.token.generators.tokensgenerator.nativetoken.class0.TransferWaterCreditTokenGenerator;
import co.nxtgrid.token.generators.tokensgenerator.prism.class0.TransferWaterCreditPrismTokenGenerator;

import java.util.Map;
import java.util.Optional;

public class TransferWaterTokenGenerator extends Class0Generator {

    public TransferWaterTokenGenerator(String requestID, TokenType tokenType) {
        super(requestID, tokenType);
    }

    public Optional<TransferWaterCreditToken> generateWaterToken(
            Map<String, String> params) throws Exception {
        if (getTokenType() == TokenType.NATIVE) {
            TransferWaterCreditTokenGenerator generator
                    = new TransferWaterCreditTokenGenerator(getRequestID(),
                                                            getTokenIdentifier(params),
                                                            getRandomNo(params),
                                                            getAmount(params),
                                                            getKeyExpiryNumber(params),
                                                            generateDecoderKey(params),
                                                            getEncryptionAlgorithm(params));
            return Optional.of(generator.generate());

        } else if (getTokenType() == TokenType.PRISM_THRIFT) {
            TransferWaterCreditPrismTokenGenerator
                    generator = new TransferWaterCreditPrismTokenGenerator(getRequestID(),
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
                                                                            getAmount(params),
                                                                            getKeyExpiryNumber(params),
                                                                            getTariffIndex(params));
            return Optional.of(generator.generate().get(0));

        }
        throw new UnsupportedTokenTypeException();
    }
}
