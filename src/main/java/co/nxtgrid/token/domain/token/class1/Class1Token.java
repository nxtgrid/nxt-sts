package co.nxtgrid.token.domain.token.class1;

import co.nxtgrid.token.domain.Control;
import co.nxtgrid.token.domain.ManufacturerCode;
import co.nxtgrid.token.domain.token.Token;
import co.nxtgrid.token.domain.tokenclass.TokenClass;
import co.nxtgrid.token.domain.tokensubclass.TokenSubClass;

import java.util.HashMap;

public abstract class Class1Token extends Token {

    private Control control;
    private ManufacturerCode manufacturerCode ;

    public Class1Token(String requestID) {
        super(requestID);
    }

    public Class1Token(String requestID,
                       TokenClass tokenClass,
                       TokenSubClass tokenSubClass,
                       Control control,
                       ManufacturerCode manufacturerCode) {
        super(requestID);
        setTokenClass(tokenClass);
        setTokenSubClass(tokenSubClass);
        setControl(control);
        setManufacturerCode(manufacturerCode);
    }

    public Control getControl() {
        return control;
    }

    public void setControl(Control control) {
        this.control = control;
    }

    public ManufacturerCode getManufacturerCode() {
        return manufacturerCode;
    }

    public void setManufacturerCode(ManufacturerCode manufacturerCode) {
        this.manufacturerCode = manufacturerCode;
    }

    @Override
    public HashMap<String, Object> getParams() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("class", getTokenClass().getBitString().getValue());
        params.put("subclass", getTokenSubClass().getBitString().getValue());
        params.put("control", getControl().getBitString().getValue());
        params.put("manufacturer_code", getManufacturerCode().getValue());
        params.put("type", getType());
        return params;
    }
}
