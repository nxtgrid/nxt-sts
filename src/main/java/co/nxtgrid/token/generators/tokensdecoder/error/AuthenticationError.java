package co.nxtgrid.token.generators.tokensdecoder.error;

public abstract class AuthenticationError extends Error {

    public AuthenticationError(String name, String errorCodeValue) {
        super(name, errorCodeValue) ;
    }
}

