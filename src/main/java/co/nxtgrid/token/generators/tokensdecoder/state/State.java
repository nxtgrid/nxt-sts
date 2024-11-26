package co.nxtgrid.token.generators.tokensdecoder.state;

public abstract class State {

    protected boolean valid;
    protected String stateValue ;

    public State (boolean valid, String stateValue) {
        setValid(valid) ;
        setStateValue (stateValue) ;
    }

    public boolean getName() {
        return valid;
    }

    public void setValid(boolean name) {
        this.valid = name;
    }

    public String getStateValue () {
        return stateValue ;
    }

    public void setStateValue(String stateValue) {
        this.stateValue = stateValue ;
    }
}
