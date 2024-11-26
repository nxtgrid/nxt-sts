package co.nxtgrid.tokens.service.impl.generate.class1;


import co.nxtgrid.token.domain.Control;
import co.nxtgrid.token.domain.ManufacturerCode;
import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.tokens.service.impl.generate.Generator;

import java.util.Map;

public class Class1Generator extends Generator {

    public Class1Generator(String requestID, TokenType tokenType) {
        super(requestID, tokenType);
    }

    protected static ManufacturerCode getManufacturerCode(Map<String, String> params)
            throws Exception {
        return new ManufacturerCode(params.get("manufacturer_code"));
    }

    protected static Control getControl(Map<String, String> params)
        throws Exception {
        String subclass = params.get("subclass");

        int controlBitStringLength = 0;
        if (subclass.equals("0"))
            controlBitStringLength = 36;
        else if (subclass.equals("1"))
            controlBitStringLength = 28;

        BitString controlBitString = new BitString(Long.parseLong(params.get("control")));
        controlBitString.setLength(controlBitStringLength);
        return new Control(controlBitString, getManufacturerCode(params));
    }
}
