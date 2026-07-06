package co.nxtgrid.token.exceptions.decode;

public class CRCError extends AuthenticationError {

    public CRCError() {
        super ("Crc Error", "The Crc value in the token is different to the Crc value as " +
                "calculated from the data in the token") ;
    }
}
