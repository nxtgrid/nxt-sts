package co.nxtgrid.token.domain.keys;

import co.nxtgrid.token.domain.Entity;
import co.nxtgrid.token.domain.base.BitString;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

public abstract class Key implements Entity {

    protected byte[] keyData;

    public Key() {
        Security.addProvider(new BouncyCastleProvider());
    }

    public Key(byte[] keyData) {
        setKeyData(keyData);
    }

    public byte[] getKeyData() {
        return keyData;
    }

    public void setKeyData(byte[] keyBytes) {
        this.keyData = keyBytes;
    }

    public abstract String bitsToString();

    public abstract BitString getBitString();

    public byte[] complement(byte[] ec) {
        byte[] complemented = new byte[ec.length];
        for (int b = 0; b < 8; b++) {
            complemented[b] = (byte) ~ec[b];
        }
        return complemented;
    }

    public byte[] rotateComplemented(byte[] complemented) {
        int complementedLength = complemented.length * 8;
        int bytesToRotate = 12;
        return rotate(complemented, complementedLength, bytesToRotate, RotateDirection.RIGHT);
    }

    public byte[] rotateRight(byte[] in, int steps) {
        int len = in.length * 8;
        return rotate(in, len, steps, RotateDirection.RIGHT);
    }

    public byte[] rotateLeft(byte[] in, int steps) {
        int len = in.length * 8;
        return rotate(in, len, steps, RotateDirection.LEFT);
    }

    public byte[] rotate(byte[] in, int len, int steps, RotateDirection rotateDirection) {
        int numOfBytes = (len - 1) / 8 + 1;
        byte[] out = new byte[numOfBytes];
        for (int i = 0; i < len; i++) {
            int newPos = (i + steps) % len;
            if (rotateDirection == RotateDirection.RIGHT) {
                int val = getBit(in, newPos);
                setBit(out, i, val);
            } else if (rotateDirection == RotateDirection.LEFT) {
                int val = getBit(in, i);
                setBit(out, newPos, val);
            }
        }
        return out;
    }

    public int getBit(byte[] data, int pos) {
        int posByte = pos / 8;
        int posBit = 7 - pos % 8;
        byte valByte = data[posByte];
        int valInt = valByte >> (8 - (posBit + 1)) & 0x0001;
        return valInt;
    }

    public void setBit(byte[] data, int pos, int val) {
        int posByte = pos / 8;
        int posBit = 7 - pos % 8;
        byte oldByte = data[posByte];
        oldByte = (byte) (((0xFF7F >> posBit) & oldByte) & 0x00FF);
        byte newByte = (byte) ((val << (8 - (posBit + 1))) | oldByte);
        data[posByte] = newByte;
    }

    public char[] toCharArr() {
        char[] res = new char[keyData.length];
        for (int i = 0; i < res.length; i++) {
            res[i] = (char) ((keyData[i] & 0xF0) | (keyData[i] & 0x0F));
        }
        return res;
    }
}
