package co.nxtgrid.ca.keys.symmetric;

import co.nxtgrid.ca.Metadata;
import co.nxtgrid.ca.Provider;
import co.nxtgrid.ca.keys.Encode;
import co.nxtgrid.ca.keys.GeneralCipher;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;
import java.util.HashMap;

public abstract class SymmetricCipher extends GeneralCipher implements Encode, Metadata {

    protected KeyType keyType ;
    protected boolean useRandomIv = false ;

    protected SecretKeySpec secretKeySpec ;
    protected byte[] keyBytes ;
    protected OperatingMode operatingMode = OperatingMode.ECB ;
    protected PaddingType paddingType = PaddingType.NoPadding;
    protected byte[] iv ;
    protected Cipher cipher ;

    public enum KeyType {
        DES, AES, DESede
    }

    public enum OperatingMode {
        ECB, CBC, CTR, GCM,NONE
    }

    public enum PaddingType {
        NoPadding, PKCS5Padding, PKCS7Padding, OAEPWithSHA1AndMGF1Padding
    }

    public SymmetricCipher() {
        super(new Provider()) ;
    }

    public byte[] getKeyBytes() {
        return keyBytes ;
    }

    public void setKeyBytes(byte[] keyBytes) {
        this.keyBytes = keyBytes ;
    }

    public OperatingMode getOperatingMode() {
        return operatingMode ;
    }

    public void setOperatingMode(OperatingMode operatingMode) {
        this.operatingMode = operatingMode ;
    }

    public PaddingType getPaddingType() {
        return paddingType ;
    }

    public void setPaddingType(PaddingType paddingType) {
        this.paddingType = paddingType ;
    }

    public byte[] getIv() {
        return iv ;
    }

    public void setIv(byte[] iv) {
        this.iv = iv ;
    }

    public void useRandomIv() {
        useRandomIv = true ;
        byte[] randomIV = new byte[8];
        new SecureRandom().nextBytes(randomIV);
        setIv(randomIV);
    }

    public KeyType getKeyType() {
        return keyType;
    }

    public void setKeyType(KeyType keyType) {
        this.keyType = keyType ;
    }

    protected Key generate(byte[] keyBytes) {
        secretKeySpec = new SecretKeySpec(keyBytes, keyType.name()) ;
        return secretKeySpec;
    }

    public Key generate(int noOfBytes) {
        SecureRandom random = new SecureRandom() ;
        byte[] keyBytes = new byte[noOfBytes] ;
        random.nextBytes(keyBytes);
        secretKeySpec = new SecretKeySpec(keyBytes, keyType.name()) ;
        return secretKeySpec;
    }

    protected Cipher initializeCipher()
            throws  NoSuchAlgorithmException, NoSuchProviderException,
            NoSuchPaddingException {
        if (null == cipher) {
            cipher = Cipher.getInstance(keyType.name() + "/" +
                    operatingMode.name() + "/" +
                    paddingType.name(), getProviderAbbr());
        }
        return cipher;
    }

    @Override
    public byte[] getEncoded() {
        return secretKeySpec.getEncoded() ;
    }

    @Override
    public byte[] encrypt(byte[] plainText)
            throws InvalidKeyException, InvalidAlgorithmParameterException,
            ShortBufferException, IllegalBlockSizeException,
            BadPaddingException, NoSuchAlgorithmException,
            NoSuchProviderException, NoSuchPaddingException,
            InvalidParameterSpecException {

        cipher = initializeCipher() ;
        if (null == iv)
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        else {
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivspec);
        }

        byte[] cipherText = new byte[cipher.getOutputSize(plainText.length)];
        int ctLength = cipher.update(plainText, 0, plainText.length, cipherText, 0);
        cipher.doFinal(cipherText, ctLength);
        return cipherText ;
    }

    @Override
    public byte[] decrypt(byte[] cipherText)
            throws  Exception {
        cipher = initializeCipher() ;
        if (null != iv)
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));
        else
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

        byte[] convertedText = new byte[cipher.getOutputSize(cipherText.length)];
        int ctLength = cipher.update(cipherText, 0, cipherText.length, convertedText, 0);
        ctLength += cipher.doFinal(convertedText, ctLength);

        byte[] decryptedText = new byte[ctLength] ;
        for (int i = 0 ; i < decryptedText.length; i++)
            decryptedText[i] = convertedText[i] ;

        return decryptedText ;
    }

    public HashMap<String, Object> getMetadata() {
        return getKeyProperties() ;
    }

    public Object getProperty(String property) {
        return getKeyProperties().get(property) ;
    }

    private HashMap<String, Object> getKeyProperties() {
        return new HashMap() {{
            put("keyType", keyType.name()) ;
            put("operatingMode", operatingMode.name()) ;
            put("paddingType", paddingType.name()) ;
            put("iv", new String(Hex.encode(iv))) ;
            put("key", new String(Hex.encode(keyBytes))) ;
        }} ;
    }
}