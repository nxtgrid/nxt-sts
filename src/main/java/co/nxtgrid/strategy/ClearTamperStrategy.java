package co.nxtgrid.strategy;

import org.springframework.stereotype.Component;

import co.nxtgrid.api.TokenRequest;
import co.nxtgrid.api.TokenType;
import co.nxtgrid.token.domain.BaseDate;
import co.nxtgrid.token.domain.Pad;
import co.nxtgrid.token.domain.RandomNo;
import co.nxtgrid.token.domain.TokenIdentifier;
import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.encryptionalgorithm.StandardTransferAlgorithmEncryptionAlgorithm;
import co.nxtgrid.token.domain.keys.decoder.DecoderKey;
import co.nxtgrid.token.generators.tokensgenerator.nativetoken.class2.ClearTamperConditionTokenGenerator;

@Component
public class ClearTamperStrategy implements TokenStrategy {

    // STS IEC 62055-41: base date 2014 is the reference epoch for this token generation
    private static final BaseDate STS_BASE_DATE = BaseDate._2014;
    private static final int RANDOM_NO_BIT_LENGTH = 4;
    private static final String ZERO_PAD = "0000000000000000";
    private static final int PAD_BIT_LENGTH = 16;

    @Override
    public boolean supports(TokenType type) {
        return TokenType.CLEAR_TAMPER == type;
    }

    @Override
    public String generate(TokenRequest request) throws Exception {
        TokenIdentifier tokenIdentifier =
            new TokenIdentifier(StrategySupport.toJodaDateTime(request.getIssueDate()), STS_BASE_DATE);

        BitString randomValueBitString = new BitString((long) request.getRandomNumber());
        randomValueBitString.setLength(RANDOM_NO_BIT_LENGTH);
        RandomNo randomNo = new RandomNo(randomValueBitString);

        BitString padBitString = new BitString();
        padBitString.setValue(ZERO_PAD);
        padBitString.setLength(PAD_BIT_LENGTH);
        Pad pad = new Pad(padBitString);

        DecoderKey decoderKey = DecoderKey.fromHex(request.getDecoderKey());
        StandardTransferAlgorithmEncryptionAlgorithm staEncryptionAlgorithm =
            new StandardTransferAlgorithmEncryptionAlgorithm();

        ClearTamperConditionTokenGenerator tokenGenerator =
            new ClearTamperConditionTokenGenerator(
                StrategySupport.newRequestId(),
                randomNo,
                tokenIdentifier,
                pad,
                decoderKey,
                staEncryptionAlgorithm
            );

        return tokenGenerator.generate().getTokenNo();
    }
}
