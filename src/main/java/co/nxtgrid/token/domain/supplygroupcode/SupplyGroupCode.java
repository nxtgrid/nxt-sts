package co.nxtgrid.token.domain.supplygroupcode;

import co.nxtgrid.token.exceptions.InvalidSupplyGroupCodeException;
import co.nxtgrid.token.miscellaneous.Strings;

public class SupplyGroupCode {

    private String supplyGroupCode;
    private final String NAME = "Supply Group Code";

    public SupplyGroupCode(String supplyGroupCode)
        throws InvalidSupplyGroupCodeException {
        setValue(supplyGroupCode);
    }

    public String getValue() {
        return supplyGroupCode;
    }

    public void setValue(String supplyGroupCode)
        throws InvalidSupplyGroupCodeException {
        if (!supplyGroupCode.matches("[0-9]{6}"))
            throw new InvalidSupplyGroupCodeException(Strings.INVALID_SUPPLY_GROUP_CODE) ;
        this.supplyGroupCode = supplyGroupCode;
    }

    public String getName() {
        return NAME;
    }
}
