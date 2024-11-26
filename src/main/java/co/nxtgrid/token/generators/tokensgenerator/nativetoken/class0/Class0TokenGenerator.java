package co.nxtgrid.token.generators.tokensgenerator.nativetoken.class0;

import co.nxtgrid.token.domain.*;
import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.encryptionalgorithm.EncryptionAlgorithm;
import co.nxtgrid.token.domain.keys.decoder.DecoderKey;
import co.nxtgrid.token.domain.token.class0.Class0Token;
import co.nxtgrid.token.exceptions.BitConcatOverflowError;
import co.nxtgrid.token.exceptions.InvalidRangeException;
import co.nxtgrid.token.generators.tokensgenerator.nativetoken.TokenGenerator;

import java.util.Optional;

public abstract class Class0TokenGenerator extends TokenGenerator<Class0Token>  {

    protected TokenIdentifier tokenIdentifier;
    protected RandomNo randomValue;
    protected Amount amountPurchased;
    protected DecoderKey decoderKey;
    protected KeyExpiryNumber keyExpiryNumber;

    public Class0TokenGenerator(String requestID,
                                TokenIdentifier tokenIdentifier,
                                RandomNo randomValue,
                                Amount amountPurchased,
                                KeyExpiryNumber keyExpiryNumber,
                                DecoderKey decoderKey,
                                EncryptionAlgorithm encryptionAlgorithm) {
        super(requestID);
        setTokenIdentifier(tokenIdentifier);
        setRandomValue(randomValue);
        setAmountPurchased(amountPurchased);
        setKeyExpiryNumber(keyExpiryNumber);
        setDecoderKey(decoderKey);
        setEncryptionAlgorithm(encryptionAlgorithm);
    }

    public TokenIdentifier getTokenIdentifier() {
        return tokenIdentifier;
    }

    public void setTokenIdentifier(TokenIdentifier tokenIdentifier) {
        this.tokenIdentifier = tokenIdentifier;
    }

    public RandomNo getRandomValue() {
        return randomValue;
    }

    public void setRandomValue(RandomNo randomValue) {
        this.randomValue = randomValue;
    }

    public Amount getAmountPurchased() {
        return amountPurchased;
    }

    public void setAmountPurchased(Amount amountPurchased) {
        this.amountPurchased = amountPurchased;
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

    @Override
    public BitString generate64BitDataBlock(Class0Token token)
            throws BitConcatOverflowError, InvalidRangeException {

        BitString tokenClass = token.getTokenClass().getBitString();
        BitString tokenSubClass = token.getTokenSubClass().getBitString();
        BitString random = token.getRandomNo().get().getBitString();
        BitString tokenIdentifier = getTokenIdentifier().getBitString();
        BitString amountPurchased = getAmountPurchased().getBitString();

        Crc calcCRC = new Crc();
        BitString crc = calcCRC.generateCRC(amountPurchased.concat(tokenIdentifier, random, tokenSubClass, tokenClass));
        token.setCrc(Optional.of(new Crc(crc)));

        BitString _64BitDataBlock = crc.concat(amountPurchased, tokenIdentifier, random, tokenSubClass);
        return _64BitDataBlock;
    }

    public KeyExpiryNumber getKeyExpiryNumber() {
        return keyExpiryNumber;
    }

    public void setKeyExpiryNumber(KeyExpiryNumber keyExpiryNumber) {
        this.keyExpiryNumber = keyExpiryNumber;
    }
}
