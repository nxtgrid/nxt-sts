package co.nxtgrid.token.domain.encryptionalgorithm;

import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.keys.Key;
import co.nxtgrid.token.domain.keys.decoder.DecoderKey;
import co.nxtgrid.token.exceptions.NotImplementedException;
import co.nxtgrid.token.generators.utils.Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class DataEncryptionAlgorithm extends EncryptionAlgorithm {

    public DataEncryptionAlgorithm(){
        super(Code.DEA);
    }

    public BitString encrypt(DecoderKey decoderKey, BitString dataBlock) throws Exception {
        Key desKeyWithOddParity = convertToDESKeyWithOddParity(decoderKey);
        byte[] encryptedData = encrypt(desKeyWithOddParity, Utils.longToBytes(dataBlock.getValue()));
        return new BitString(Utils.bytesToLong(encryptedData));
    }

    private Key convertToDESKeyWithOddParity(Key decoderKey) {
        /**
         * Convert 56 bit key into binary form
         * Separate bits into groups of 7
         * If there are odd number of 1s in each group, add a 0 bit at the end, else add a 1
         * Each group should now have 8 bits, convert to hex to get 64 bit key
         */
        byte[] desKeyWithOddParity = new byte[64];
        // byte[] masks = { -128, 64, 32, 16, 8, 4, 2, 1 };
        byte[] masks = { -128, 64, 32, 16, 8, 4, 2 };
        byte[] decoderByteData = decoderKey.getKeyData();
        for(int decoderKeyByteCounter = 0; decoderKeyByteCounter < decoderByteData.length; decoderKeyByteCounter++) {
            byte currDecoderKeyByte = decoderByteData[decoderKeyByteCounter];
            int noOf1s = 0;
            for (byte maskByte : masks) {
                if ((currDecoderKeyByte & maskByte) == maskByte) {
                    noOf1s++;
                }
            }
            if (noOf1s % 2 == 0) desKeyWithOddParity[decoderKeyByteCounter] = (byte) (currDecoderKeyByte | (1 << 8));
            else desKeyWithOddParity[decoderKeyByteCounter] = (byte) (currDecoderKeyByte & ~(1 << 8)) ;
        }
        return new DecoderKey(desKeyWithOddParity);
    }

    public byte[] encrypt (Key decoderKey, byte[] dataToEncrypt) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(decoderKey.getKeyData(), "DES");
        Cipher ecipher = Cipher.getInstance("DES/ECB/NoPadding");
        ecipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return ecipher.doFinal(dataToEncrypt);
    }

    public BitString decrypt(DecoderKey decoderKey, BitString dataBlock) throws Exception {
        throw new NotImplementedException();
    }
}
