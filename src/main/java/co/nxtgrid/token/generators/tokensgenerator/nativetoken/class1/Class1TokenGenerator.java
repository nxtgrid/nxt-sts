package co.nxtgrid.token.generators.tokensgenerator.nativetoken.class1;

import co.nxtgrid.token.domain.Control;
import co.nxtgrid.token.domain.Crc;
import co.nxtgrid.token.domain.ManufacturerCode;
import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.token.class1.Class1Token;
import co.nxtgrid.token.exceptions.BitConcatOverflowError;
import co.nxtgrid.token.generators.tokensgenerator.nativetoken.TokenGenerator;

public abstract class Class1TokenGenerator extends TokenGenerator<Class1Token> {

    protected Control control;
    protected ManufacturerCode manufacturerCode;

    public Class1TokenGenerator(String requestID, Control control, ManufacturerCode manufacturerCode) {
        super(requestID);
        setControl(control);
        setManufacturerCode(manufacturerCode);
    }

    @Override
    public BitString generate64BitDataBlock(Class1Token token)
            throws BitConcatOverflowError {

        BitString tokenClass = token.getTokenClass().getBitString();
        BitString tokenSubClass = token.getTokenSubClass().getBitString();
        BitString control = token.getControl().getBitString();
        BitString mfrCode = token.getManufacturerCode().getBitString();
        BitString crc = new Crc().generateCRC(mfrCode.concat(control, tokenSubClass, tokenClass));
        BitString _64BitDataBlock = crc.concat(mfrCode, control, tokenSubClass);
        return _64BitDataBlock;
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
}
