package co.nxtgrid.token.exceptions.decode;

public class FormatError extends TokenError {

    public FormatError () {
        super("FormatError", "One or more data elements in the token does not comply with the\n" +
                "required format for that element");
    }
}
