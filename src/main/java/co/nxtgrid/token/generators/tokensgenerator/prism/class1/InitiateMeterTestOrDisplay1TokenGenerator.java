package co.nxtgrid.token.generators.tokensgenerator.prism.class1;

import co.nxtgrid.hsm.prism.impl.PrismClientFacade;
import co.nxtgrid.hsm.prism.impl.PrismHSMConnector;
import co.nxtgrid.token.domain.Control;
import co.nxtgrid.token.domain.IndividualAccountIdentificationNumber;
import co.nxtgrid.token.domain.ManufacturerCode;
import co.nxtgrid.token.domain.token.class1.InitiateMeterTestOrDisplay1Token;

import java.util.Arrays;
import java.util.List;

public class InitiateMeterTestOrDisplay1TokenGenerator extends Class1TokenGenerator {

    public InitiateMeterTestOrDisplay1TokenGenerator(String requestID,
                                                     String host, int port, String realm,
                                                     String username, String password,
                                                     IndividualAccountIdentificationNumber iain,
                                                     Control control,
                                                     ManufacturerCode manufacturerCode) {
        super(requestID, host, port, realm, username,
                password, iain, control, manufacturerCode);
    }

    public List<InitiateMeterTestOrDisplay1Token> generate() throws Exception {
        PrismHSMConnector connector = new PrismHSMConnector();
        PrismClientFacade prismClientFacade = new PrismClientFacade(getHost(), getPort(), getRealm(),
                                                                    getUsername(), getPassword(), connector);
        return Arrays.asList(
                prismClientFacade
                        .generateInitiateMeterTestOrDisplay1Token(getRequestID(), iain, control, manufacturerCode));
    }
}
