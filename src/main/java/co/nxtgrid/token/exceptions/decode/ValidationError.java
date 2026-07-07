package co.nxtgrid.token.exceptions.decode;

public class ValidationError extends Error {

    public ValidationError (String name, String errorCodeValue) {
        super(name, errorCodeValue) ;
    }
}
