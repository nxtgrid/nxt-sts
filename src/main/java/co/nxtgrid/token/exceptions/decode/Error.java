package co.nxtgrid.token.exceptions.decode;

public abstract class Error extends Exception {

    private String name ;
    protected String errorCodeValue;

    public Error(String name, String errorCodeValue) {
        setName(name) ;
        setErrorCodeValue(errorCodeValue) ;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getErrorCodeValue() {
        return errorCodeValue;
    }

    public void setErrorCodeValue(String errorCodeValue) {
        this.errorCodeValue = errorCodeValue;
    }
}

