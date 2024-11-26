package co.nxtgrid.token.generators.tokensdecoder.error;

public class OldError extends ValidationError {

    private static final String NAME = "OldError" ;
    private static final String VALUE = "The TID VALUE as recorded in the token is older than the oldest " +
            "VALUE of recorded values recorded in the memory store of the " +
            "payment meter" ;

    public OldError () {
        super (NAME, VALUE) ;
    }
}