package co.nxtgrid.token.generators.tokensgenerator.nativetoken.class2;

import co.nxtgrid.token.domain.Crc;
import co.nxtgrid.token.domain.RandomNo;
import co.nxtgrid.token.domain.TokenIdentifier;
import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.encryptionalgorithm.EncryptionAlgorithm;
import co.nxtgrid.token.domain.keys.decoder.DecoderKey;
import co.nxtgrid.token.domain.rate.Rate;
import co.nxtgrid.token.domain.token.class2.SetTariffRateToken;
import co.nxtgrid.token.domain.tokenclass.class2.SetTariffRateTokenClass;
import co.nxtgrid.token.domain.tokensubclass.class2.SetTariffRateTokenSubClass;
import co.nxtgrid.token.exceptions.BitConcatOverflowError;
import co.nxtgrid.token.exceptions.InvalidRangeException;

import java.util.Optional;

public class SetTariffRateTokenGenerator extends Class2TokenGenerator<SetTariffRateToken> {

    private RandomNo randomNo;
    private TokenIdentifier tokenIdentifier;
    private Rate rate;

    public SetTariffRateTokenGenerator(String requestID,
                                       RandomNo randomNo,
                                       TokenIdentifier tokenIdentifier,
                                       Rate rate,
                                       DecoderKey decoderKey,
                                       EncryptionAlgorithm encryptionAlgorithm) {
        super(requestID);
        setRandomNo(randomNo);
        setTokenIdentifier(tokenIdentifier);
        setRate(rate);
        setDecoderKey(decoderKey);
        setEncryptionAlgorithm(encryptionAlgorithm);
    }

    public SetTariffRateToken generate() throws Exception {
        SetTariffRateToken token = new SetTariffRateToken(requestID, Optional.of(randomNo), tokenIdentifier, Optional.of(rate));
        BitString _64BitDataBlock = generate64BitDataBlock(token);
        BitString _64bitStringEncryptedBitString = encryptionAlgorithm.encrypt(getDecoderKey(), _64BitDataBlock);
        String _66bitStringEncryptedBlockAT = transpose66BitString(token.getTokenClass(), _64bitStringEncryptedBitString);
        token.setEncryptedTokenBitString(_66bitStringEncryptedBlockAT);
        return token;
    }

    public BitString generate64BitDataBlock(SetTariffRateToken token) throws BitConcatOverflowError {
        BitString tokenClass = token.getTokenClass().getBitString();
        BitString tokenSubClass = token.getTokenSubClass().getBitString();
        BitString rnd = token.getRandomNo().get().getBitString();
        BitString tid = token.getTokenIdentifier().getBitString();
        BitString rate = token.getRate().get().getRateBitString();
        Crc calcCRC = new Crc();
        BitString crc = calcCRC.generateCRC(rate.concat(tid, rnd, tokenSubClass, tokenClass));
        BitString _64BitDataBlock = crc.concat(rate, tid, rnd, tokenSubClass);
        return _64BitDataBlock;
    }

    public SetTariffRateTokenClass getTokenClass() throws InvalidRangeException {
        return new SetTariffRateTokenClass();
    }

    public SetTariffRateTokenSubClass getTokenSubClass() throws InvalidRangeException {
        return new SetTariffRateTokenSubClass();
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

    public Rate getRate() {
        return rate;
    }

    public void setRate(Rate rate) {
        this.rate = rate;
    }
}
