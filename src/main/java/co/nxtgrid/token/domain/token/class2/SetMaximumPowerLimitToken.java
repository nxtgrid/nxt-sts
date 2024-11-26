package co.nxtgrid.token.domain.token.class2;

import co.nxtgrid.token.domain.Crc;
import co.nxtgrid.token.domain.MaximumPowerLimit;
import co.nxtgrid.token.domain.RandomNo;
import co.nxtgrid.token.domain.TokenIdentifier;
import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.tokenclass.class2.SetMaximumPowerLimitTokenClass;
import co.nxtgrid.token.domain.tokensubclass.class2.SetMaximumPowerLimitTokenSubClass;
import co.nxtgrid.token.exceptions.*;
import co.nxtgrid.token.generators.tokensdecoder.error.CRCError;

import java.util.HashMap;
import java.util.Optional;

public class SetMaximumPowerLimitToken extends Class2Token {

    private Optional<RandomNo> randomNo;
    private TokenIdentifier tokenIdentifier;
    private MaximumPowerLimit maximumPowerLimit;

    public SetMaximumPowerLimitToken(String requestID)
            throws InvalidRangeException {
        super(requestID);
        setTokenClass(new SetMaximumPowerLimitTokenClass());
        setTokenSubClass(new SetMaximumPowerLimitTokenSubClass());
    }

    public SetMaximumPowerLimitToken(String requestID,
                                     Optional<RandomNo> randomNo,
                                     TokenIdentifier tokenIdentifier,
                                     MaximumPowerLimit maximumPowerLimit,
                                     Optional<Crc> crc)
            throws InvalidRangeException {
        super(requestID);
        setTokenClass(new SetMaximumPowerLimitTokenClass());
        setTokenSubClass(new SetMaximumPowerLimitTokenSubClass());
        setRandomNo(randomNo);
        setTokenIdentifier(tokenIdentifier);
        setMaximumPowerLimit(maximumPowerLimit);
        setCrc(crc);
    }

    @Override
    public String getType() {
        return "SetMaximumPowerLimit_20";
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

    public MaximumPowerLimit getMaximumPowerLimit() {
        return maximumPowerLimit;
    }

    public void setMaximumPowerLimit(MaximumPowerLimit maximumPowerLimit) {
        this.maximumPowerLimit = maximumPowerLimit;
    }

    @Override
    public HashMap<String, Object> getParams() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("class", getTokenClass().getBitString().getValue());
        params.put("subclass", getTokenSubClass().getBitString().getValue());
        params.put("token_identifier", getTokenIdentifier().getTimeOfIssue().toString());
        params.put("type", getType());
        params.put("maximum_power_limit", getMaximumPowerLimit().getMaximumPowerLimit());

        if (getRandomNo().isPresent())
            params.put("rnd", getRandomNo().get().getBitString().getValue());

        return params;
    }

    public void decode(BitString decryptedTokenBitString,
                       BitString encryptedTokenBitString) throws
            CRCError, InvalidRangeException,InvalidDateTimeBitsException, InvalidUnitsPurchasedException,
            InvalidBitStringException, InvalidMPLException, BitConcatOverflowError {
        if (checkCrc(decryptedTokenBitString, getTokenClass())) {
            setRandomNo(extractRandomNo(decryptedTokenBitString));
            setTokenIdentifier(extractTokenIdentifier(decryptedTokenBitString));
            setMaximumPowerLimit(new MaximumPowerLimit(decryptedTokenBitString.extractBits(16, 16).getValue()));
            setCrc(Optional.of(extractCrc(decryptedTokenBitString)));
            setDecryptedTokenBitString(Long.toBinaryString(decryptedTokenBitString.getValue()));
            setEncryptedTokenBitString(Long.toBinaryString(encryptedTokenBitString.getValue()));
        }
    }
}
