package co.nxtgrid.ca.keys.symmetric;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidParameterSpecException;

public class AES128Cipher extends SymmetricCipher {

    public AES128Cipher() {
        final int NO_OF_BYTES = 16 ;
        setKeyType(KeyType.AES);
        generate(NO_OF_BYTES) ;
    }

    public AES128Cipher(byte[] keyBytes) {
        setKeyBytes(keyBytes);
        setKeyType(KeyType.AES);
        generate(keyBytes) ;
    }

    public AES128Cipher(byte[] keyBytes, OperatingMode operatingMode) {
        setKeyBytes(keyBytes);
        setOperatingMode(operatingMode);
        setKeyType(KeyType.AES);
        generate(keyBytes) ;
    }

    public AES128Cipher(byte[] keyBytes, OperatingMode operatingMode, PaddingType paddingType) {
        setKeyBytes(keyBytes);
        setOperatingMode(operatingMode);
        setPaddingType(paddingType);
        setKeyType(KeyType.AES);
        generate(keyBytes) ;
    }

    @Override
    public byte[] encrypt(byte[] plainText)
            throws InvalidKeyException, InvalidAlgorithmParameterException,
            ShortBufferException, IllegalBlockSizeException,
            BadPaddingException, NoSuchAlgorithmException,
            NoSuchProviderException, NoSuchPaddingException,
            InvalidParameterSpecException {
        cipher = initializeCipher();
        byte[] cipherText ;

        switch (getOperatingMode()) {
            case CBC:
            case CTR:
                /* CBC mode requires an IV of the same size as the block size */
                byte[] ivBytes ;
                if (null == iv || useRandomIv)
                    ivBytes = new byte[cipher.getBlockSize()];
                else
                    ivBytes = getIv() ;
                IvParameterSpec ivspec = new IvParameterSpec(ivBytes);
                setIv(ivspec.getIV());
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivspec);
                break;

            case GCM:
                if (null == iv || useRandomIv)
                    ivBytes = new byte[cipher.getBlockSize()];
                else
                    ivBytes = new byte[256 / Byte.SIZE];
                GCMParameterSpec gcmSpecWithIV = new GCMParameterSpec(128, ivBytes);
                setIv(gcmSpecWithIV.getIV());
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, gcmSpecWithIV);
                break;

            case ECB:
                /* Does not use an IV */
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        }

        cipherText = new byte[cipher.getOutputSize(plainText.length)];
        int ctLength = cipher.update(plainText, 0, plainText.length, cipherText, 0);
        cipher.doFinal(cipherText, ctLength);
        return cipherText ;
    }
}
