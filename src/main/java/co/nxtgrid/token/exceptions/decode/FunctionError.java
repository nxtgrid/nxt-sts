package co.nxtgrid.token.exceptions.decode;

public class FunctionError extends TokenError {

    private final static String NAME = "TokenError" ;
    private final static String MESSAGE = "The particular function to execute the co.nxtgrid.co.nxtgrid.token is not implemented" ;

    public FunctionError () {
        super (NAME, MESSAGE) ;
    }
}