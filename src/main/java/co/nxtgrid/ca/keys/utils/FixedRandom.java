package co.nxtgrid.ca.keys.utils;

import java.security.MessageDigest;
import java.security.SecureRandom;

public class FixedRandom extends SecureRandom {
    MessageDigest sha;
    byte[] state;

    public FixedRandom(String providerAbbr) {
        try {
            this.sha = MessageDigest.getInstance("SHA-1", providerAbbr);
            this.state = sha.digest();
        } catch (Exception e) {
            throw new RuntimeException("can't find SHA-1!");
        }
    }

    public void nextBytes(byte[] bytes) {
        int off = 0;
        sha.update(state);
        while (off < bytes.length) {
            state = sha.digest();
            if (bytes.length - off > state.length) {
                System.arraycopy(state, 0, bytes, off, state.length);
            } else {
                System.arraycopy(state, 0, bytes, off, bytes.length - off);
            }
            off += state.length;
            sha.update(state);
        }
    }
}
