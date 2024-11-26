package co.nxtgrid.token.domain;

import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.exceptions.InvalidManufacturerCodeException;
import co.nxtgrid.token.miscellaneous.Strings;

public class ManufacturerCode implements Entity {

    private String manufacturerCode = "0";
    private BitString manufacturerCodeBitString;
    private final String NAME = "ManufacturerCode";

    public ManufacturerCode(String manufacturerCode)
        throws InvalidManufacturerCodeException {
        setManufacturerCode(manufacturerCode);
    }


    public ManufacturerCode(BitString manufacturerCodeBitString)
        throws InvalidManufacturerCodeException {
        if (manufacturerCodeBitString.getLength() != 8 &&
                manufacturerCodeBitString.getLength() != 16)
            throw new InvalidManufacturerCodeException(Strings.INVALID_MANUFACTURER_CODE) ;
        if (manufacturerCodeBitString.getLength() == 8)
            manufacturerCode = String.format("%04d", manufacturerCodeBitString.getValue());
        else if (manufacturerCodeBitString.getLength() == 16)
            manufacturerCode = String.format("%016d", manufacturerCodeBitString.getValue());
        this.manufacturerCodeBitString = manufacturerCodeBitString;
    }

    public String getValue() {
        return manufacturerCode;
    }

    public void setManufacturerCode(String manufacturerCode)
        throws InvalidManufacturerCodeException {
        if (!manufacturerCode.matches("[0-9]{2}") && !manufacturerCode.matches("[0-9]{4}"))
            throw new InvalidManufacturerCodeException(Strings.INVALID_MANUFACTURER_CODE) ;
        this.manufacturerCode = manufacturerCode;
        this.manufacturerCodeBitString = new BitString(Long.parseLong(manufacturerCode));
        if (manufacturerCode.length() == 2)
            this.manufacturerCodeBitString.setLength(8);
        else if (manufacturerCode.length() == 4)
            this.manufacturerCodeBitString.setLength(16);
    }

    public BitString getBitString() {
        return manufacturerCodeBitString;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
