package co.nxtgrid.token.domain;

import co.nxtgrid.token.exceptions.InvalidDecoderSerialNumberException;
import co.nxtgrid.token.miscellaneous.Strings;

public class DecoderSerialNumber implements Entity {

    private String decoderSerialNumber;
    private final String NAME = "DecoderSerialNumber";

    public DecoderSerialNumber(String decoderSerialNumber)
        throws InvalidDecoderSerialNumberException {
        setDecoderSerialNumber(decoderSerialNumber);
    }

    public String getValue() {
        return decoderSerialNumber;
    }

    public void setDecoderSerialNumber(String decoderSerialNumber)
        throws InvalidDecoderSerialNumberException {

        if (!decoderSerialNumber.matches("[0-9]{8}"))
            throw new InvalidDecoderSerialNumberException(Strings.INVALID_DECODER_SERIAL_NUMBER);

        this.decoderSerialNumber = decoderSerialNumber;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
