package co.nxtgrid.token.generators.tokensdecoder.result;

import co.nxtgrid.token.generators.tokensdecoder.state.AcceptTokenState;

public class TokenResult extends Result {

    public TokenResult (AcceptTokenState acceptedTokenState) {
        super (acceptedTokenState, null) ;
    }
}