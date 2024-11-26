package co.nxtgrid.token.generators.tokensgenerator.nativetoken.class2;

import co.nxtgrid.token.domain.Crc;
import co.nxtgrid.token.domain.Pad;
import co.nxtgrid.token.domain.RandomNo;
import co.nxtgrid.token.domain.TokenIdentifier;
import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.encryptionalgorithm.EncryptionAlgorithm;
import co.nxtgrid.token.domain.keys.decoder.DecoderKey;
import co.nxtgrid.token.domain.token.class2.ClearTamperConditionToken;
import co.nxtgrid.token.exceptions.BitConcatOverflowError;

import java.util.Optional;

public class ClearTamperConditionTokenGenerator extends Class2TokenGenerator<ClearTamperConditionToken> {

    private RandomNo randomNo;
    private TokenIdentifier tokenIdentifier;
    private Pad pad;
    private DecoderKey decoderKey;
    private EncryptionAlgorithm encryptionAlgorithm;

    public ClearTamperConditionTokenGenerator(String requestID,
                                              RandomNo randomNo,
                                              TokenIdentifier tokenIdentifier,
                                              Pad pad,
                                              DecoderKey decoderKey,
                                              EncryptionAlgorithm encryptionAlgorithm) {
        super(requestID);
        setRandomNo(randomNo);
        setTokenIdentifier(tokenIdentifier);
        setPad(pad);
        setDecoderKey(decoderKey);
        setEncryptionAlgorithm(encryptionAlgorithm);
    }

    public ClearTamperConditionToken generate() throws Exception {
        ClearTamperConditionToken token = new ClearTamperConditionToken(requestID, Optional.of(randomNo), tokenIdentifier, Optional.of(pad));
        BitString _64BitDataBlock = generate64BitDataBlock(token);
        BitString _64bitStringEncryptedBitString = encryptionAlgorithm.encrypt(getDecoderKey(), _64BitDataBlock);
        String _66bitStringEncryptedBlockAT = transpose66BitString(token.getTokenClass(), _64bitStringEncryptedBitString);
        token.setEncryptedTokenBitString(_66bitStringEncryptedBlockAT);
        return token;
    }

    public BitString generate64BitDataBlock(ClearTamperConditionToken token) throws BitConcatOverflowError {
        BitString tokenClass = token.getTokenClass().getBitString();
        BitString tokenSubClass = token.getTokenSubClass().getBitString();
        BitString random = token.getRandomNo().get().getBitString();
        BitString tid = token.getTokenIdentifier().getBitString();
        BitString pad = token.getPad().get().getBitString();
        Crc calcCRC = new Crc();
        BitString crc = calcCRC.generateCRC(pad.concat(tid, random, tokenSubClass, tokenClass));
        BitString _64BitDataBlock = crc.concat(pad, tid, random, tokenSubClass);
        return _64BitDataBlock;
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

    public Pad getPad() {
        return pad;
    }

    public void setPad(Pad pad) {
        this.pad = pad;
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
