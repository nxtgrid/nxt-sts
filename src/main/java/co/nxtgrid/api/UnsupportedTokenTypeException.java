package co.nxtgrid.api;

public class UnsupportedTokenTypeException extends RuntimeException {

    private final TokenType type;

    public UnsupportedTokenTypeException(TokenType type) {
        super("Unsupported token type: " + type);
        this.type = type;
    }

    public TokenType getType() {
        return type;
    }
}
