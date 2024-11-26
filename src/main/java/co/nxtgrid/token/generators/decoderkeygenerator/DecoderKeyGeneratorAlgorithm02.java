package co.nxtgrid.token.generators.decoderkeygenerator;

import co.nxtgrid.token.domain.*;
import co.nxtgrid.token.domain.encryptionalgorithm.DataEncryptionAlgorithm;
import co.nxtgrid.token.domain.keys.decoder.DecoderKey;
import co.nxtgrid.token.domain.keys.vending.VendingKey;
import co.nxtgrid.token.domain.supplygroupcode.SupplyGroupCode;
import org.bouncycastle.util.encoders.Hex;

public class DecoderKeyGeneratorAlgorithm02 extends DecoderKeyGeneratorAlgorithm {

    private VendingKey vendingKey;

    public DecoderKeyGeneratorAlgorithm02(KeyType keyType, SupplyGroupCode supplyGroupCode, TariffIndex tariffIndex,
                                          KeyRevisionNumber keyRevisionNumber, IssuerIdentificationNumber issuerIdentificationNumber,
                                          IndividualAccountIdentificationNumber individualAccountIdentificationNumber, VendingKey vudk) {
        setIssuerIdentificationNumber(issuerIdentificationNumber);
        setIndividualAccountIdentificationNumber(individualAccountIdentificationNumber);
        setKeyType(keyType);
        setSupplyGroupCode(supplyGroupCode);
        setTariffIndex(tariffIndex);
        setKeyRevisionNumber(keyRevisionNumber);
        setVendingKey(vudk);
    }

    public String getName() {
        return "DKGA02";
    }

    public VendingKey getVendingKey() {
        return vendingKey;
    }

    public void setVendingKey(VendingKey vendingKey) {
        this.vendingKey = vendingKey;
    }

    @Override
    public DecoderKey generate() throws Exception {
        PrimaryAccountNumberBlock primaryAccountNumberBlock = createPrimaryAccountNumberBlock();
        ControlBlock controlBlock = createControlBlock();
        byte[] panBlockData = Hex.decode(primaryAccountNumberBlock.getValue());
        byte[] controlBlockData = Hex.decode(controlBlock.getValue());
        byte[] panXorControlBlock = xor(panBlockData, controlBlockData);
        byte[] encryptedPanXorControlBlock = encrypt(vendingKey, panXorControlBlock);
        byte[] doubleEncryptedKeyData = xor(encryptedPanXorControlBlock, panXorControlBlock);
        byte[] decoderKeyData = xor(doubleEncryptedKeyData, vendingKey.getKeyData());
        byte[] reversedDecoderKey = reverseByteString(decoderKeyData);
        DecoderKey decoderKey = new DecoderKey();
        decoderKey.setKeyData(reversedDecoderKey);
        return decoderKey;
    }

    private byte[] reverseByteString(byte[] initial) {
        byte[] reversed = new byte[initial.length];
        for (int newPos = 0, oldPos = initial.length - 1; oldPos >= 0; oldPos--, newPos++)
            reversed[newPos] = initial[oldPos];
        return reversed;
    }

    public byte[] encrypt (VendingKey vendingKey, byte[] dataToEncrypt) throws Exception {
        DataEncryptionAlgorithm dataEncryptionAlgorithm = new DataEncryptionAlgorithm();
        return dataEncryptionAlgorithm.encrypt(vendingKey, dataToEncrypt);
    }
}
