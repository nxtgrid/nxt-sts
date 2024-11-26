package co.nxtgrid.token.domain;

import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.exceptions.InvalidRegisterBitString;
import co.nxtgrid.token.miscellaneous.Strings;

public class Register implements Entity {

    private final String NAME = "Register";
    private BitString registerBitString = new BitString();

    public Register(BitString registerBitString)
        throws InvalidRegisterBitString {
        setRegisterBitString(registerBitString);
    }

    public String getName() {
        return NAME;
    }

    public BitString getBitString() {
        return registerBitString;
    }

    public void setRegisterBitString(BitString registerBitString)
        throws InvalidRegisterBitString {
        if (registerBitString.getLength() != 16)
            throw new InvalidRegisterBitString(Strings.INVALID_REGISTER_BITSTRING);
        this.registerBitString = registerBitString;
    }
}

