package co.nxtgrid.token.generators.tokensgenerator.prism.class0;

import co.nxtgrid.hsm.prism.impl.PrismClientFacade;
import co.nxtgrid.hsm.prism.impl.PrismHSMConnector;
import co.nxtgrid.token.domain.*;
import co.nxtgrid.token.domain.encryptionalgorithm.EncryptionAlgorithm;
import co.nxtgrid.token.domain.supplygroupcode.SupplyGroupCode;
import co.nxtgrid.token.domain.token.class0.TransferWaterCreditToken;

import java.util.Arrays;
import java.util.List;

public class TransferWaterCreditPrismTokenGenerator
        extends Class0TokenGenerator {

    public TransferWaterCreditPrismTokenGenerator(String requestID,
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
        super(requestID, host, port, realm, username, password, individualAccountIdentificationNumber,
                encryptionAlgorithm, tokenCarrierType, supplyGroupCode,
                keyRevisionNumber, amountPurchased, keyExpiryNumber, tariffIndex);
    }

    @Override
    public List<TransferWaterCreditToken> generate() throws Exception {
        PrismHSMConnector connector = new PrismHSMConnector();
        PrismClientFacade prismClientFacade = new PrismClientFacade(getHost(), getPort(), getRealm(),
                                                                    getUsername(), getPassword(), connector);
        return Arrays.asList(prismClientFacade
                .generateTransferWaterCreditToken(getRequestID(), individualAccountIdentificationNumber,
                        encryptionAlgorithm, tokenCarrierType, supplyGroupCode, keyRevisionNumber,
                        amountPurchased, keyExpiryNumber, tariffIndex));
    }
}
