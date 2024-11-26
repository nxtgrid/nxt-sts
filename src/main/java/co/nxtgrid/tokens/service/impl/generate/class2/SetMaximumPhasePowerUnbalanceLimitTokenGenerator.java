package co.nxtgrid.tokens.service.impl.generate.class2;

import co.nxtgrid.hsm.prism.impl.exceptions.UnsupportedTokenTypeException;
import co.nxtgrid.token.domain.MaximumPhasePowerUnbalanceLimit;
import co.nxtgrid.token.domain.token.class2.SetMaximumPhasePowerUnbalanceLimitToken;

import java.util.Map;
import java.util.Optional;

public class SetMaximumPhasePowerUnbalanceLimitTokenGenerator extends Class2Generator {

    public SetMaximumPhasePowerUnbalanceLimitTokenGenerator(String requestID, TokenType tokenType) {
        super(requestID, tokenType);
    }

    public Optional<SetMaximumPhasePowerUnbalanceLimitToken>
            generateSetMaximumPhasePowerUnbalanceLimitToken(Map<String, String> params) throws Exception {
        if (getTokenType() == TokenType.NATIVE) {
            co.nxtgrid.token.generators.tokensgenerator.nativetoken.class2.SetMaximumPhasePowerUnbalanceLimitTokenGenerator generator
                    = new co.nxtgrid.token.generators.tokensgenerator.nativetoken.class2
                            .SetMaximumPhasePowerUnbalanceLimitTokenGenerator(getRequestID(),
                                                                                getRandomNo(params),
                                                                                getTokenIdentifier(params),
                                                                                Optional.of(getMaximumPhasePowerUnbalanceLimit(params)),
                                                                                generateDecoderKey(params),
                                                                                getEncryptionAlgorithm(params));
            return Optional.of(generator.generate());

        } else if (getTokenType() == TokenType.PRISM_THRIFT) {
            co.nxtgrid.token.generators.tokensgenerator.prism.class2
                        .SetMaximumPhasePowerUnbalanceLimitTokenGenerator generator
                    = new co.nxtgrid.token.generators.tokensgenerator.prism.class2
                            .SetMaximumPhasePowerUnbalanceLimitTokenGenerator(getRequestID(),
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

    private static MaximumPhasePowerUnbalanceLimit getMaximumPhasePowerUnbalanceLimit(Map<String, String> params)
            throws Exception {
        String mppul = params.get("mppul");
        return new MaximumPhasePowerUnbalanceLimit(Long.parseLong(mppul));
    }
}
