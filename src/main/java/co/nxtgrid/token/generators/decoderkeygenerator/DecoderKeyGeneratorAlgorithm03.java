package co.nxtgrid.token.generators.decoderkeygenerator;

import co.nxtgrid.token.domain.*;
import co.nxtgrid.token.domain.keys.decoder.DecoderKey;
import co.nxtgrid.token.domain.keys.vending.VendingKey;
import co.nxtgrid.token.domain.supplygroupcode.SupplyGroupCode;
import co.nxtgrid.token.exceptions.InvalidPrimaryAccountNumberBlockComponentsException;
import co.nxtgrid.token.exceptions.NotImplementedException;
import co.nxtgrid.token.generators.utils.Utils;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class DecoderKeyGeneratorAlgorithm03 extends DecoderKeyGeneratorAlgorithm {

    private VendingKey vendingKey1;
    private VendingKey vendingKey2;

    public DecoderKeyGeneratorAlgorithm03(KeyType keyType, SupplyGroupCode supplyGroupCode, TariffIndex tariffIndex,
                                          KeyRevisionNumber keyRevisionNumber, IssuerIdentificationNumber issuerIdentificationNumber,
                                          IndividualAccountIdentificationNumber individualAccountIdentificationNumber,
                                          VendingKey vendingKey1, VendingKey vendingKey2) {
        setIssuerIdentificationNumber(issuerIdentificationNumber);
        setIndividualAccountIdentificationNumber(individualAccountIdentificationNumber);
        setKeyType(keyType);
        setSupplyGroupCode(supplyGroupCode);
        setTariffIndex(tariffIndex);
        setKeyRevisionNumber(keyRevisionNumber);
        setVendingKey1(vendingKey1);
        setVendingKey2(vendingKey2);
    }

    public String getName() {
        return "DKGA03";
    }

    public VendingKey getVendingKey1() {
        return vendingKey1;
    }

    public void setVendingKey1(VendingKey vendingKey1) {
        this.vendingKey1 = vendingKey1;
    }

    public VendingKey getVendingKey2() {
        return vendingKey2;
    }

    public void setVendingKey2(VendingKey vendingKey2) {
        this.vendingKey2 = vendingKey2;
    }

    @Override
    public DecoderKey generate()
            throws InvalidPrimaryAccountNumberBlockComponentsException,
            NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException, IOException {

        PrimaryAccountNumberBlock primaryAccountNumberBlock = createPrimaryAccountNumberBlock();
        ControlBlock controlBlock = createControlBlock();
        byte[] panBlockData = Hex.decode(primaryAccountNumberBlock.getValue());
        byte[] controlBlockData = Hex.decode(controlBlock.getValue());
        byte[] panXorControlBlock = xor(panBlockData, controlBlockData);
        byte[] encryptedPanXorControlBlock = encrypt(panXorControlBlock);
        byte[] decoderKeyData = xor(encryptedPanXorControlBlock, panXorControlBlock);
        return new DecoderKey(decoderKeyData);
    }

    public byte[] encrypt(byte[] dataToEncrypt)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException, IOException {
        byte[] desKey1Data = vendingKey1.getKeyData();
        byte[] desKey2Data = vendingKey2.getKeyData();
        byte[] combinedVendingKey = Utils.combine(desKey1Data, desKey2Data);
        Cipher ecipher = Cipher.getInstance("DESede/ECB/NoPadding");
        ecipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(combinedVendingKey, "DESede/ECB/NoPadding"));
        return ecipher.doFinal(dataToEncrypt);
    }

    public String decrypt(String str) throws NotImplementedException {
        throw new NotImplementedException();
    }
}
