package co.nxtgrid.token.domain.token;

import co.nxtgrid.token.domain.BaseDate;
import co.nxtgrid.token.domain.Crc;
import co.nxtgrid.token.domain.RandomNo;
import co.nxtgrid.token.domain.TokenIdentifier;
import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.tokenclass.TokenClass;
import co.nxtgrid.token.domain.tokensubclass.TokenSubClass;
import co.nxtgrid.token.exceptions.BitConcatOverflowError;
import co.nxtgrid.token.exceptions.InvalidDateTimeBitsException;
import co.nxtgrid.token.exceptions.InvalidRangeException;
import co.nxtgrid.token.generators.tokensdecoder.error.CRCError;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Optional;

public abstract class Token {

    protected String requestID = "";
    protected String encryptedTokenBitString;
    private String decryptedTokenBitString;
    private String tokenNo;
    private Optional<Crc> crc;
    private TokenClass tokenClass;
    private TokenSubClass tokenSubClass;

    public Token(String requestID) {
        setRequestID(requestID);
    }

    public abstract String getType();

    public abstract HashMap<String, Object> getParams();

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public String getTokenNo() {

        String generatedToken = String.format("%s", new BigInteger(encryptedTokenBitString, 2));
        return ("00000000000000000000".substring(generatedToken.length()) + generatedToken).replaceFirst("(\\d{4})(\\d{4})(\\d{4})(\\d{4})(\\d{4})", "$1$2$3$4$5");
    }

    public abstract void decode(BitString decryptedTokenBitString,
                                BitString encryptedTokenBitString) throws Exception ;

    public abstract String getBitString();

    public TokenClass getTokenClass() {
        return tokenClass;
    }

    public void setTokenClass(TokenClass tokenClass) {
        this.tokenClass = tokenClass;
    }

    public TokenSubClass getTokenSubClass() {
        return tokenSubClass;
    }

    public void setTokenSubClass(TokenSubClass tokenSubClass) {
        this.tokenSubClass = tokenSubClass;
    }

    public void setEncryptedTokenBitString(String encryptedTokenBitString) {
        this.encryptedTokenBitString = encryptedTokenBitString;
    }

    public void setDecryptedTokenBitString(String decryptedTokenBitString) {
        this.decryptedTokenBitString = decryptedTokenBitString;
    }

    public void setTokenNo(String tokenNo) {
        this.tokenNo = tokenNo;
    }

    public Optional<Crc> getCrc() {
        return crc ;
    }

    public void setCrc(Optional<Crc> crc) {
        this.crc = crc ;
    }

    protected boolean checkCrc(BitString _64bitStringDecryptedDataBlock, TokenClass tokenClass)
            throws CRCError, InvalidRangeException, BitConcatOverflowError {
        BitString calculatedCRC = calculateCrc(_64bitStringDecryptedDataBlock, tokenClass);
        BitString extractedCrcSection = _64bitStringDecryptedDataBlock.extractBits(0, 16);
        if (calculatedCRC.compareTo(extractedCrcSection) != BitString.SAME)
            throw new CRCError();
        return true;
    }

    protected BitString calculateCrc(BitString _64bitStringDecryptedDataBlock, TokenClass tokenClass)
            throws InvalidRangeException, BitConcatOverflowError {
        BitString APDUWithoutCRCBlock = _64bitStringDecryptedDataBlock.extractBits(16, 48);
        BitString crcBitString = new Crc().generateCRC(APDUWithoutCRCBlock.concat(tokenClass.getBitString()));
        return crcBitString;
    }

    protected Crc extractCrc(BitString dataBlock)
            throws InvalidRangeException {
        return new Crc(dataBlock.extractBits(0, 16));
    }

    protected TokenIdentifier extractTokenIdentifier(BitString _66BitStringAPDU)
            throws InvalidDateTimeBitsException, InvalidRangeException {
        TokenIdentifier tid = new TokenIdentifier(BaseDate._1993);
        BitString dateTimeOfIssueBitString = _66BitStringAPDU.extractBits(32, 24);
        tid.setTimeOfIssue(tid.getDateTimeOfIssue(dateTimeOfIssueBitString));
        return tid;
    }

    protected Optional<RandomNo> extractRandomNo(BitString _66BitStringAPDU)
            throws InvalidRangeException {
        return Optional.of(new RandomNo(_66BitStringAPDU.extractBits(56, 4)));
    }

    @Override
    public String toString() {
        return String.format("RequestID: %s, Token: %s", getRequestID(), getTokenNo());
    }
}