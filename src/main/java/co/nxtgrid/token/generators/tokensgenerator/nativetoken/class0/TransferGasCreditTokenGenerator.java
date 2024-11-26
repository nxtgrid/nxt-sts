package co.nxtgrid.token.generators.tokensgenerator.nativetoken.class0;

import co.nxtgrid.token.domain.Amount;
import co.nxtgrid.token.domain.KeyExpiryNumber;
import co.nxtgrid.token.domain.RandomNo;
import co.nxtgrid.token.domain.TokenIdentifier;
import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.encryptionalgorithm.EncryptionAlgorithm;
import co.nxtgrid.token.domain.keys.decoder.DecoderKey;
import co.nxtgrid.token.domain.token.class0.TransferGasCreditToken;
import co.nxtgrid.token.generators.utils.Utils;

import java.util.Optional;

public class TransferGasCreditTokenGenerator extends Class0TokenGenerator {

    public TransferGasCreditTokenGenerator(String requestID,
                                           TokenIdentifier tokenIdentifier,
                                           RandomNo randomValue,
                                           Amount amountPurchased,
                                           KeyExpiryNumber keyExpiryNumber,
                                           DecoderKey decoderKey,
                                           EncryptionAlgorithm encryptionAlgorithm) {
        super(requestID, tokenIdentifier, randomValue, amountPurchased,
                keyExpiryNumber, decoderKey, encryptionAlgorithm);
    }

    public TransferGasCreditToken generate() throws Exception {
        Utils.validateTokenIdentifier(tokenIdentifier, keyExpiryNumber);
        TransferGasCreditToken token = new TransferGasCreditToken(getRequestID(),
                                                                    getTokenIdentifier(),
                                                                    Optional.of(getRandomValue()),
                                                                    getAmountPurchased());
        BitString _64BitDataBlock = generate64BitDataBlock(token);
        BitString _64bitStringEncryptedBitString = encryptionAlgorithm.encrypt(decoderKey, _64BitDataBlock);
        String _66bitStringEncryptedBlockAT = transpose66BitString(token.getTokenClass(), _64bitStringEncryptedBitString);
        token.setEncryptedTokenBitString(_66bitStringEncryptedBlockAT);
        return token;
    }
}
