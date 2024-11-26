package co.nxtgrid.token.generators.tokensdecoder.error;

public class CRCError extends AuthenticationError {

    public CRCError() {
        super ("Crc Error", "The Crc value in the token is different to the Crc value as " +
                "calculated from the data in the token") ;
    }
}
