package co.nxtgrid.token.generators.tokensgenerator.nativetoken;

import co.nxtgrid.token.domain.base.Bit;
import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.encryptionalgorithm.EncryptionAlgorithm;
import co.nxtgrid.token.domain.keys.decoder.DecoderKey;
import co.nxtgrid.token.domain.token.Token;
import co.nxtgrid.token.domain.tokenclass.TokenClass;
import co.nxtgrid.tokens.service.impl.generate.DecoderKeyGeneratorManager;

import java.util.Map;

public abstract class TokenGenerator <T extends Token>{

    protected String requestID;
    protected abstract T generate() throws Exception ;
    protected abstract BitString generate64BitDataBlock(T token) throws Exception;
    protected DecoderKey decoderKey;
    protected EncryptionAlgorithm encryptionAlgorithm = null;

    public TokenGenerator(String requestID) {
        setRequestID(requestID);
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    protected String transpose66BitString(TokenClass tokenClass, BitString _64BitTokenDataBeforeTransposition) {
        Bit bit28 = _64BitTokenDataBeforeTransposition.getBit(28);
        Bit bit27 = _64BitTokenDataBeforeTransposition.getBit(27);
        _64BitTokenDataBeforeTransposition.setBit(28, tokenClass.getBitString().getBit(1).getValue());
        _64BitTokenDataBeforeTransposition.setBit(27, tokenClass.getBitString().getBit(0).getValue());
        String tokenDataBitString = Long.toBinaryString(_64BitTokenDataBeforeTransposition.getValue());
        String paddedTokenDataBeforeTransposition = "0000000000000000000000000000000000000000000000000000000000000000".substring(tokenDataBitString.length()) + tokenDataBitString;
        return Character.toString(bit28.getValue())
                + bit27.getValue()
                + paddedTokenDataBeforeTransposition;
    }

    protected String encrypt(TokenClass tokenClass, BitString _64BitDataBlock,
                             EncryptionAlgorithm encryptionAlgorithm) throws Exception {
        BitString _64bitStringEncryptedBitString = encryptionAlgorithm.encrypt(decoderKey, _64BitDataBlock);
        return transpose66BitString(tokenClass, _64bitStringEncryptedBitString);
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
}
