package co.nxtgrid.token.generators.tokensdecoder.error;

public class UsedError extends ValidationError {

    private final static String NAME = "UsedError" ;
    private final static String VALUE = "The TID value as recorded in the nectar.token is already recorded in the " +
            "memory store of the payment meter" ;

    public UsedError () {
        super (NAME, VALUE) ;
    }
}