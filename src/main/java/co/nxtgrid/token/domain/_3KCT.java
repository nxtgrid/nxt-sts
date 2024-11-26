package co.nxtgrid.token.domain;

import co.nxtgrid.token.domain.base.BitString;

public class _3KCT implements Entity {

    private final String NAME = "_3KCT";
    private BitString bitString ;

    public _3KCT(BitString bitString) {
        setBitString(bitString);
    }

    public String getName() {
        return NAME;
    }

    public BitString getBitString() {
        return bitString;
    }

    public void setBitString(BitString bitString) {
        this.bitString = bitString;
    }
}
