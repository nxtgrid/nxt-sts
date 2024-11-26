package co.nxtgrid.token.generators.tokensgenerator.nativetoken.class2;

import co.nxtgrid.token.domain.Crc;
import co.nxtgrid.token.domain.MaximumPhasePowerUnbalanceLimit;
import co.nxtgrid.token.domain.RandomNo;
import co.nxtgrid.token.domain.TokenIdentifier;
import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.encryptionalgorithm.EncryptionAlgorithm;
import co.nxtgrid.token.domain.keys.decoder.DecoderKey;
import co.nxtgrid.token.domain.token.class2.SetMaximumPhasePowerUnbalanceLimitToken;
import co.nxtgrid.token.exceptions.BitConcatOverflowError;
import co.nxtgrid.token.exceptions.InvalidRangeException;

import java.util.Optional;

public class SetMaximumPhasePowerUnbalanceLimitTokenGenerator
        extends Class2TokenGenerator<SetMaximumPhasePowerUnbalanceLimitToken> {

    private RandomNo randomValue;
    private TokenIdentifier tokenIdentifier;
    private Optional<MaximumPhasePowerUnbalanceLimit> maximumPhasePowerUnbalanceLimit;
    private Crc crc;
    private DecoderKey decoderKey;
    private EncryptionAlgorithm encryptionAlgorithm;

    public SetMaximumPhasePowerUnbalanceLimitTokenGenerator(String requestID,
                                                            RandomNo randomValue,
                                                            TokenIdentifier tokenIdentifier,
                                                            Optional<MaximumPhasePowerUnbalanceLimit> maximumPhasePowerUnbalanceLimit,
                                                            DecoderKey decoderKey,
                                                            EncryptionAlgorithm encryptionAlgorithm) {
        super(requestID);
        setRandomValue(randomValue);
        setTokenIdentifier(tokenIdentifier);
        setMaximumPhasePowerUnbalanceLimit(maximumPhasePowerUnbalanceLimit);
        setDecoderKey(decoderKey);
        setEncryptionAlgorithm(encryptionAlgorithm);
    }

    public SetMaximumPhasePowerUnbalanceLimitToken generate() throws Exception {
        SetMaximumPhasePowerUnbalanceLimitToken token = new SetMaximumPhasePowerUnbalanceLimitToken(requestID,
                                                                                                    Optional.of(randomValue),
                                                                                                    tokenIdentifier,
                                                                                                    maximumPhasePowerUnbalanceLimit);
        BitString _64BitDataBlock = generate64BitDataBlock(token);
        BitString _64bitStringEncryptedBitString = getEncryptionAlgorithm().encrypt(getDecoderKey(), _64BitDataBlock);
        String _66bitStringEncryptedBlockAT = transpose66BitString(token.getTokenClass(), _64bitStringEncryptedBitString);
        token.setEncryptedTokenBitString(_66bitStringEncryptedBlockAT);
        return token;
    }

    public BitString generate64BitDataBlock(SetMaximumPhasePowerUnbalanceLimitToken token)
            throws BitConcatOverflowError, InvalidRangeException {
        BitString tokenClass = token.getTokenClass().getBitString();
        BitString tokenSubClass = token.getTokenSubClass().getBitString();
        BitString random = token.getRandomNo().get().getBitString();
        BitString tid = token.getTokenIdentifier().getBitString();
        BitString mppul = token.getMaximumPhasePowerUnbalanceLimit().get().getMppulBitString();
        BitString concatenated = mppul.concat(tid, random, tokenSubClass, tokenClass);
        BitString generateCrc = new Crc().generateCRC(concatenated);
        setCrc(new Crc(generateCrc));
        BitString _64BitDataBlock = generateCrc.concat(mppul, tid, random, tokenSubClass);
        return _64BitDataBlock;
    }

    public RandomNo getRandomValue() {
        return randomValue;
    }

    public void setRandomValue(RandomNo randomValue) {
        this.randomValue = randomValue;
    }

    public TokenIdentifier getTokenIdentifier() {
        return tokenIdentifier;
    }

    public void setTokenIdentifier(TokenIdentifier tokenIdentifier) {
        this.tokenIdentifier = tokenIdentifier;
    }

    public Optional<MaximumPhasePowerUnbalanceLimit> getMaximumPhasePowerUnbalanceLimit() {
        return maximumPhasePowerUnbalanceLimit;
    }

    public void setMaximumPhasePowerUnbalanceLimit(Optional<MaximumPhasePowerUnbalanceLimit> maximumPhasePowerUnbalanceLimit) {
        this.maximumPhasePowerUnbalanceLimit = maximumPhasePowerUnbalanceLimit;
    }

    public Crc getCrc() {
        return crc;
    }

    public void setCrc(Crc crc) {
        this.crc = crc;
    }

    public DecoderKey getDecoderKey() {
        return decoderKey;
    }

    public void setDecoderKey(DecoderKey decoderKey) {
        this.decoderKey = decoderKey;
    }

    public EncryptionAlgorithm getEncryptionAlgorithm() {
        return encryptionAlgorithm;
    }

    public void setEncryptionAlgorithm(EncryptionAlgorithm encryptionAlgorithm) {
        this.encryptionAlgorithm = encryptionAlgorithm;
    }
}
