package co.nxtgrid.strategy;

import org.springframework.stereotype.Component;

import co.nxtgrid.api.TokenRequest;
import co.nxtgrid.api.TokenType;
import co.nxtgrid.token.domain.Amount;
import co.nxtgrid.token.domain.BaseDate;
import co.nxtgrid.token.domain.KeyExpiryNumber;
import co.nxtgrid.token.domain.RandomNo;
import co.nxtgrid.token.domain.TokenIdentifier;
import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.encryptionalgorithm.StandardTransferAlgorithmEncryptionAlgorithm;
import co.nxtgrid.token.domain.keys.decoder.DecoderKey;
import co.nxtgrid.token.generators.tokensgenerator.nativetoken.class0.TransferElectricityCreditTokenGenerator;

@Component
public class TransferElectricityCreditStrategy implements TokenStrategy {

    // STS IEC 62055-41: base date 2014 is the reference epoch for this token generation
    private static final BaseDate STS_BASE_DATE = BaseDate._2014;
    // Maximum key expiry: value 255 means "no expiry" per the STS standard
    private static final int KEY_EXPIRY_NO_EXPIRY = 255;
    private static final int RANDOM_NO_BIT_LENGTH = 4;

    @Override
    public boolean supports(TokenType type) {
        return TokenType.TOP_UP == type;
    }

    @Override
    public String generate(TokenRequest request) throws Exception {
        TokenIdentifier tokenIdentifier =
            new TokenIdentifier(StrategySupport.toJodaDateTime(request.getIssueDate()), STS_BASE_DATE);

        BitString randomValueBitString = new BitString((long) request.getRandomNumber());
        randomValueBitString.setLength(RANDOM_NO_BIT_LENGTH);
        RandomNo randomNo = new RandomNo(randomValueBitString);

        Amount amountPurchased = new Amount(request.getKwh());
        DecoderKey decoderKey = DecoderKey.fromHex(request.getDecoderKey());
        KeyExpiryNumber keyExpiryNumber = new KeyExpiryNumber(KEY_EXPIRY_NO_EXPIRY);
        StandardTransferAlgorithmEncryptionAlgorithm staEncryptionAlgorithm =
            new StandardTransferAlgorithmEncryptionAlgorithm();

        TransferElectricityCreditTokenGenerator tokenGenerator =
            new TransferElectricityCreditTokenGenerator(
                StrategySupport.newRequestId(),
                tokenIdentifier,
                randomNo,
                amountPurchased,
                keyExpiryNumber,
                decoderKey,
                staEncryptionAlgorithm
            );

        return tokenGenerator.generate().getTokenNo();
    }
}
