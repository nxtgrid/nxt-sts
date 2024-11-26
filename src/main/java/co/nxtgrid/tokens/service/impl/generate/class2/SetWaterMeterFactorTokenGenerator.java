package co.nxtgrid.tokens.service.impl.generate.class2;

import co.nxtgrid.hsm.prism.impl.exceptions.UnsupportedTokenTypeException;
import co.nxtgrid.token.domain.WaterMeterFactor;
import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.token.class2.SetWaterMeterFactorToken;

import java.util.Map;
import java.util.Optional;

public class SetWaterMeterFactorTokenGenerator extends Class2Generator {


    public SetWaterMeterFactorTokenGenerator(String requestID, TokenType tokenType) {
        super(requestID, tokenType);
    }

    public Optional<SetWaterMeterFactorToken> generateSetWaterMeterFactorToken(Map<String, String> params)
            throws Exception {
        if (getTokenType() == TokenType.NATIVE) {
            co.nxtgrid.token.generators.tokensgenerator.nativetoken.class2.SetWaterMeterFactorTokenGenerator generator
                    = new co.nxtgrid.token.generators.tokensgenerator.nativetoken.class2
                            .SetWaterMeterFactorTokenGenerator(getRequestID(),
                                                                getRandomNo(params),
                                                                getTokenIdentifier(params),
                                                                getWaterMeterFactor(params),
                                                                generateDecoderKey(params),
                                                                getEncryptionAlgorithm(params));
            return Optional.of(generator.generate());

        } else if (getTokenType() == TokenType.PRISM_THRIFT) {
            co.nxtgrid.token.generators.tokensgenerator.prism.class2
                    .SetWaterMeterFactorTokenGenerator generator
                        = new co.nxtgrid.token.generators.tokensgenerator.prism.class2
                    .SetWaterMeterFactorTokenGenerator(getRequestID(),
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

    public static WaterMeterFactor getWaterMeterFactor(Map<String, String> params) throws Exception {
        String wmf = params.get("wm_factor");
        BitString waterMeterFactorBitString = new BitString(Long.parseLong(wmf));
        waterMeterFactorBitString.setLength(16);
       return new WaterMeterFactor(waterMeterFactorBitString);
    }
}
