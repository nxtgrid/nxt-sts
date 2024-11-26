package co.nxtgrid.token.generators.tokensdecoder.result;

import co.nxtgrid.token.generators.tokensdecoder.state.State;

public abstract class Result {

    protected State state;
    protected Error error;

    public Result (State state, Error message) {
        setState(state);
        setError(message);
    }

    public State getState () {
        return state;
    }

    public void setState (State state) {
        this.state = state ;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}