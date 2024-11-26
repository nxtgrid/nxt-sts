package co.nxtgrid.token.generators.tokensgenerator.nativetoken.class2;

import co.nxtgrid.token.domain.*;
import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.encryptionalgorithm.EncryptionAlgorithm;
import co.nxtgrid.token.domain.keys.decoder.DecoderKey;
import co.nxtgrid.token.domain.token.class2.Set1stSectionDecoderKeyToken;
import co.nxtgrid.token.exceptions.BitConcatOverflowError;

public class Set1stSectionDecoderKeyTokenGenerator extends Class2TokenGenerator<Set1stSectionDecoderKeyToken> {

    private KeyExpiryNumberHighOrder keyExpiryNumberHighOrder;
    private KeyRevisionNumber keyRevisionNumber;
    private RolloverKeyChange rolloverKeyChange;
    private KeyType keyType;
    private NewKeyHighOrder newKeyHighOrder;
    private DecoderKey decoderKey;
    private DecoderKey newDecoderKey;
    private EncryptionAlgorithm encryptionAlgorithm;

    public Set1stSectionDecoderKeyTokenGenerator(String requestID,
                                                 KeyExpiryNumberHighOrder keyExpiryNumberHighOrder,
                                                 KeyRevisionNumber keyRevisionNumber,
                                                 RolloverKeyChange rolloverKeyChange,
                                                 KeyType keyType,
                                                 DecoderKey decoderKey,
                                                 DecoderKey newDecoderKey,
                                                 EncryptionAlgorithm encryptionAlgorithm)
            throws Exception {
        super(requestID);
        setKeyExpiryNumberHighOrder(keyExpiryNumberHighOrder);
        setKeyRevisionNumber(keyRevisionNumber);
        setRolloverKeyChange(rolloverKeyChange);
        setKeyType(keyType);
        setDecoderKey(decoderKey);
        setNewDecoderKey(newDecoderKey);
        setEncryptionAlgorithm(encryptionAlgorithm);
        generateNewKeyHighOrder();
    }

    public Set1stSectionDecoderKeyToken generate() throws Exception {
        Set1stSectionDecoderKeyToken token = new Set1stSectionDecoderKeyToken(requestID,
                                                                                keyExpiryNumberHighOrder,
                                                                                keyRevisionNumber,
                                                                                rolloverKeyChange,
                                                                                keyType,
                                                                                newKeyHighOrder);
        BitString _64BitDataBlock = generate64BitDataBlock(token);
        BitString _64bitStringEncryptedBitString = getEncryptionAlgorithm().encrypt(getDecoderKey(), _64BitDataBlock);
        String _66bitStringEncryptedBlockAT = transpose66BitString(token.getTokenClass(), _64bitStringEncryptedBitString);
        token.setEncryptedTokenBitString(_66bitStringEncryptedBlockAT);
        return token;
    }

    public BitString generate64BitDataBlock(Set1stSectionDecoderKeyToken token)
            throws BitConcatOverflowError {
        BitString tokenClass = token.getTokenClass().getBitString();
        BitString tokenSubClass = token.getTokenSubClass().getBitString();
        BitString kenho = token.getKeyExpiryNumberHighOrder().getBitString();
        BitString krn = new BitString((long) token.getKeyRevisionNumber().getValue());
        krn.setLength(4);
        BitString ro = token.getRolloverKeyChange().getBitString();
        BitString res = token.get3KCT().getBitString();
        BitString keyType = new BitString((long) token.getKeyType().getValue());
        keyType.setLength(2);
        BitString nkho = token.getNewKeyHighOrder().getBitString();
        Crc calcCRC = new Crc();
        BitString crc = calcCRC.generateCRC(nkho.concat(keyType, res, ro, krn, kenho, tokenSubClass, tokenClass));
        BitString _64BitDataBlock = crc.concat(nkho, keyType, res, ro, krn, kenho, tokenSubClass);
        return _64BitDataBlock;
    }

    public KeyExpiryNumberHighOrder getKeyExpiryNumberHighOrder() {
        return keyExpiryNumberHighOrder;
    }

    public void setKeyExpiryNumberHighOrder(KeyExpiryNumberHighOrder keyExpiryNumberHighOrder) {
        this.keyExpiryNumberHighOrder = keyExpiryNumberHighOrder;
    }

    public KeyRevisionNumber getKeyRevisionNumber() {
        return keyRevisionNumber;
    }

    public void setKeyRevisionNumber(KeyRevisionNumber keyRevisionNumber) {
        this.keyRevisionNumber = keyRevisionNumber;
    }

    public RolloverKeyChange getRolloverKeyChange() {
        return rolloverKeyChange;
    }

    public void setRolloverKeyChange(RolloverKeyChange rolloverKeyChange) {
        this.rolloverKeyChange = rolloverKeyChange;
    }

    public KeyType getKeyType() {
        return keyType;
    }

    public void setKeyType(KeyType keyType) {
        this.keyType = keyType;
    }

    public NewKeyHighOrder getNewKeyHighOrder() {
        return newKeyHighOrder;
    }

    public void generateNewKeyHighOrder() throws Exception {
        BitString newKeyHighOrderBitString = null;
        if (encryptionAlgorithm.getCode().toString().equals("STA"))
            newKeyHighOrderBitString = new BitString(newDecoderKey.bitsToString().substring(0, 32));
        else if (encryptionAlgorithm.getCode().toString().equals("MISTY1"))
            newKeyHighOrderBitString = new BitString(newDecoderKey.bitsToStringReversed().substring(0,32));
        newKeyHighOrderBitString.setLength(32);
        this.newKeyHighOrder = new NewKeyHighOrder(newKeyHighOrderBitString);
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

    public EncryptionAlgorithm getEncryptionAlgorithm() {
        return encryptionAlgorithm;
    }

    public void setEncryptionAlgorithm(EncryptionAlgorithm encryptionAlgorithm) {
        this.encryptionAlgorithm = encryptionAlgorithm;
    }
}
