package co.nxtgrid.strategy;

import co.nxtgrid.api.TokenRequest;

public interface TokenStrategy {

    /** Returns true if this strategy handles the given token type. */
    boolean supports(String type);

    /** Generates the 20-digit STS token string for the given request. */
    String generate(TokenRequest request) throws Exception;
}
