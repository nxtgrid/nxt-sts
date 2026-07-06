package co.nxtgrid.strategy;

import org.springframework.stereotype.Component;

import co.nxtgrid.api.TokenRequest;
import co.nxtgrid.token.domain.BaseDate;
import co.nxtgrid.token.domain.RandomNo;
import co.nxtgrid.token.domain.Register;
import co.nxtgrid.token.domain.TokenIdentifier;
import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.encryptionalgorithm.StandardTransferAlgorithmEncryptionAlgorithm;
import co.nxtgrid.token.domain.keys.decoder.DecoderKey;
import co.nxtgrid.token.generators.tokensgenerator.nativetoken.class2.ClearCreditTokenGenerator;

@Component
public class ClearCreditStrategy implements TokenStrategy {

    // STS IEC 62055-41: base date 2014 is the reference epoch for this token generation
    private static final BaseDate STS_BASE_DATE = BaseDate._2014;
    private static final String REQUEST_ID = "asda";
    private static final int RANDOM_NO_BIT_LENGTH = 4;
    private static final String ZERO_REGISTER = "0000000000000000";
    private static final int REGISTER_BIT_LENGTH = 16;

    @Override
    public boolean supports(String type) {
        return "CLEAR_CREDIT".equals(type);
    }

    @Override
    public String generate(TokenRequest request) throws Exception {
        TokenIdentifier tokenIdentifier = new TokenIdentifier(request.getIssueDate(), STS_BASE_DATE);

        BitString randomValueBitString = new BitString((long) request.getRandomNumber());
        randomValueBitString.setLength(RANDOM_NO_BIT_LENGTH);
        RandomNo randomNo = new RandomNo(randomValueBitString);

        BitString registerBitString = new BitString();
        registerBitString.setValue(ZERO_REGISTER);
        registerBitString.setLength(REGISTER_BIT_LENGTH);
        Register register = new Register(registerBitString);

        DecoderKey decoderKey = DecoderKey.fromHex(request.getDecoderKey());
        StandardTransferAlgorithmEncryptionAlgorithm staEncryptionAlgorithm =
            new StandardTransferAlgorithmEncryptionAlgorithm();

        ClearCreditTokenGenerator tokenGenerator = new ClearCreditTokenGenerator(
            REQUEST_ID,
            randomNo,
            tokenIdentifier,
            register,
            decoderKey,
            staEncryptionAlgorithm
        );

        return tokenGenerator.generate().getTokenNo();
    }
}
