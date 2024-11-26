package co.nxtgrid.tokens.service.impl.generate.class2;

import co.nxtgrid.hsm.prism.impl.exceptions.UnsupportedTokenTypeException;
import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.rate.Rate;
import co.nxtgrid.token.domain.token.class2.SetTariffRateToken;

import java.util.Map;
import java.util.Optional;

public class SetTariffRateTokenGenerator extends Class2Generator {

    public SetTariffRateTokenGenerator(String requestID, TokenType tokenType) {
        super(requestID, tokenType);
    }

    public Optional<SetTariffRateToken> generateSetTariffRateToken(Map<String, String> params) throws Exception {
        if (getTokenType() == TokenType.NATIVE) {
            co.nxtgrid.token.generators.tokensgenerator.nativetoken.class2.SetTariffRateTokenGenerator generator
                    = new co.nxtgrid.token.generators.tokensgenerator.nativetoken.class2
                            .SetTariffRateTokenGenerator(getRequestID(),
                                                        getRandomNo(params),
                                                        getTokenIdentifier(params),
                                                        getTariffRate(params),
                                                        generateDecoderKey(params),
                                                        getEncryptionAlgorithm(params));
            return Optional.of(generator.generate());

        } else if (getTokenType() == TokenType.PRISM_THRIFT) {
            co.nxtgrid.token.generators.tokensgenerator.prism.class2
                    .SetTariffRateTokenGenerator generator
                        = new co.nxtgrid.token.generators.tokensgenerator.prism.class2
                            .SetTariffRateTokenGenerator(getRequestID(),
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

    private static Rate getTariffRate(Map<String, String> params)
        throws Exception {
        BitString tariffRateBitString = new BitString(Long.parseLong(params.get("tariff_rate")));
        tariffRateBitString.setLength(16);
        return new Rate(tariffRateBitString);
    }
}
