package co.nxtgrid.token.generators.decoderkeygenerator;

import co.nxtgrid.token.domain.BaseDate;
import co.nxtgrid.token.domain.KeyRevisionNumber;
import co.nxtgrid.token.domain.KeyType;
import co.nxtgrid.token.domain.TariffIndex;
import co.nxtgrid.token.domain.encryptionalgorithm.EncryptionAlgorithm;
import co.nxtgrid.token.domain.keys.decoder.DecoderKey;
import co.nxtgrid.token.domain.keys.vending.VendingKey;
import co.nxtgrid.token.domain.meterprimaryaccountnumber.MeterPrimaryAccountNumber;
import co.nxtgrid.token.domain.supplygroupcode.SupplyGroupCode;
import co.nxtgrid.token.exceptions.EncryptionAlgorithmVendingKeyLengthMismatchException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class DecoderKeyGeneratorAlgorithm04 extends DecoderKeyGeneratorAlgorithm {

    private VendingKey vendingKey;
    private BaseDate baseDate;
    private MeterPrimaryAccountNumber meterPAN;
    private int lengthOfDecoderKey = 0;

    public DecoderKeyGeneratorAlgorithm04(BaseDate baseDate, TariffIndex tariffIndex, SupplyGroupCode supplyGroupCode,
                                          KeyType keyType, KeyRevisionNumber keyRevisionNumber,
                                          EncryptionAlgorithm encryptionAlgorithm, MeterPrimaryAccountNumber meterPAN,
                                          VendingKey vendingKey) {
        setBaseDate(baseDate);
        setTariffIndex(tariffIndex);
        setSupplyGroupCode(supplyGroupCode);
        setKeyType(keyType);
        setKeyRevisionNumber(keyRevisionNumber);
        setEncryptionAlgorithm(encryptionAlgorithm);
        setMeterPAN(meterPAN);
        setVendingKey(vendingKey);
    }

    public String getName() {
        return "DKGA02";
    }

    public BaseDate getBaseDate() {
        return baseDate;
    }

    public void setBaseDate(BaseDate baseDate) {
        this.baseDate = baseDate;
    }

    public VendingKey getVendingKey() {
        return vendingKey;
    }

    public void setVendingKey(VendingKey vendingKey) {
        this.vendingKey = vendingKey;
    }

    public MeterPrimaryAccountNumber getMeterPAN() {
        return meterPAN;
    }

    public void setMeterPAN(MeterPrimaryAccountNumber meterPAN) {
        this.meterPAN = meterPAN;
    }

    @Override
    public DecoderKey generate()
            throws NoSuchAlgorithmException, InvalidKeyException,
            EncryptionAlgorithmVendingKeyLengthMismatchException {

        if (encryptionAlgorithm.getCode().toString().equals("STA") &&
                vendingKey.getKeyData().length == 20) { // 160 bit vending key
            lengthOfDecoderKey = 64;
        } else if (encryptionAlgorithm.getCode().toString().equals("MISTY1") &&
                vendingKey.getKeyData().length == 20) {
            lengthOfDecoderKey = 128;
        } else {
            throw new EncryptionAlgorithmVendingKeyLengthMismatchException(
                    String.format("Vending key mismatched with encryption algorithm %s. Key must be 160 bit.",
                                    vendingKey.getName(), encryptionAlgorithm.getCode()));
        }

        byte[] vendingkeybytes = vendingKey.getKeyData();
        byte[] databytes = charArrayToByteArray(generateDataBlock(baseDate, encryptionAlgorithm,
                                                                    tariffIndex, supplyGroupCode, keyType,
                                                                    keyRevisionNumber, meterPAN));

        Mac sha256HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(vendingkeybytes, "HmacSHA256");
        sha256HMAC.init(secretKey);
        byte[] res = sha256HMAC.doFinal(databytes);

        DecoderKey decoderKey = new DecoderKey();

        if (encryptionAlgorithm.getCode().toString().equals("MISTY1")) {
            byte[] misty1DecoderKey = { res[0], res[1], res[2], res[3],
                                        res[4], res[5], res[6], res[7],
                                        res[8], res[9], res[10], res[11],
                                        res[12], res[13], res[14], res[15] };
            decoderKey.setKeyData(misty1DecoderKey);
        } else if (encryptionAlgorithm.getCode().toString().equals("STA")) {
            // The reversed order is to conform with the earlier
            // DKGA02, EA02 implementation in IEC 62055-41:2014
            // as it was implemented in this application
            byte[] staDecoderKey = { res[7], res[6], res[5], res[4],
                                     res[3], res[2], res[1], res[0] };
            decoderKey.setKeyData(staDecoderKey);
        }
        return decoderKey;
    }

    private byte[] charArrayToByteArray(char[] c_array) {
        byte[] b_array = new byte[c_array.length];
        for (int i = 0; i < c_array.length; i++) {
            b_array[i] = (byte) (0xFF & (int) c_array[i]);
        }
        return b_array;
    }

    private char[] generateDataBlock(BaseDate baseDate, EncryptionAlgorithm encryptionAlgorithm,
                                     TariffIndex tariffIndex, SupplyGroupCode supplyGroupCode,
                                     KeyType keyType, KeyRevisionNumber keyRevisionNumber,
                                     MeterPrimaryAccountNumber meterPrimaryAccountNumber) {
        char[] sep1 = { 0x04, 0x02 };
        char[] sep2 = { 0x02 };
        char[] sep3 = { 0x02 };
        char[] sep4 = { 0x02 };
        char[] sep5 = { 0x00, 0x04, 0x06 };
        char[] sep6 = { 0x01 };
        char[] sep7 = { 0x01 };
        char[] sep8 = { 0x12 };

        char[] dkga = "04".toCharArray();
        char[] bd = baseDate.shortCode.toCharArray();
        char[] ea = encryptionAlgorithm.getCode().getName().toCharArray();
        char[] ti = tariffIndex.getValue().toCharArray();
        char[] sgc = supplyGroupCode.getValue().toCharArray();
        char[] kt = Integer.toHexString(keyType.getValue()).toCharArray();
        char[] krn = Integer.toHexString(keyRevisionNumber.getValue()).toCharArray();
        char[] meterpan = meterPrimaryAccountNumber.getMeterPanValue().toCharArray();
        char[] decoderLength = { 0x00, 0x00, 0x00, 0x00 };
        decoderLength[3] = (lengthOfDecoderKey == 64) ?  64 : (char) 128;

        return new StringBuilder().append(sep1).append(dkga).append(sep2).append(bd)
                                .append(sep3).append(ea).append(sep4).append(ti)
                                .append(sep5).append(sgc).append(sep6).append(kt)
                                .append(sep7).append(krn).append(sep8).append(meterpan)
                                .append(decoderLength).toString().toCharArray();
    }
}
