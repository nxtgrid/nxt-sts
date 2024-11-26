package co.nxtgrid.token.domain.base;

public class Bit {

    private char val = '0' ;

    public Bit(char val) {
        setValue(val);
    }

    public char getValue() {
        return val ;
    }

    public void setValue(char val) {
        this.val = val ;
    }

    public String toString () {
        return Character.toString(val) ;
    }
}
