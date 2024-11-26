package co.nxtgrid.token.domain.token.class2;

import co.nxtgrid.token.domain.RandomNo;
import co.nxtgrid.token.domain.TokenIdentifier;
import co.nxtgrid.token.domain.WaterMeterFactor;
import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.tokenclass.class2.SetWaterMeterFactorTokenClass;
import co.nxtgrid.token.domain.tokensubclass.class2.SetWaterMeterFactorTokenSubClass;
import co.nxtgrid.token.exceptions.BitConcatOverflowError;
import co.nxtgrid.token.exceptions.InvalidDateTimeBitsException;
import co.nxtgrid.token.exceptions.InvalidRangeException;
import co.nxtgrid.token.exceptions.InvalidWMFException;
import co.nxtgrid.token.generators.tokensdecoder.error.CRCError;

import java.util.HashMap;
import java.util.Optional;

public class SetWaterMeterFactorToken extends Class2Token {

    private Optional<RandomNo> randomNo;
    private TokenIdentifier tokenIdentifier;
    private Optional<WaterMeterFactor> waterMeterFactor;

    public SetWaterMeterFactorToken(String requestID)
            throws InvalidRangeException {
        super(requestID);
        setTokenClass(new SetWaterMeterFactorTokenClass());
        setTokenSubClass(new SetWaterMeterFactorTokenSubClass());
    }

    public SetWaterMeterFactorToken(String requestID,
                                    Optional<RandomNo> randomValue,
                                    TokenIdentifier tokenIdentifier,
                                    Optional<WaterMeterFactor> waterMeterFactor)
            throws InvalidRangeException {
        super(requestID);
        setTokenClass(new SetWaterMeterFactorTokenClass());
        setTokenSubClass(new SetWaterMeterFactorTokenSubClass());
        setRandomNo(randomValue);
        setTokenIdentifier(tokenIdentifier);
        setWaterMeterFactor(waterMeterFactor);
    }

    @Override
    public String getType() {
        return "SetWaterMeterFactor_27";
    }

    public String getBitString() {
        return encryptedTokenBitString;
    }

    public Optional<RandomNo> getRandomNo() {
        return randomNo;
    }

    public void setRandomNo(Optional<RandomNo> randomNo) {
        this.randomNo = randomNo;
    }

    public TokenIdentifier getTokenIdentifier() {
        return tokenIdentifier;
    }

    public void setTokenIdentifier(TokenIdentifier tokenIdentifier) {
        this.tokenIdentifier = tokenIdentifier;
    }

    public Optional<WaterMeterFactor> getWaterMeterFactor() {
        return waterMeterFactor;
    }

    public void setWaterMeterFactor(Optional<WaterMeterFactor> waterMeterFactor) {
        this.waterMeterFactor = waterMeterFactor;
    }

    @Override
    public HashMap<String, Object> getParams() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("class", getTokenClass().getBitString().getValue());
        params.put("subclass", getTokenSubClass().getBitString().getValue());
        params.put("token_identifier", getTokenIdentifier().getTimeOfIssue().toString());
        params.put("type", getType());

        if (getWaterMeterFactor().isPresent())
            params.put("tariff_rate", getWaterMeterFactor().get().getWmfBitString().getValue());

        if (getRandomNo().isPresent())
            params.put("rnd", getRandomNo().get().getBitString().getValue());

        return params;
    }

    public void decode(BitString decryptedTokenBitString,
                       BitString encryptedTokenBitString) throws
            CRCError, InvalidRangeException,
            InvalidDateTimeBitsException,
            InvalidWMFException, BitConcatOverflowError {
        if (checkCrc(decryptedTokenBitString, getTokenClass())) {
            setRandomNo(extractRandomNo(decryptedTokenBitString));
            setTokenIdentifier(extractTokenIdentifier(decryptedTokenBitString));
            setWaterMeterFactor(Optional.of(
                    new WaterMeterFactor(decryptedTokenBitString.extractBits(16, 16))));
            setCrc(Optional.of(extractCrc(decryptedTokenBitString)));
            setEncryptedTokenBitString(Long.toBinaryString(encryptedTokenBitString.getValue()));
            setDecryptedTokenBitString(Long.toBinaryString(decryptedTokenBitString.getValue()));
        }
    }
}
