package co.nxtgrid.ca.keys.symmetric;

public class DESCipher extends SymmetricCipher {

    public DESCipher() {
        final int NO_OF_BYTES = 16 ;
        setKeyType(KeyType.AES);
        generate(NO_OF_BYTES);
    }

    public DESCipher(byte[] keyBytes) {
        setKeyBytes(keyBytes);
        setKeyType(KeyType.DES);
        generate(keyBytes) ;
    }

    public DESCipher(byte[] keyBytes, OperatingMode operatingMode) {
        setKeyBytes(keyBytes);
        setKeyType(KeyType.DES);
        setOperatingMode(operatingMode);
        generate(keyBytes) ;
    }

    public DESCipher(byte[] keyBytes, OperatingMode operatingMode, PaddingType paddingType) {
        setKeyBytes(keyBytes);
        setKeyType(KeyType.DES);
        setOperatingMode(operatingMode);
        setPaddingType(paddingType);
        generate(keyBytes);
    }
}