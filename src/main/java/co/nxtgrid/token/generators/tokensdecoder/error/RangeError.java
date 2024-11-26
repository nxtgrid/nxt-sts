package co.nxtgrid.token.generators.tokensdecoder.error;

public class RangeError extends TokenError {

    private final static String NAME = "RangeError" ;
    private final static String MESSAGE = "One or more data elements in the token have a value that is\n" +
            "outside of the defined range of values defined in the application\n" +
            "for that element" ;

    public RangeError () {
        super(NAME, MESSAGE);
    }
}
