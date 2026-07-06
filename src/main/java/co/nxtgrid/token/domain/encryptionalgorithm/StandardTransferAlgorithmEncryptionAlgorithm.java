package co.nxtgrid.token.domain.encryptionalgorithm;

import co.nxtgrid.token.domain.base.Bit;
import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.base.Nibble;
import co.nxtgrid.token.domain.keys.decoder.DecoderKey;
import co.nxtgrid.token.domain.table.Table;
import co.nxtgrid.token.domain.table.permutationtable.DecryptingPermutationTable;
import co.nxtgrid.token.domain.table.permutationtable.EncryptingPermutationTable;
import co.nxtgrid.token.domain.table.substitutiontable.DecryptingFirstSubstitutionTable;
import co.nxtgrid.token.domain.table.substitutiontable.DecryptingSecondSubstitutionTable;
import co.nxtgrid.token.domain.table.substitutiontable.EncryptingFirstSubstitutionTable;
import co.nxtgrid.token.domain.table.substitutiontable.EncryptingSecondSubstitutionTable;
import co.nxtgrid.token.exceptions.*;
import co.nxtgrid.token.miscellaneous.Strings;

public class StandardTransferAlgorithmEncryptionAlgorithm extends EncryptionAlgorithm {

    private Table firstSubstitutionTable = new EncryptingFirstSubstitutionTable();
    private Table secondSubstitutionTable = new EncryptingSecondSubstitutionTable();
    private DecoderKey decoderKey;

    public StandardTransferAlgorithmEncryptionAlgorithm() {
        super(Code.STA);
    }

    public DecoderKey getDecoderKey() {
        return decoderKey;
    }

    public void setDecoderKey(DecoderKey decoderKey) {
        this.decoderKey = decoderKey;
    }

    public BitString encrypt(DecoderKey decoderKey, BitString _64BitDataBlock)
        throws InvalidRangeException, InvalidNibbleBitStringException,
            NibbleOutOfRangeException, InvalidBitStringException {
        setDecoderKey(decoderKey);
        byte[] decoderKeyProcessed = processDecoderKey(decoderKey);
        return get64BitEncryptedDataBlock(decoderKeyProcessed, _64BitDataBlock);
    }

    public BitString decrypt(DecoderKey decoderKey, BitString _64BitDataBlock)
        throws InvalidBitStringException  {
        setDecoderKey(decoderKey);
        return decrypt64BitDataBlock(decoderKey.getKeyData(), _64BitDataBlock);
    }

    private byte[] processDecoderKey(DecoderKey decoderKey) {
        byte[] ec = decoderKey.getKeyData();
        byte[] complemented = decoderKey.complement(ec);
        return decoderKey.rotateComplemented(complemented);
    }

    public BitString get64BitEncryptedDataBlock(byte[] key, BitString _64bitDataBlock)
            throws InvalidNibbleBitStringException, NibbleOutOfRangeException,
            InvalidRangeException, InvalidBitStringException {
        BitString substitutedDataBlock;
        BitString permutatedDataBlock;
        int encryptionLoopsCounter = 0;
        final int NO_OF_LOOPS = 16;
        final int ONE_STEP = 1;

        while (encryptionLoopsCounter < NO_OF_LOOPS) {
            substitutedDataBlock = encryptingSubstitute(key, _64bitDataBlock);
            EncryptingPermutationTable encryptingPermutationTable = new EncryptingPermutationTable();
            permutatedDataBlock = permutate(substitutedDataBlock, encryptingPermutationTable);
            key = decoderKey.rotateLeft(key, ONE_STEP);
            _64bitDataBlock = permutatedDataBlock;
            encryptionLoopsCounter++;
        }
        return _64bitDataBlock;
    }

    private BitString permutate(BitString substitutionDataBlock, Table permutationTable)
            throws InvalidBitStringException {
        BitString permutatedDataBlock = new BitString(64);
        if (substitutionDataBlock.getLength() == 64) {
            for (int bitsCounter = 0; bitsCounter < substitutionDataBlock.getLength(); bitsCounter++) {
                int replacementBitPosition = permutationTable.getValue(bitsCounter);
                Bit replacementBit = substitutionDataBlock.getBit(bitsCounter);
                permutatedDataBlock.setBit(replacementBitPosition, replacementBit.getValue());
            }
        } else
            throw new InvalidBitStringException(Strings.INVALID_SUBSTITUTION_DATA_BLOCK);

        return permutatedDataBlock;
    }

    private BitString encryptingSubstitute(byte[] key, BitString _64bitDataBlock)
            throws InvalidNibbleBitStringException, NibbleOutOfRangeException, InvalidRangeException {
        int nibbleCounter = 0;
        final int SIZE_OF_NIBBLE = 4;
        final int NO_OF_NIBBLES = 16;
        Nibble substitutionValue = new Nibble();
        Nibble currDataNibbleBitString;

        while (nibbleCounter < NO_OF_NIBBLES) {

            int checkIndex = nibbleCounter * SIZE_OF_NIBBLE + 3;
            int mostSignificantBitNibbleValue = decoderKey.getBit(key, checkIndex);

            currDataNibbleBitString = _64bitDataBlock.getNibble(nibbleCounter);
            int desiredTableIndexValue = (int) currDataNibbleBitString.getNibble().getValue();

            if (mostSignificantBitNibbleValue == 0) {
                substitutionValue = getFromSubstitutionTable(desiredTableIndexValue, firstSubstitutionTable);
            } else if (mostSignificantBitNibbleValue == 1) {
                substitutionValue = getFromSubstitutionTable(desiredTableIndexValue, secondSubstitutionTable);
            }

            _64bitDataBlock = substituteDataBits(_64bitDataBlock, nibbleCounter, substitutionValue);
            nibbleCounter++;
        }
        return _64bitDataBlock;
    }

    private Nibble getFromSubstitutionTable(int nibblePosition, Table table) {
        try {
            int substitutionValue = table.getValue(nibblePosition);
            BitString extractedNibbleBitString = new BitString(substitutionValue);
            extractedNibbleBitString.setLength(4);
            return new Nibble(extractedNibbleBitString);
        } catch (InvalidNibbleBitStringException e) {
            e.printStackTrace();
        }
        return new Nibble();
    }

    private BitString substituteDataBits(BitString dataBlock, int nibblePosition, Nibble substitutionValue)
            throws NibbleOutOfRangeException, InvalidRangeException {
        dataBlock.setNibble(nibblePosition, substitutionValue);
        return dataBlock;
    }

    protected BitString decrypt64BitDataBlock(byte[] key, BitString _64bitDataBlock)
        throws InvalidBitStringException {
        BitString substitutedDataBlock ;
        BitString permutatedDataBlock ;
        int decryptionLoopsCounter = 0;
        final int NO_OF_LOOPS = 16 ;
        final int ONE_STEP = 1 ;

        while (decryptionLoopsCounter < NO_OF_LOOPS) {
            DecryptingPermutationTable decryptingPermutationTable = new DecryptingPermutationTable() ;
            permutatedDataBlock = permutate(_64bitDataBlock, decryptingPermutationTable);
            substitutedDataBlock = decryptingSubstitute(key, permutatedDataBlock) ;
            key = decoderKey.rotateRight(key, ONE_STEP) ;
            _64bitDataBlock = substitutedDataBlock;
            decryptionLoopsCounter++ ;
        }
        return _64bitDataBlock ;
    }

    protected BitString decryptingSubstitute (byte[] key, BitString _64bitDataBlock) {
        int nibbleCounter = 0 ;
        final int SIZE_OF_NIBBLE = 4 ;
        final int NO_OF_NIBBLES = 16 ;
        Nibble substitutionValue = new Nibble() ;
        Nibble currDataNibbleBitString ;
        Table firstTable = new DecryptingFirstSubstitutionTable()  ;
        Table secondTable = new DecryptingSecondSubstitutionTable() ;

        while (nibbleCounter < NO_OF_NIBBLES) {
            try {
                int checkIndex = nibbleCounter * SIZE_OF_NIBBLE;
                int mostSignificantBitNibbleValue = decoderKey.getBit(key, checkIndex);

                currDataNibbleBitString = _64bitDataBlock.getNibble(nibbleCounter);
                int desiredTableIndexValue = (int) currDataNibbleBitString.getNibble().getValue();

                if (mostSignificantBitNibbleValue == 0)
                    substitutionValue = getFromSubstitutionTable(desiredTableIndexValue, firstTable);
                else if (mostSignificantBitNibbleValue == 1)
                    substitutionValue = getFromSubstitutionTable(desiredTableIndexValue, secondTable);

                _64bitDataBlock = substituteDataBits(_64bitDataBlock, nibbleCounter, substitutionValue);
                nibbleCounter++;

            } catch (InvalidNibbleBitStringException | NibbleOutOfRangeException | InvalidRangeException e) {
                e.printStackTrace();
            }
        }
        return _64bitDataBlock ;
    }
}
