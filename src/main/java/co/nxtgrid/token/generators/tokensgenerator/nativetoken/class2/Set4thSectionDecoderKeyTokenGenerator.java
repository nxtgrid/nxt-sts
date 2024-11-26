package co.nxtgrid.token.generators.tokensgenerator.nativetoken.class2;

import co.nxtgrid.token.domain.Crc;
import co.nxtgrid.token.domain.NewKeyMiddleOrder1;
import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.encryptionalgorithm.EncryptionAlgorithm;
import co.nxtgrid.token.domain.keys.decoder.DecoderKey;
import co.nxtgrid.token.domain.supplygroupcode.SupplyGroupCode;
import co.nxtgrid.token.domain.supplygroupcode.SupplyGroupCodeHighOrder;
import co.nxtgrid.token.domain.token.class2.Set4thSectionDecoderKeyToken;
import co.nxtgrid.token.exceptions.BitConcatOverflowError;
import co.nxtgrid.token.exceptions.InvalidBitStringException;
import co.nxtgrid.token.exceptions.InvalidRangeException;
import co.nxtgrid.token.exceptions.InvalidSgchoException;

public class Set4thSectionDecoderKeyTokenGenerator extends Class2TokenGenerator<Set4thSectionDecoderKeyToken> {

    private Crc crc;
    private SupplyGroupCodeHighOrder supplyGroupCodeHighOrder;
    private SupplyGroupCode supplyGroupCode;
    private NewKeyMiddleOrder1 newKeyMiddleOrder1;
    private DecoderKey decoderKey;
    private DecoderKey newDecoderKey;

    public Set4thSectionDecoderKeyTokenGenerator(String requestID,
                                                 SupplyGroupCode supplyGroupCode,
                                                 DecoderKey decoderKey,
                                                 DecoderKey newDecoderKey,
                                                 EncryptionAlgorithm encryptionAlgorithm)
        throws Exception {
        super(requestID);
        setSupplyGroupCode(supplyGroupCode);
        setDecoderKey(decoderKey);
        setNewDecoderKey(newDecoderKey);
        setEncryptionAlgorithm(encryptionAlgorithm);
        generateNewKeyMiddleOrder1();
        generateSupplyGroupCodeHighOrder();
    }

    public Set4thSectionDecoderKeyToken generate() throws Exception {
        Set4thSectionDecoderKeyToken token = new Set4thSectionDecoderKeyToken(requestID, supplyGroupCodeHighOrder, newKeyMiddleOrder1);
        BitString _64BitDataBlock = generate64BitDataBlock(token);
        BitString _64bitStringEncryptedBitString = getEncryptionAlgorithm().encrypt(getDecoderKey(), _64BitDataBlock);
        String _66bitStringEncryptedBlockAT = transpose66BitString(token.getTokenClass(), _64bitStringEncryptedBitString);
        token.setEncryptedTokenBitString(_66bitStringEncryptedBlockAT);
        return token;
    }

    public BitString generate64BitDataBlock(Set4thSectionDecoderKeyToken token)
            throws BitConcatOverflowError {
        BitString tokenClass = token.getTokenClass().getBitString();
        BitString tokenSubClass = token.getTokenSubClass().getBitString();
        BitString sgclo = token.getSupplyGroupCodeHighOrder().getBitString();
        BitString nkmo1 = token.getNewKeyMiddleOrder1().getBitString();
        BitString concatenated = nkmo1.concat(sgclo, tokenSubClass, tokenClass);
        BitString crc = new Crc().generateCRC(concatenated);
        BitString _64BitDataBlock = crc.concat(nkmo1, sgclo, tokenSubClass);
        return _64BitDataBlock;
    }

    public SupplyGroupCode getSupplyGroupCode() {
        return supplyGroupCode;
    }

    public void setSupplyGroupCode(SupplyGroupCode supplyGroupCode) {
        this.supplyGroupCode = supplyGroupCode;
    }

    private void generateSupplyGroupCodeHighOrder() throws InvalidSgchoException,
                InvalidBitStringException, InvalidRangeException {
        supplyGroupCodeHighOrder = new SupplyGroupCodeHighOrder(supplyGroupCode);
    }

    public NewKeyMiddleOrder1 getNewKeyMiddleOrder1() {
        return newKeyMiddleOrder1;
    }

    public void generateNewKeyMiddleOrder1() throws Exception {
        BitString newKeyMiddleOrder1BitString = null;
        if (encryptionAlgorithm.getCode().toString().equals("STA"))
            throw new NotSupportedException("Set4thSection KCT not supported for EA07 (STA)");
        else if (encryptionAlgorithm.getCode().toString().equals("MISTY1")) {
            newKeyMiddleOrder1BitString = new BitString(newDecoderKey.bitsToStringReversed().substring(64, 96));
            newKeyMiddleOrder1BitString.setLength(32);
        }
        this.newKeyMiddleOrder1 = new NewKeyMiddleOrder1(newKeyMiddleOrder1BitString);
    }

    public Crc getCrc() {
        return crc;
    }

    public void setCrc(Crc crc) {
        this.crc = crc;
    }

    public DecoderKey getDecoderKey() {
        return decoderKey;
    }

    public void setDecoderKey(DecoderKey decoderKey) {
        this.decoderKey = decoderKey;
    }

    public DecoderKey getNewDecoderKey() {
        return newDecoderKey;
    }

    public void setNewDecoderKey(DecoderKey newDecoderKey) {
        this.newDecoderKey = newDecoderKey;
    }
}
