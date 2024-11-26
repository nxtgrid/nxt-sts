package co.nxtgrid.token.generators.tokensgenerator.prism;

import co.nxtgrid.token.domain.encryptionalgorithm.EncryptionAlgorithm;
import co.nxtgrid.token.domain.token.Token;

import java.util.List;

public abstract class TokenGenerator<T extends Token>{

    protected String requestID;
    protected String host;
    protected int port;
    protected String realm;
    protected String username;
    protected String password;

    protected EncryptionAlgorithm encryptionAlgorithm = null;
    protected abstract <T> List<T> generate() throws Exception;

    public TokenGenerator(String requestID, String host, int port, String realm,
                          String username, String password) {
        this.setRequestID(requestID);
        this.setHost(host);
        this.setPort(port);
        this.setRealm(realm);
        this.setUsername(username);
        this.setPassword(password);
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
