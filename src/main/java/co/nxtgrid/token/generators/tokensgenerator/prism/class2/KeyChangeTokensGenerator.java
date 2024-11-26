package co.nxtgrid.token.generators.tokensgenerator.prism.class2;

import co.nxtgrid.hsm.prism.impl.PrismClientFacade;
import co.nxtgrid.hsm.prism.impl.PrismHSMConnector;
import co.nxtgrid.token.domain.*;
import co.nxtgrid.token.domain.encryptionalgorithm.EncryptionAlgorithm;
import co.nxtgrid.token.domain.supplygroupcode.SupplyGroupCode;
import co.nxtgrid.token.domain.token.class2.Class2Token;

import java.util.List;

public class KeyChangeTokensGenerator extends Class2TokenGenerator {

    private boolean allow3Kct;
    private SupplyGroupCode newSupplyGroupCode;
    private KeyRevisionNumber newKeyRevisionNumber;
    private TariffIndex newTariffIndex;

    public KeyChangeTokensGenerator(String requestID,
                                    String host, int port, String realm,
                                    String username, String password,
                                    IndividualAccountIdentificationNumber iain,
                                    EncryptionAlgorithm encryptionAlgorithm,
                                    TokenCarrierType tokenCarrierType,
                                    SupplyGroupCode supplyGroupCode,
                                    KeyRevisionNumber keyRevisionNumber,
                                    KeyExpiryNumber keyExpiryNumber,
                                    TariffIndex tariffIndex,
                                    SupplyGroupCode newSupplyGroupCode,
                                    KeyRevisionNumber newKeyRevisionNumber,
                                    TariffIndex newTariffIndex,
                                    boolean allow3Kct) {
        super(requestID, host, port, realm, username, password,
                iain, encryptionAlgorithm, tokenCarrierType, supplyGroupCode,
                keyRevisionNumber, keyExpiryNumber, tariffIndex);
        setAllow3Kct(allow3Kct);
        setNewSupplyGroupCode(newSupplyGroupCode);
        setNewKeyRevisionNumber(newKeyRevisionNumber);
        setNewTariffIndex(newTariffIndex);
    }

    public boolean getAllow3Kct() {
        return allow3Kct;
    }

    public void setAllow3Kct(boolean allow3Kct) {
        this.allow3Kct = allow3Kct;
    }

    public SupplyGroupCode getNewSupplyGroupCode() {
        return newSupplyGroupCode;
    }

    public void setNewSupplyGroupCode(SupplyGroupCode newSupplyGroupCode) {
        this.newSupplyGroupCode = newSupplyGroupCode;
    }

    public KeyRevisionNumber getNewKeyRevisionNumber() {
        return newKeyRevisionNumber;
    }

    public void setNewKeyRevisionNumber(KeyRevisionNumber newKeyRevisionNumber) {
        this.newKeyRevisionNumber = newKeyRevisionNumber;
    }

    public TariffIndex getNewTariffIndex() {
        return newTariffIndex;
    }

    public void setNewTariffIndex(TariffIndex newTariffIndex) {
        this.newTariffIndex = newTariffIndex;
    }

    public List<Class2Token> generate() throws Exception {
        PrismHSMConnector connector = new PrismHSMConnector();
        PrismClientFacade facade = new PrismClientFacade(getHost(), getPort(), getRealm(),
                                                        getUsername(), getPassword(), connector);
        return facade.generateDecoderKeyTokens(getRequestID(), iain, encryptionAlgorithm, tokenCarrierType,
                supplyGroupCode, keyRevisionNumber, keyExpiryNumber, tariffIndex,
                newSupplyGroupCode, newKeyRevisionNumber, newTariffIndex, allow3Kct);
    }
}
