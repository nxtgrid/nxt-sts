package co.nxtgrid.token.domain;

import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.exceptions.InvalidRangeException;
import co.nxtgrid.token.generators.utils.Utils;
import co.nxtgrid.token.miscellaneous.Strings;

/**
 *
 * The CRC checksum  is used to verify the integrity of
 * the data transferred and is derived from the generator
 * polynomial as  defined in IEC620055-41 as:
 *
 * <b><i>x^16 + x^15 + x^2 + 1</i></b>
 *
 * It is derived from the previous 50 bits of the co.nxtgridoco.nxtgriden which
 * is initially set to 0xFFFF
 *
 * Steps:
 *
 * (Basic Implementation - Actual implementation uses CRC16-IBM with reversed keys
 * i.e. 0xA001 rather than 0x8005
 *
 *      1. First encoder a polynomial keys
 *      1100 0000 0000 00101
 *
 *      2. Append sets of 0's to polynomial keys with length polynomial keys - 1 to data
 *      e.g. if data is 1100 1011 1001
 *
 *      then data appended becomes
 *      1100 0000 0000 00101 1100 1011 1001
 *
 *      3. Perform long division on the concatenated value, the reminder is
 *      the domain.crc.Crc transmitted with the data
 *      e.g.
 *                      +-------------------------------------
 *      1100 1011 1001  | 1100 0000 0000 00101 1100 1011 1001
 */
public class Crc implements Entity {

    private final String NAME = "Crc";
    private BitString crcBitString = new BitString();
    private final int NO_OF_BITS = 16 ;

    // Forms of expressing the CRC Polynomial used
    // CRC_POLYNOMIAL as X^16+x^15+x^2+1
    // private final long CRC_POLYNOMIAL = 0x18005 ;
    // private final String CRC_POLYNOMIAL = "1 1000 0000 0000 0101" ; // note the reversed version is used in the standard

    public Crc() {}

    public Crc(BitString crcBitString)
            throws InvalidRangeException {
        setCrcBitString(crcBitString) ;
    }

    public BitString getBitString() {
        return crcBitString ;
    }

    public void setCrcBitString(BitString crcBitString)
            throws InvalidRangeException {
        if (crcBitString.getLength() == NO_OF_BITS)
            this.crcBitString = crcBitString ;
        else
            throw new InvalidRangeException(Strings.BIT_STRING_SIZE_ERROR) ;
    }

    public String getName() {
        return NAME;
    }

    public BitString generateCRC(BitString initialBitString) {
        BitString generatedCrc = new BitString() ;
        if (null != initialBitString) {
            byte[] convertedBitStringToBytes = Utils.longToBytes(initialBitString.getValue()) ;
            long crc = generateCRC(convertedBitStringToBytes) ;
            generatedCrc = new BitString(crc) ;
            generatedCrc.setLength(16);
        }
        return generatedCrc;
    }

    public int generateCRC(byte[] bitStringBytes) {
        int crc = 0xFFFF;
        int val = 0;
        int len = bitStringBytes.length ;

        for (int pos = 0; pos < len; pos++) {
            crc ^= (0x00ff & bitStringBytes[pos]);

            for (int i = 8; i != 0; i--) {
                if ((crc & 0x0001) != 0) {
                    crc >>= 1;
                    crc ^= 0xA001;
                } else
                    crc >>= 1;
            }
        }
        val = (crc & 0xff) << 8;
        val = val + ((crc >> 8) & 0xff);
        return val;

    }

    public String bitsToString() {
        return String.format("%" + NO_OF_BITS + "s", Long.toBinaryString(crcBitString.getValue())).replace(' ', '0');
    }
}
