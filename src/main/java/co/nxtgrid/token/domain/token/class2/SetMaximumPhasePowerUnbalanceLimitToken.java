package co.nxtgrid.token.domain.token.class2;

import co.nxtgrid.token.domain.MaximumPhasePowerUnbalanceLimit;
import co.nxtgrid.token.domain.RandomNo;
import co.nxtgrid.token.domain.TokenIdentifier;
import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.tokenclass.class2.SetMaximumPhasePowerUnbalanceLimitTokenClass;
import co.nxtgrid.token.domain.tokensubclass.class2.SetMaximumPhasePowerUnbalanceLimitTokenSubClass;
import co.nxtgrid.token.exceptions.BitConcatOverflowError;
import co.nxtgrid.token.exceptions.InvalidDateTimeBitsException;
import co.nxtgrid.token.exceptions.InvalidMppulException;
import co.nxtgrid.token.exceptions.InvalidRangeException;
import co.nxtgrid.token.generators.tokensdecoder.error.CRCError;

import java.util.HashMap;
import java.util.Optional;

public class SetMaximumPhasePowerUnbalanceLimitToken extends Class2Token {

    private Optional<RandomNo> randomNo;
    private TokenIdentifier tokenIdentifier;
    private Optional<MaximumPhasePowerUnbalanceLimit> maximumPhasePowerUnbalanceLimit;

    public SetMaximumPhasePowerUnbalanceLimitToken(String requestID)
            throws InvalidRangeException {
        super(requestID);
        setTokenClass(new SetMaximumPhasePowerUnbalanceLimitTokenClass());
        setTokenSubClass(new SetMaximumPhasePowerUnbalanceLimitTokenSubClass());
    }

    public SetMaximumPhasePowerUnbalanceLimitToken(String requestID,
                                                   Optional<RandomNo> randomNo,
                                                   TokenIdentifier tokenIdentifier,
                                                   Optional<MaximumPhasePowerUnbalanceLimit> maximumPhasePowerUnbalanceLimit)
            throws InvalidRangeException {
        super(requestID);
        setTokenClass(new SetMaximumPhasePowerUnbalanceLimitTokenClass());
        setTokenSubClass(new SetMaximumPhasePowerUnbalanceLimitTokenSubClass());
        setRandomNo(randomNo);
        setTokenIdentifier(tokenIdentifier);
        setMaximumPhasePowerUnbalanceLimit(maximumPhasePowerUnbalanceLimit);
    }

    @Override
    public String getType() {
        return "SetMaximumPhasePowerUnbalanceLimit_26";
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

    public Optional<MaximumPhasePowerUnbalanceLimit> getMaximumPhasePowerUnbalanceLimit() {
        return maximumPhasePowerUnbalanceLimit;
    }

    public void setMaximumPhasePowerUnbalanceLimit(Optional<MaximumPhasePowerUnbalanceLimit>
                                                           maximumPhasePowerUnbalanceLimit) {
        this.maximumPhasePowerUnbalanceLimit = maximumPhasePowerUnbalanceLimit;
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

        if (getMaximumPhasePowerUnbalanceLimit().isPresent())
            params.put("register", getMaximumPhasePowerUnbalanceLimit().get()
                    .getMppulBitString().getValue());

        return params;
    }

    public void decode(BitString decryptedTokenBitString,
                       BitString encryptedTokenBitString) throws
            InvalidRangeException,
            CRCError, InvalidDateTimeBitsException,
            InvalidMppulException, BitConcatOverflowError {
        if (checkCrc(decryptedTokenBitString, getTokenClass())) {
            setRandomNo(extractRandomNo(decryptedTokenBitString));
            setTokenIdentifier(extractTokenIdentifier(decryptedTokenBitString));
            setMaximumPhasePowerUnbalanceLimit(
                    Optional.of(new MaximumPhasePowerUnbalanceLimit(decryptedTokenBitString.extractBits(16, 16).getValue())));
            setEncryptedTokenBitString(Long.toBinaryString(encryptedTokenBitString.getValue()));
            setDecryptedTokenBitString(Long.toBinaryString(decryptedTokenBitString.getValue()));
        }
    }
}
