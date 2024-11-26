package co.nxtgrid.token.generators.tokensgenerator.nativetoken.class2;

import co.nxtgrid.token.domain.Crc;
import co.nxtgrid.token.domain.MaximumPowerLimit;
import co.nxtgrid.token.domain.RandomNo;
import co.nxtgrid.token.domain.TokenIdentifier;
import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.encryptionalgorithm.EncryptionAlgorithm;
import co.nxtgrid.token.domain.keys.decoder.DecoderKey;
import co.nxtgrid.token.domain.token.class2.SetMaximumPowerLimitToken;
import co.nxtgrid.token.domain.tokenclass.class2.SetMaximumPowerLimitTokenClass;
import co.nxtgrid.token.domain.tokensubclass.class2.SetMaximumPowerLimitTokenSubClass;
import co.nxtgrid.token.exceptions.BitConcatOverflowError;
import co.nxtgrid.token.exceptions.InvalidBitStringException;
import co.nxtgrid.token.exceptions.InvalidRangeException;

import java.util.Optional;

public class SetMaximumPowerLimitTokenGenerator extends Class2TokenGenerator<SetMaximumPowerLimitToken> {

    private RandomNo randomNo;
    private TokenIdentifier tokenIdentifier;
    private MaximumPowerLimit maximumPowerLimit;

    public SetMaximumPowerLimitTokenGenerator(String requestID,
                                                RandomNo randomNo,
                                                TokenIdentifier tokenIdentifier,
                                                MaximumPowerLimit maximumPowerLimit,
                                                DecoderKey decoderKey,
                                                EncryptionAlgorithm encryptionAlgorithm) {
        super(requestID);
        setRandomNo(randomNo);
        setTokenIdentifier(tokenIdentifier);
        setMaximumPowerLimit(maximumPowerLimit);
        setDecoderKey(decoderKey);
        setEncryptionAlgorithm(encryptionAlgorithm);
    }

    public SetMaximumPowerLimitToken generate() throws Exception {
        BitString concat = maximumPowerLimit.getBitString().concat(tokenIdentifier.getBitString(),
                                                                                    randomNo.getBitString(),
                                                                                    getTokenSubClass().getBitString(),
                                                                                    getTokenClass().getBitString());
        BitString generatedCrcBitString = new Crc().generateCRC(concat);
        Crc generatedCrc = new Crc(generatedCrcBitString);
        SetMaximumPowerLimitToken token = new SetMaximumPowerLimitToken(requestID, Optional.of(randomNo), tokenIdentifier, maximumPowerLimit, Optional.of(generatedCrc));
        BitString _64BitString =  generatedCrcBitString.concat( maximumPowerLimit.getBitString(),
                                                                tokenIdentifier.getBitString(),
                                                                randomNo.getBitString(),
                                                                getTokenSubClass().getBitString());
        String encryptedToken = encrypt(getTokenClass(), _64BitString, getEncryptionAlgorithm());
        token.setEncryptedTokenBitString(encryptedToken);
        return token;
    }

    public BitString generate64BitDataBlock(SetMaximumPowerLimitToken token)
            throws BitConcatOverflowError, InvalidBitStringException, InvalidRangeException {
        BitString tokenClass = token.getTokenClass().getBitString();
        BitString tokenSubClass = token.getTokenSubClass().getBitString();
        BitString rnd = token.getRandomNo().get().getBitString();
        BitString tid = token.getTokenIdentifier().getBitString();
        BitString mpl = token.getMaximumPowerLimit().getBitString();
        BitString concatenated = mpl.concat(tid, rnd, tokenSubClass, tokenClass);
        BitString crc = new Crc().generateCRC(concatenated);
        BitString _64BitDataBlock = crc.concat(mpl, tid, rnd, tokenSubClass);
        return _64BitDataBlock;
    }

    public SetMaximumPowerLimitTokenClass getTokenClass() throws InvalidRangeException {
        return new SetMaximumPowerLimitTokenClass();
    }

    public SetMaximumPowerLimitTokenSubClass getTokenSubClass() throws InvalidRangeException {
        return new SetMaximumPowerLimitTokenSubClass();
    }

    public RandomNo getRandomNo() {
        return randomNo;
    }

    public void setRandomNo(RandomNo randomNo) {
        this.randomNo = randomNo;
    }

    public TokenIdentifier getTokenIdentifier() {
        return tokenIdentifier;
    }

    public void setTokenIdentifier(TokenIdentifier tokenIdentifier) {
        this.tokenIdentifier = tokenIdentifier;
    }

    public MaximumPowerLimit getMaximumPowerLimit() {
        return maximumPowerLimit;
    }

    public void setMaximumPowerLimit(MaximumPowerLimit maximumPowerLimit) {
        this.maximumPowerLimit = maximumPowerLimit;
    }
}
