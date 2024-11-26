package co.nxtgrid.token.generators.tokensgenerator.prism.class0;

import co.nxtgrid.token.domain.*;
import co.nxtgrid.token.domain.encryptionalgorithm.EncryptionAlgorithm;
import co.nxtgrid.token.domain.supplygroupcode.SupplyGroupCode;
import co.nxtgrid.token.domain.token.class0.Class0Token;
import co.nxtgrid.token.generators.tokensgenerator.prism.TokenGenerator;

public abstract class Class0TokenGenerator extends TokenGenerator<Class0Token> {

    protected IndividualAccountIdentificationNumber individualAccountIdentificationNumber;
    protected EncryptionAlgorithm encryptionAlgorithm;
    protected TokenCarrierType tokenCarrierType;
    protected SupplyGroupCode supplyGroupCode;
    protected KeyRevisionNumber keyRevisionNumber;
    protected Amount amountPurchased;
    protected KeyExpiryNumber keyExpiryNumber;
    protected TariffIndex tariffIndex;

    public Class0TokenGenerator(String requestID,
                                String host, int port, String realm,
                                String username, String password,
                                IndividualAccountIdentificationNumber
                                        individualAccountIdentificationNumber,
                                EncryptionAlgorithm encryptionAlgorithm,
                                TokenCarrierType tokenCarrierType,
                                SupplyGroupCode supplyGroupCode,
                                KeyRevisionNumber keyRevisionNumber,
                                Amount amountPurchased,
                                KeyExpiryNumber keyExpiryNumber,
                                TariffIndex tariffIndex) {
        super(requestID, host, port, realm, username, password);
        setIndividualAccountIdentificationNumber(individualAccountIdentificationNumber);
        setEncryptionAlgorithm(encryptionAlgorithm);
        setTokenCarrierType(tokenCarrierType);
        setSupplyGroupCode(supplyGroupCode);
        setKeyRevisionNumber(keyRevisionNumber);
        setAmountPurchased(amountPurchased);
        setKeyExpiryNumber(keyExpiryNumber);
        setTariffIndex(tariffIndex);
    }

    public IndividualAccountIdentificationNumber getIndividualAccountIdentificationNumber() {
        return individualAccountIdentificationNumber;
    }

    public EncryptionAlgorithm getEncryptionAlgorithm() {
        return encryptionAlgorithm;
    }

    public void setEncryptionAlgorithm(EncryptionAlgorithm encryptionAlgorithm) {
        this.encryptionAlgorithm = encryptionAlgorithm;
    }

    public void setIndividualAccountIdentificationNumber(IndividualAccountIdentificationNumber individualAccountIdentificationNumber) {
        this.individualAccountIdentificationNumber = individualAccountIdentificationNumber;
    }

    public TokenCarrierType getTokenCarrierType() {
        return tokenCarrierType;
    }

    public void setTokenCarrierType(TokenCarrierType tokenCarrierType) {
        this.tokenCarrierType = tokenCarrierType;
    }

    public SupplyGroupCode getSupplyGroupCode() {
        return supplyGroupCode;
    }

    public void setSupplyGroupCode(SupplyGroupCode supplyGroupCode) {
        this.supplyGroupCode = supplyGroupCode;
    }

    public KeyRevisionNumber getKeyRevisionNumber() {
        return keyRevisionNumber;
    }

    public void setKeyRevisionNumber(KeyRevisionNumber keyRevisionNumber) {
        this.keyRevisionNumber = keyRevisionNumber;
    }

    public Amount getAmountPurchased() {
        return amountPurchased;
    }

    public void setAmountPurchased(Amount amountPurchased) {
        this.amountPurchased = amountPurchased;
    }

    public KeyExpiryNumber getKeyExpiryNumber() {
        return keyExpiryNumber;
    }

    public void setKeyExpiryNumber(KeyExpiryNumber keyExpiryNumber) {
        this.keyExpiryNumber = keyExpiryNumber;
    }

    public TariffIndex getTariffIndex() {
        return tariffIndex;
    }

    public void setTariffIndex(TariffIndex tariffIndex) {
        this.tariffIndex = tariffIndex;
    }
}
