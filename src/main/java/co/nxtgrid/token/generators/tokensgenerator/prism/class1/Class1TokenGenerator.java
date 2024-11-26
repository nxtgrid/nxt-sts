package co.nxtgrid.token.generators.tokensgenerator.prism.class1;

import co.nxtgrid.token.domain.Control;
import co.nxtgrid.token.domain.IndividualAccountIdentificationNumber;
import co.nxtgrid.token.domain.ManufacturerCode;
import co.nxtgrid.token.domain.token.class1.Class1Token;
import co.nxtgrid.token.generators.tokensgenerator.prism.TokenGenerator;

public abstract class Class1TokenGenerator extends TokenGenerator<Class1Token> {

    protected Control control;
    protected IndividualAccountIdentificationNumber iain;
    protected ManufacturerCode manufacturerCode;

    public Class1TokenGenerator(String requestID, String host, int port, String realm,
                                String username, String password,
                                IndividualAccountIdentificationNumber iain,
                                Control control,
                                ManufacturerCode manufacturerCode) {
        super(requestID, host, port, realm, username, password);
        setIain(iain);
        setControl(control);
        setManufacturerCode(manufacturerCode);
    }

    public Control getControl() {
        return control;
    }

    public void setControl(Control control) {
        this.control = control;
    }

    public IndividualAccountIdentificationNumber getIain() {
        return iain;
    }

    public void setIain(IndividualAccountIdentificationNumber iain) {
        this.iain = iain;
    }

    public ManufacturerCode getManufacturerCode() {
        return manufacturerCode;
    }

    public void setManufacturerCode(ManufacturerCode manufacturerCode) {
        this.manufacturerCode = manufacturerCode;
    }

}
