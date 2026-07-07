package co.nxtgrid.token.domain.token.class2;

import co.nxtgrid.token.domain.Pad;
import co.nxtgrid.token.domain.RandomNo;
import co.nxtgrid.token.domain.TokenIdentifier;
import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.tokenclass.class2.ClearCreditTokenClass;
import co.nxtgrid.token.domain.tokenclass.class2.ClearTamperConditionTokenClass;
import co.nxtgrid.token.domain.tokensubclass.class2.ClearCreditTokenSubClass;
import co.nxtgrid.token.domain.tokensubclass.class2.ClearTamperConditionTokenSubClass;
import co.nxtgrid.token.exceptions.BitConcatOverflowError;
import co.nxtgrid.token.exceptions.InvalidDateTimeBitsException;
import co.nxtgrid.token.exceptions.InvalidPadException;
import co.nxtgrid.token.exceptions.InvalidRangeException;
import co.nxtgrid.token.exceptions.decode.CRCError;

import java.util.HashMap;
import java.util.Optional;

public class ClearTamperConditionToken extends Class2Token {

    private Optional<RandomNo> randomNo;
    private TokenIdentifier tokenIdentifier;
    private Optional<Pad> pad;

    public ClearTamperConditionToken(String requestID)
            throws InvalidRangeException {
        super(requestID);
        setTokenClass(new ClearCreditTokenClass());
        setTokenSubClass(new ClearCreditTokenSubClass());
    }

    public ClearTamperConditionToken(String requestID,
                                     Optional<RandomNo> randomNo,
                                     TokenIdentifier tokenIdentifier,
                                     Optional<Pad> pad) throws InvalidRangeException {
        super(requestID);
        setTokenClass(new ClearTamperConditionTokenClass());
        setTokenSubClass(new ClearTamperConditionTokenSubClass());
        setRandomNo(randomNo);
        setTokenIdentifier(tokenIdentifier);
        setPad(pad);
    }

    @Override
    public String getType() {
        return "ClearTamperCondition_25";
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

    public Optional<Pad> getPad() {
        return pad;
    }

    public void setPad(Optional<Pad> pad) {
        this.pad = pad;
    }

    @Override
    public HashMap<String, Object> getParams() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("class", getTokenClass().getBitString().getValue());
        params.put("subclass", getTokenSubClass().getBitString().getValue());
        params.put("token_identifier", getTokenIdentifier().getTimeOfIssue().toString());
        params.put("type", getType());

        if (getRandomNo().isPresent())
            params.put("rnd", getRandomNo().get().getBitString().getValue());

        if (getPad().isPresent())
            params.put("register", getPad().get().getBitString().getValue());

        return params;
    }

    public void decode(BitString decryptedTokenBitString,
                       BitString encryptedTokenBitString) throws
            InvalidRangeException, CRCError,
            InvalidDateTimeBitsException, InvalidPadException,
            BitConcatOverflowError  {
        if (checkCrc(decryptedTokenBitString, getTokenClass())) {
            setTokenClass(new ClearTamperConditionTokenClass());
            setTokenSubClass(new ClearTamperConditionTokenSubClass());
            setRandomNo(extractRandomNo(decryptedTokenBitString));
            setTokenIdentifier(extractTokenIdentifier(decryptedTokenBitString));
            setPad(Optional.of(new Pad(decryptedTokenBitString.extractBits(16, 16))));
            setEncryptedTokenBitString(Long.toBinaryString(encryptedTokenBitString.getValue()));
            setDecryptedTokenBitString(Long.toBinaryString(decryptedTokenBitString.getValue()));
        }
    }
}
