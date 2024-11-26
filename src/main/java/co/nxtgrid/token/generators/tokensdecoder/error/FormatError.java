package co.nxtgrid.token.generators.tokensdecoder.error;

public class FormatError extends TokenError {

    public FormatError () {
        super("FormatError", "One or more data elements in the token does not comply with the\n" +
                "required format for that element");
    }
}
