package co.nxtgrid.token.exceptions.decode;

public class OverflowError extends TokenError {

    public OverflowError () {
        super ("OverflowError", "The credit maximumPhasePowerUnbalanceLimit in the payment meter would overflow if the\n" +
                "token were to be accepted; the token is not accepted") ;
    }
}
