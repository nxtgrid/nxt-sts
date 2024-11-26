package co.nxtgrid.token.generators.tokensgenerator.nativetoken.class2;

import co.nxtgrid.token.domain.Crc;
import co.nxtgrid.token.domain.NewKeyMiddleOrder2;
import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.encryptionalgorithm.EncryptionAlgorithm;
import co.nxtgrid.token.domain.keys.decoder.DecoderKey;
import co.nxtgrid.token.domain.supplygroupcode.SupplyGroupCode;
import co.nxtgrid.token.domain.supplygroupcode.SupplyGroupCodeLowOrder;
import co.nxtgrid.token.domain.token.class2.Set3rdSectionDecoderKeyToken;
import co.nxtgrid.token.exceptions.BitConcatOverflowError;
import co.nxtgrid.token.exceptions.InvalidBitStringException;
import co.nxtgrid.token.exceptions.InvalidRangeException;
import co.nxtgrid.token.exceptions.InvalidSgcloException;

public class Set3rdSectionDecoderKeyTokenGenerator extends Class2TokenGenerator<Set3rdSectionDecoderKeyToken> {

    private Crc crc;
    private DecoderKey decoderKey;
    private NewKeyMiddleOrder2 newKeyMiddleOrder2;
    private DecoderKey newDecoderKey;
    private SupplyGroupCode supplyGroupCode;
    private SupplyGroupCodeLowOrder supplyGroupCodeLowOrder;

    public Set3rdSectionDecoderKeyTokenGenerator(String requestID,
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
        generateNewKeyMiddleOrder2();
        generateSupplyGroupCodeLowOrder();
    }

    public Set3rdSectionDecoderKeyToken generate() throws Exception {
        Set3rdSectionDecoderKeyToken token = new Set3rdSectionDecoderKeyToken(requestID, supplyGroupCodeLowOrder, newKeyMiddleOrder2);
        BitString _64BitDataBlock = generate64BitDataBlock(token);
        BitString _64bitStringEncryptedBitString = getEncryptionAlgorithm().encrypt(getDecoderKey(), _64BitDataBlock);
        String _66bitStringEncryptedBlockAT = transpose66BitString(token.getTokenClass(), _64bitStringEncryptedBitString);
        token.setEncryptedTokenBitString(_66bitStringEncryptedBlockAT);
        return token;
    }

    public BitString generate64BitDataBlock(Set3rdSectionDecoderKeyToken token)
            throws BitConcatOverflowError {
        BitString tokenClass = token.getTokenClass().getBitString();
        BitString tokenSubClass = token.getTokenSubClass().getBitString();
        BitString sgclo = token.getSupplyGroupCodeLowOrder().getSgcloBitString();
        BitString nkmo2 = token.getNewKeyMiddleOrder2().getBitString();
        BitString concatenated = nkmo2.concat(sgclo, tokenSubClass, tokenClass);
        BitString crc = new Crc().generateCRC(concatenated);
        BitString _64BitDataBlock = crc.concat(nkmo2, sgclo, tokenSubClass);
        return _64BitDataBlock;
    }

    public SupplyGroupCode getSupplyGroupCode() {
        return supplyGroupCode;
    }

    public void setSupplyGroupCode(SupplyGroupCode supplyGroupCode) {
        this.supplyGroupCode = supplyGroupCode;
    }

    public NewKeyMiddleOrder2 getNewKeyMiddleOrder2() {
        return newKeyMiddleOrder2;
    }

    public void generateNewKeyMiddleOrder2() throws Exception {
        BitString newKeyMiddleOrder2BitString = null;
        if (encryptionAlgorithm.getCode().toString().equals("STA"))
            throw new NotSupportedException("Set3rdSection KCT not supported for EA07 (STA)");
        else if (encryptionAlgorithm.getCode().toString().equals("MISTY1")) {
            newKeyMiddleOrder2BitString = new BitString(newDecoderKey.bitsToStringReversed().substring(32, 64));
            newKeyMiddleOrder2BitString.setLength(32);
        }
        this.newKeyMiddleOrder2 =  new NewKeyMiddleOrder2(newKeyMiddleOrder2BitString);
    }

    private void generateSupplyGroupCodeLowOrder() throws InvalidSgcloException,
            InvalidBitStringException, InvalidRangeException  {
        supplyGroupCodeLowOrder = new SupplyGroupCodeLowOrder(supplyGroupCode);
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

    public void setNewDecoderKey(DecoderKey decoderKey) {
        this.newDecoderKey = decoderKey;
    }
}

class NotSupportedException extends Exception {
    public NotSupportedException(String message) {
        super(message);
    }
}
