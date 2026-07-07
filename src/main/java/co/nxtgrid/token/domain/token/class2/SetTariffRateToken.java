package co.nxtgrid.token.domain.token.class2;

import co.nxtgrid.token.domain.RandomNo;
import co.nxtgrid.token.domain.TokenIdentifier;
import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.rate.InvalidRateException;
import co.nxtgrid.token.domain.rate.Rate;
import co.nxtgrid.token.domain.tokenclass.class2.ClearCreditTokenClass;
import co.nxtgrid.token.domain.tokenclass.class2.SetTariffRateTokenClass;
import co.nxtgrid.token.domain.tokensubclass.class2.ClearCreditTokenSubClass;
import co.nxtgrid.token.domain.tokensubclass.class2.SetTariffRateTokenSubClass;
import co.nxtgrid.token.exceptions.BitConcatOverflowError;
import co.nxtgrid.token.exceptions.InvalidDateTimeBitsException;
import co.nxtgrid.token.exceptions.InvalidRangeException;
import co.nxtgrid.token.exceptions.decode.CRCError;

import java.util.HashMap;
import java.util.Optional;

public class SetTariffRateToken extends Class2Token {

    private Optional<RandomNo> randomNo;
    private TokenIdentifier tokenIdentifier;
    private Optional<Rate> rate;

    public SetTariffRateToken(String requestID)
            throws InvalidRangeException {
        super(requestID);
        setTokenClass(new SetTariffRateTokenClass());
        setTokenSubClass(new SetTariffRateTokenSubClass());
    }

    public SetTariffRateToken(String requestID,
                              Optional<RandomNo> randomNo,
                              TokenIdentifier tokenIdentifier,
                              Optional<Rate> rate)
            throws InvalidRangeException {
        super(requestID);
        setTokenClass(new SetTariffRateTokenClass());
        setTokenSubClass(new SetTariffRateTokenSubClass());
        setRandomNo(randomNo);
        setTokenIdentifier(tokenIdentifier);
        setRate(rate);
    }

    @Override
    public String getType() {
        return "SetTariffRate_22";
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

    public Optional<Rate> getRate() {
        return rate;
    }

    public void setRate(Optional<Rate> rate) {
        this.rate = rate;
    }

    @Override
    public HashMap<String, Object> getParams() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("class", getTokenClass().getBitString().getValue());
        params.put("subclass", getTokenSubClass().getBitString().getValue());
        params.put("token_identifier", getTokenIdentifier().getTimeOfIssue().toString());
        params.put("type", getType());

        if (getRate().isPresent())
            params.put("tariff_rate", getRate().get().getRateBitString().getValue());

        if (getRandomNo().isPresent())
            params.put("rnd", getRandomNo().get().getBitString().getValue());

        return params;
    }

    public void decode(BitString decryptedTokenBitString,
                       BitString encryptedTokenBitString) throws
            CRCError,
            InvalidRangeException,
            InvalidDateTimeBitsException,
            InvalidRateException, BitConcatOverflowError {
        if (checkCrc(decryptedTokenBitString, getTokenClass())) {
            setRandomNo(extractRandomNo(decryptedTokenBitString));
            setTokenIdentifier(extractTokenIdentifier(decryptedTokenBitString));
            setRate(Optional.of(new Rate(decryptedTokenBitString.extractBits(16, 16))));
            setEncryptedTokenBitString(Long.toBinaryString(encryptedTokenBitString.getValue()));
            setDecryptedTokenBitString(Long.toBinaryString(decryptedTokenBitString.getValue()));
        }
    }
}
