package co.nxtgrid.token.generators.tokensdecoder.error;

public class ValidationError extends Error {

    public ValidationError (String name, String errorCodeValue) {
        super(name, errorCodeValue) ;
    }
}
