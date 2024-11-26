package co.nxtgrid.ca.keys.symmetric;

public class DesedeCipher extends SymmetricCipher  {

    public DesedeCipher() {
        final int NO_OF_BYTES = 112 ;
        setKeyType(KeyType.DESede);
        generate(NO_OF_BYTES) ;
    }

    public DesedeCipher(byte[] keyBytes) {
        setKeyBytes(keyBytes);
        setKeyType(KeyType.DESede);
        generate(keyBytes) ;
    }

    public DesedeCipher(byte[] keyBytes, OperatingMode operatingMode) {
        setKeyBytes(keyBytes);
        setKeyType(KeyType.DESede);
        setOperatingMode(operatingMode);
        generate(keyBytes) ;
    }

    public DesedeCipher(byte[] keyBytes, OperatingMode operatingMode, PaddingType paddingType) {
        setKeyBytes(keyBytes);
        setKeyType(KeyType.DESede);
        setOperatingMode(operatingMode);
        setPaddingType(paddingType);
        generate(keyBytes) ;
    }
}
