package co.nxtgrid.token.generators.tokensgenerator.prism.class2;

import co.nxtgrid.token.domain.*;
import co.nxtgrid.token.domain.encryptionalgorithm.EncryptionAlgorithm;
import co.nxtgrid.token.domain.supplygroupcode.SupplyGroupCode;
import co.nxtgrid.token.domain.token.Token;
import co.nxtgrid.token.generators.tokensgenerator.prism.TokenGenerator;

public abstract class  Class2TokenGenerator<T extends Token> extends TokenGenerator<T> {

    protected IndividualAccountIdentificationNumber iain;
    protected EncryptionAlgorithm encryptionAlgorithm;
    protected TokenCarrierType tokenCarrierType;
    protected SupplyGroupCode supplyGroupCode;
    protected KeyRevisionNumber keyRevisionNumber;
    protected KeyExpiryNumber keyExpiryNumber;
    protected TariffIndex tariffIndex;

    public Class2TokenGenerator(String requestID,
                                String host, int port, String realm,
                                String username, String password,
                                IndividualAccountIdentificationNumber
                                        individualAccountIdentificationNumber,
                                EncryptionAlgorithm encryptionAlgorithm,
                                TokenCarrierType tokenCarrierType,
                                SupplyGroupCode supplyGroupCode,
                                KeyRevisionNumber keyRevisionNumber,
                                KeyExpiryNumber keyExpiryNumber,
                                TariffIndex tariffIndex) {
        super(requestID, host, port, realm, username, password);
        setIndividualAccountIdentificationNumber(individualAccountIdentificationNumber);
        setEncryptionAlgorithm(encryptionAlgorithm);
        setTokenCarrierType(tokenCarrierType);
        setSupplyGroupCode(supplyGroupCode);
        setKeyRevisionNumber(keyRevisionNumber);
        setKeyExpiryNumber(keyExpiryNumber);
        setTariffIndex(tariffIndex);
    }

    public IndividualAccountIdentificationNumber getIndividualAccountIdentificationNumber() {
        return iain;
    }

    public EncryptionAlgorithm getEncryptionAlgorithm() {
        return encryptionAlgorithm;
    }

    public void setEncryptionAlgorithm(EncryptionAlgorithm encryptionAlgorithm) {
        this.encryptionAlgorithm = encryptionAlgorithm;
    }

    public void setIndividualAccountIdentificationNumber(IndividualAccountIdentificationNumber iain) {
        this.iain = iain;
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
