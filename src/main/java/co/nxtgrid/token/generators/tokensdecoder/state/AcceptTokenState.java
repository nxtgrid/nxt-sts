package co.nxtgrid.token.generators.tokensdecoder.state;

public class AcceptTokenState extends TokenState {

    private final static boolean ACCEPTED = true ;
    final static String STATE_VALUE = "Token was successfully processed" ;

    public AcceptTokenState () {
        super (ACCEPTED, STATE_VALUE) ;
    }
}