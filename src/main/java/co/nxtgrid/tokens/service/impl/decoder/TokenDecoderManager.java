package co.nxtgrid.tokens.service.impl.decoder;

import co.nxtgrid.token.domain.keys.decoder.DecoderKey;
import co.nxtgrid.token.domain.token.Token;
import co.nxtgrid.token.generators.tokensdecoder.Meter;
import co.nxtgrid.tokens.service.impl.generate.DecoderKeyGeneratorManager;
import co.nxtgrid.tokens.service.impl.generate.Generator;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TokenDecoderManager {

    public Token decode(String requestID, String token, Map<String, String> params)
            throws Exception {
        Generator.TokenType type = getTokenType(params);
        if (type == Generator.TokenType.PRISM_THRIFT) {
            Meter meter = getPrismMeter(params);
            return meter.decodePrism(requestID,
                    Generator.getIndividualAccountIdentificationNumber(params),
                    Generator.getEncryptionAlgorithm(params),
                    Generator.getTokenCarrierType(params),
                    Generator.getSupplyGroupCode(params),
                    Generator.getKeyRevisionNumber(params),
                    Generator.getKeyExpiryNumber(params),
                    Generator.getTariffIndex(params),
                    token);
        } else {
            DecoderKeyGeneratorManager manager = new DecoderKeyGeneratorManager();
            manager.initializeDecoderKeyGenerationParameters(params);
            DecoderKey decoderKey = manager.generateDecoderKey(params);
            Meter meter = new Meter(token, decoderKey, manager.getEncryptionAlgorithm());
            return meter.decodeNative(requestID);
        }
    }

    private Generator.TokenType getTokenType(Map<String, String> params) {
        if (params.containsKey("type")) {
            if (params.get("type").trim()
                    .toLowerCase().equals("prism-thrift")) {
                return Generator.TokenType.PRISM_THRIFT;
            }
        }
        return Generator.TokenType.NATIVE;
    }

    private Meter getPrismMeter(Map<String, String> params) {
        String host = params.get("host");
        int port = Integer.parseInt(params.get("port"));
        String realm = params.get("realm");
        String username = params.get("username");
        String password = params.get("password");
        return new Meter(host, port, realm, username, password);
    }
}
