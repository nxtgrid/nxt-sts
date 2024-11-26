package co.nxtgrid.token.domain.encryptionalgorithm;

import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.keys.decoder.DecoderKey;

public abstract class EncryptionAlgorithm {

    public enum Code {
        STA("07"),
        DEA("09"),
        MISTY1("11");

        String name;

        Code(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private final String NAME = "Encryption Algorithm";
    private Code encryptionAlgorithmCode ;

    public EncryptionAlgorithm(Code encryptionAlgorithmCode) {
        this.encryptionAlgorithmCode = encryptionAlgorithmCode;
    }

    public Code getCode() {
        return encryptionAlgorithmCode;
    }

    public void setCode(Code encryptionAlgorithmCode) {
        this.encryptionAlgorithmCode = encryptionAlgorithmCode;
    }

    public String getName() {
        return NAME;
    }

    public abstract BitString encrypt(DecoderKey decoderKey, BitString dataBlock) throws Exception;

    public abstract BitString decrypt(DecoderKey decoderKey, BitString dataBlock) throws Exception;
}
