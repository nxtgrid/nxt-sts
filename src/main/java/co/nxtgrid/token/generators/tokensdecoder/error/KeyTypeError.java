package co.nxtgrid.token.generators.tokensdecoder.error;

public class KeyTypeError extends TokenError {

    public KeyTypeError () {
        super("KeyTypeError", "The key may not be changed to this type in accordance with the\n" +
                "key change rules given in 6.5.2.4." );
    }
}
