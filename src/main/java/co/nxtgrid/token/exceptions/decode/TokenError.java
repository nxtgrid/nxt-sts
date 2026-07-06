package co.nxtgrid.token.exceptions.decode;

public class TokenError extends Error {

    public TokenError(String name, String errorCodeValue) {
        super(name, errorCodeValue) ;
    }
}