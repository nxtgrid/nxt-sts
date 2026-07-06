package co.nxtgrid.token.exceptions.decode;

public abstract class AuthenticationError extends Error {

    public AuthenticationError(String name, String errorCodeValue) {
        super(name, errorCodeValue) ;
    }
}

