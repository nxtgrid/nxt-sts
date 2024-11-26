package co.nxtgrid.ca;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

public class Provider {

    public Provider() {}

    public void setProvider() {
        Security.addProvider(new BouncyCastleProvider()) ;
    }

    public String getProviderAbbr() {
        return "BC" ;
    }
}
