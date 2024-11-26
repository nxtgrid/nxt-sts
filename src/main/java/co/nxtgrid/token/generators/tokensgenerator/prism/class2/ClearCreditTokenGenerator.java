package co.nxtgrid.token.generators.tokensgenerator.prism.class2;

import co.nxtgrid.hsm.prism.impl.PrismClientFacade;
import co.nxtgrid.hsm.prism.impl.PrismHSMConnector;
import co.nxtgrid.token.domain.*;
import co.nxtgrid.token.domain.encryptionalgorithm.EncryptionAlgorithm;
import co.nxtgrid.token.domain.supplygroupcode.SupplyGroupCode;
import co.nxtgrid.token.domain.token.class2.ClearCreditToken;

import java.util.Arrays;
import java.util.List;

public class ClearCreditTokenGenerator extends Class2TokenGenerator {

    public ClearCreditTokenGenerator(String requestID,
                                     String host, int port, String realm,
                                     String username, String password,
                                     IndividualAccountIdentificationNumber iain,
                                     EncryptionAlgorithm encryptionAlgorithm,
                                     TokenCarrierType tokenCarrierType,
                                     SupplyGroupCode supplyGroupCode,
                                     KeyRevisionNumber keyRevisionNumber,
                                     KeyExpiryNumber keyExpiryNumber,
                                     TariffIndex tariffIndex) {
        super(requestID, host, port, realm, username, password,
                iain, encryptionAlgorithm, tokenCarrierType, supplyGroupCode,
                keyRevisionNumber, keyExpiryNumber, tariffIndex);
    }

    public List<ClearCreditToken> generate() throws Exception {
        PrismHSMConnector connector = new PrismHSMConnector();
        PrismClientFacade facade = new PrismClientFacade(getHost(), getPort(), getRealm(),
                                                        getUsername(), getPassword(), connector);
        return Arrays.asList(facade.generateClearCreditToken(getRequestID(), iain, encryptionAlgorithm,
                                                tokenCarrierType, supplyGroupCode, keyRevisionNumber,
                                                keyExpiryNumber,  tariffIndex));
    }
}
