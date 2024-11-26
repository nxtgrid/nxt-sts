package co.nxtgrid.token.generators.decoderkeygenerator;

import co.nxtgrid.token.domain.*;
import co.nxtgrid.token.domain.encryptionalgorithm.EncryptionAlgorithm;
import co.nxtgrid.token.domain.keys.decoder.DecoderKey;
import co.nxtgrid.token.domain.keys.vending.VendingKey;
import co.nxtgrid.token.domain.supplygroupcode.SupplyGroupCode;

public abstract class DecoderKeyGeneratorAlgorithm {

    protected IssuerIdentificationNumber issuerIdentificationNumber;
    protected KeyRevisionNumber keyRevisionNumber;
    protected KeyType keyType;
    protected IndividualAccountIdentificationNumber individualAccountIdentificationNumber;
    protected SupplyGroupCode supplyGroupCode;
    protected TariffIndex tariffIndex;
    protected VendingKey vendingKey;
    protected EncryptionAlgorithm encryptionAlgorithm;

    public DecoderKeyGeneratorAlgorithm() {}

    public abstract String getName();

    public abstract DecoderKey generate() throws Exception;

    public VendingKey getVendingKey() {
        return vendingKey;
    }

    public void setVendingKey(VendingKey vendingKey) {
        this.vendingKey = vendingKey;
    }

    public EncryptionAlgorithm getEncryptionAlgorithm() {
        return encryptionAlgorithm;
    }

    public void setEncryptionAlgorithm(EncryptionAlgorithm encryptionAlgorithm) {
        this.encryptionAlgorithm = encryptionAlgorithm;
    }

    public IssuerIdentificationNumber getIssuerIdentificationNumber() {
        return issuerIdentificationNumber;
    }

    public void setIssuerIdentificationNumber(IssuerIdentificationNumber issuerIdentificationNumber) {
        this.issuerIdentificationNumber = issuerIdentificationNumber;
    }

    public KeyRevisionNumber getKeyRevisionNumber() {
        return keyRevisionNumber;
    }

    public void setKeyRevisionNumber(KeyRevisionNumber keyRevisionNumber) {
        this.keyRevisionNumber = keyRevisionNumber;
    }

    public KeyType getKeyType() {
        return keyType;
    }

    public void setKeyType(KeyType keyType) {
        this.keyType = keyType;
    }

    public IndividualAccountIdentificationNumber getIndividualAccountIdentificationNumber() {
        return individualAccountIdentificationNumber;
    }

    public void setIndividualAccountIdentificationNumber(IndividualAccountIdentificationNumber individualAccountIdentificationNumber) {
        this.individualAccountIdentificationNumber = individualAccountIdentificationNumber;
    }

    public SupplyGroupCode getSupplyGroupCode() {
        return supplyGroupCode;
    }

    public void setSupplyGroupCode(SupplyGroupCode supplyGroupCode) {
        this.supplyGroupCode = supplyGroupCode;
    }

    public TariffIndex getTariffIndex() {
        return tariffIndex;
    }

    public void setTariffIndex(TariffIndex tariffIndex) {
        this.tariffIndex = tariffIndex;
    }

    protected PrimaryAccountNumberBlock createPrimaryAccountNumberBlock() {
        return new PrimaryAccountNumberBlock(issuerIdentificationNumber, individualAccountIdentificationNumber, keyType);
    }

    protected ControlBlock createControlBlock() {
        return new ControlBlock(keyType, supplyGroupCode, tariffIndex, keyRevisionNumber);
    }

    protected byte[] xor(byte[] firstArr, byte[] secondArr) {
        int bytesCounter = 0;
        byte[] xorRes = new byte[firstArr.length];
        for (byte byteData : firstArr)
            xorRes[bytesCounter] = (byte) (0xff & ((int) byteData ^ (int) secondArr[bytesCounter++]));
        return xorRes;
    }
}
