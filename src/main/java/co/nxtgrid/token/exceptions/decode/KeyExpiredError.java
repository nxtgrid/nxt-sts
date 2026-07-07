package co.nxtgrid.token.exceptions.decode;

public class KeyExpiredError extends ValidationError {

    private final static String NAME = "KeyExpiredError" ;
    private final static String VALUE = "The TID value as recorded in the co.nxtgrid.co.nxtgrid.token is larger than the KEN\n" +
            "stored in the payment meter memory" ;

    public  KeyExpiredError() {
        super (NAME, VALUE) ;
    }
}
