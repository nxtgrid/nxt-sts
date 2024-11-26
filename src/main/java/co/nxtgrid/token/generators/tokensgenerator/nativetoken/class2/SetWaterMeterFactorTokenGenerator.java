package co.nxtgrid.token.generators.tokensgenerator.nativetoken.class2;


import co.nxtgrid.token.domain.Crc;
import co.nxtgrid.token.domain.RandomNo;
import co.nxtgrid.token.domain.TokenIdentifier;
import co.nxtgrid.token.domain.WaterMeterFactor;
import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.encryptionalgorithm.EncryptionAlgorithm;
import co.nxtgrid.token.domain.keys.decoder.DecoderKey;
import co.nxtgrid.token.domain.token.class2.SetWaterMeterFactorToken;
import co.nxtgrid.token.domain.tokenclass.class2.SetWaterMeterFactorTokenClass;
import co.nxtgrid.token.domain.tokensubclass.class2.SetWaterMeterFactorTokenSubClass;
import co.nxtgrid.token.exceptions.BitConcatOverflowError;
import co.nxtgrid.token.exceptions.InvalidRangeException;

import java.util.Optional;

public class SetWaterMeterFactorTokenGenerator extends Class2TokenGenerator<SetWaterMeterFactorToken> {

    private RandomNo randomNo;
    private TokenIdentifier tokenIdentifier;
    private WaterMeterFactor waterMeterFactor;

    public SetWaterMeterFactorTokenGenerator(String requestID,
                                             RandomNo randomNo,
                                             TokenIdentifier tokenIdentifier,
                                             WaterMeterFactor waterMeterFactor,
                                             DecoderKey decoderKey,
                                             EncryptionAlgorithm encryptionAlgorithm) {
        super(requestID);
        setRandomNo(randomNo);
        setTokenIdentifier(tokenIdentifier);
        setWaterMeterFactor(waterMeterFactor);
        setDecoderKey(decoderKey);
        setEncryptionAlgorithm(encryptionAlgorithm);
    }

    public SetWaterMeterFactorToken generate() throws Exception {
        SetWaterMeterFactorToken token = new SetWaterMeterFactorToken(requestID, Optional.of(randomNo), tokenIdentifier, Optional.of(waterMeterFactor));
        BitString _64BitDataBlock = generate64BitDataBlock(token);
        BitString _64bitStringEncryptedBitString = getEncryptionAlgorithm().encrypt(getDecoderKey(), _64BitDataBlock);
        String _66bitStringEncryptedBlockAT = transpose66BitString(token.getTokenClass(), _64bitStringEncryptedBitString);
        token.setEncryptedTokenBitString(_66bitStringEncryptedBlockAT);
        return token;
    }

    public BitString generate64BitDataBlock(SetWaterMeterFactorToken token)
            throws BitConcatOverflowError {
        BitString tokenClass = token.getTokenClass().getBitString();
        BitString tokenSubClass = token.getTokenSubClass().getBitString();
        BitString rnd = token.getRandomNo().get().getBitString();
        BitString tid = token.getTokenIdentifier().getBitString();
        BitString waterMeterFactor = token.getWaterMeterFactor().get().getWmfBitString();
        BitString crc = new Crc().generateCRC(waterMeterFactor.concat(tid, rnd, tokenSubClass, tokenClass));
        BitString _64BitDataBlock = crc.concat(waterMeterFactor, tid, rnd, tokenSubClass);
        return _64BitDataBlock;
    }

    public SetWaterMeterFactorTokenClass getTokenClass() throws InvalidRangeException {
        return new SetWaterMeterFactorTokenClass();
    }

    public SetWaterMeterFactorTokenSubClass getTokenSubClass() throws InvalidRangeException {
        return new SetWaterMeterFactorTokenSubClass();
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

    public WaterMeterFactor getWaterMeterFactor() {
        return waterMeterFactor;
    }

    public void setWaterMeterFactor(WaterMeterFactor waterMeterFactor) {
        this.waterMeterFactor = waterMeterFactor;
    }
}
