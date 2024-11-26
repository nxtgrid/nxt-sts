package co.nxtgrid.token.generators.tokensgenerator.nativetoken.class2;

import co.nxtgrid.token.domain.Crc;
import co.nxtgrid.token.domain.KeyExpiryNumberLowOrder;
import co.nxtgrid.token.domain.NewKeyLowOrder;
import co.nxtgrid.token.domain.TariffIndex;
import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.encryptionalgorithm.EncryptionAlgorithm;
import co.nxtgrid.token.domain.keys.decoder.DecoderKey;
import co.nxtgrid.token.domain.token.class2.Set2ndSectionDecoderKeyToken;
import co.nxtgrid.token.exceptions.BitConcatOverflowError;

public class Set2ndSectionDecoderKeyTokenGenerator extends Class2TokenGenerator<Set2ndSectionDecoderKeyToken> {

    private KeyExpiryNumberLowOrder keyExpiryNumberLowOrder;
    private TariffIndex tariffIndex;
    private NewKeyLowOrder newKeyLowOrder;
    private Crc crc;
    private DecoderKey decoderKey;
    private DecoderKey newDecoderKey;
    private EncryptionAlgorithm encryptionAlgorithm;

    public Set2ndSectionDecoderKeyTokenGenerator(String requestID,
                                                 KeyExpiryNumberLowOrder keyExpiryNumberLowOrder,
                                                 TariffIndex tariffIndex,
                                                 DecoderKey decoderKey,
                                                 DecoderKey newDecoderKey,
                                                 EncryptionAlgorithm encryptionAlgorithm)
            throws Exception {
        super(requestID);
        setKeyExpiryNumberLowOrder(keyExpiryNumberLowOrder);
        setTariffIndex(tariffIndex);
        setDecoderKey(decoderKey);
        setNewDecoderKey(newDecoderKey);
        setEncryptionAlgorithm(encryptionAlgorithm);
        generateNewKeyLowOrder();
    }

    public Set2ndSectionDecoderKeyToken generate() throws Exception {
        Set2ndSectionDecoderKeyToken token = new Set2ndSectionDecoderKeyToken(requestID, keyExpiryNumberLowOrder, tariffIndex, newKeyLowOrder);
        BitString _64BitDataBlock = generate64BitDataBlock(token);
        BitString _64bitStringEncryptedBitString = getEncryptionAlgorithm().encrypt(getDecoderKey(), _64BitDataBlock);
        String _66bitStringEncryptedBlockAT = transpose66BitString(token.getTokenClass(), _64bitStringEncryptedBitString);
        token.setEncryptedTokenBitString(_66bitStringEncryptedBlockAT);
        return token;
    }

    public BitString generate64BitDataBlock(Set2ndSectionDecoderKeyToken token)
            throws BitConcatOverflowError {
        BitString tokenClass = token.getTokenClass().getBitString();
        BitString tokenSubClass = token.getTokenSubClass().getBitString();
        BitString kenlo = token.getKeyExpiryNumberLowOrder().getKenloBitString();
        BitString ti = new BitString(Long.parseLong(token.getTariffIndex().getValue()));
        ti.setLength(8);
        BitString nklo = token.getNewKeyLowOrder().getNkloBitString();
        BitString concatenated = nklo.concat(ti, kenlo, tokenSubClass, tokenClass);
        BitString crc = new Crc().generateCRC(concatenated);
        BitString _64BitDataBlock = crc.concat(nklo, ti, kenlo, tokenSubClass);
        return _64BitDataBlock;
    }

    public KeyExpiryNumberLowOrder getKeyExpiryNumberLowOrder() {
        return keyExpiryNumberLowOrder;
    }

    public void setKeyExpiryNumberLowOrder(KeyExpiryNumberLowOrder keyExpiryNumberLowOrder) {
        this.keyExpiryNumberLowOrder = keyExpiryNumberLowOrder;
    }

    public TariffIndex getTariffIndex() {
        return tariffIndex;
    }

    public void setTariffIndex(TariffIndex tariffIndex) {
        this.tariffIndex = tariffIndex;
    }

    public NewKeyLowOrder getNewKeyLowOrder() {
        return newKeyLowOrder;
    }

    public void generateNewKeyLowOrder() throws Exception {
        this.newKeyLowOrder = generateNKLO();
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

    public EncryptionAlgorithm getEncryptionAlgorithm() {
        return encryptionAlgorithm;
    }

    public void setEncryptionAlgorithm(EncryptionAlgorithm encryptionAlgorithm) {
        this.encryptionAlgorithm = encryptionAlgorithm;
    }

    private NewKeyLowOrder generateNKLO() throws Exception {
        BitString newkeyLowOrderBitString = null;
        if (encryptionAlgorithm.getCode().toString().equals("STA"))
            newkeyLowOrderBitString = new BitString(newDecoderKey.bitsToString().substring(32, 64));
        else if (encryptionAlgorithm.getCode().toString().equals("MISTY1"))
            newkeyLowOrderBitString = new BitString(newDecoderKey.bitsToStringReversed().substring(96, 128));
        newkeyLowOrderBitString.setLength(32);
        return new NewKeyLowOrder(newkeyLowOrderBitString);
    }
}
