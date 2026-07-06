package co.nxtgrid.token.domain;

import co.nxtgrid.token.exceptions.InvalidDecoderSerialNumberException;
import co.nxtgrid.token.exceptions.InvalidDrnCheckDigitException;
import co.nxtgrid.token.exceptions.InvalidIssuerIAINComponents;
import co.nxtgrid.token.exceptions.InvalidManufacturerCodeException;
import co.nxtgrid.token.generators.utils.LuhnAlgorithm;
import co.nxtgrid.token.miscellaneous.Strings;
import co.nxtgrid.token.exceptions.InvalidIndividualAccountIdentificationNumber;

public class IndividualAccountIdentificationNumber implements Entity {

    private ManufacturerCode manufacturerCode;
    private DecoderSerialNumber decoderSerialNumber;
    private DecoderReferenceNumberCheckDigit decoderReferenceNumberCheckDigit;
    private final String NAME = "Individual Account Identification Number";
    private String iain = "0";

    public IndividualAccountIdentificationNumber(String meterNo)
        throws InvalidIndividualAccountIdentificationNumber,
            InvalidManufacturerCodeException, InvalidDecoderSerialNumberException,
            InvalidDrnCheckDigitException, InvalidIssuerIAINComponents {
        if (!meterNo.matches("[0-9]{11}") &&
            !meterNo.matches("[0-9]{13}")) {
            throw new
                    InvalidIndividualAccountIdentificationNumber(String
                    .format("%s is an invalid meter number", meterNo));
        }
        if (meterNo.matches("[0-9]{11}")) {
            setManufacturerCode(new ManufacturerCode(meterNo.substring(0, 2)));
            setDecoderSerialNumber(new DecoderSerialNumber(meterNo.substring(2, 10)));
            decoderReferenceNumberCheckDigit = new DecoderReferenceNumberCheckDigit(
                    Long.parseLong(String.valueOf(meterNo.charAt(10))));
        } else if (meterNo.matches("[0-9]{13}")) {
            setManufacturerCode(new ManufacturerCode(meterNo.substring(0, 4)));
            setDecoderSerialNumber(new DecoderSerialNumber(meterNo.substring(4, 12)));
            decoderReferenceNumberCheckDigit = new DecoderReferenceNumberCheckDigit(
                    Long.parseLong(String.valueOf(meterNo.charAt(12))));
        }
        generateIndividualAccountIdentificationNumber();
    }

    public IndividualAccountIdentificationNumber(ManufacturerCode manufacturerCode,
                                                 DecoderSerialNumber decoderSerialNumber)
        throws InvalidIssuerIAINComponents, InvalidDrnCheckDigitException {
        setManufacturerCode(manufacturerCode);
        setDecoderSerialNumber(decoderSerialNumber);
        generateDrnCheckDigit();
        generateIndividualAccountIdentificationNumber() ;
    }

    public IndividualAccountIdentificationNumber(ManufacturerCode manufacturerCode,
                                                 DecoderSerialNumber decoderSerialNumber,
                                                 long drnCheckDigit)
            throws InvalidDrnCheckDigitException {
        setManufacturerCode(manufacturerCode);
        setDecoderSerialNumber(decoderSerialNumber);
        setDecoderReferenceNumberCheckDigit(drnCheckDigit);
        generateIndividualAccountIdentificationNumber() ;
    }

    public String getName() {
        return NAME ;
    }

    public String getValue() {
        return iain;
    }

    public ManufacturerCode getManufacturerCode() {
        return manufacturerCode;
    }

    public void setManufacturerCode(ManufacturerCode manufacturerCode) {
        this.manufacturerCode = manufacturerCode;
    }

    public DecoderSerialNumber getDecoderSerialNumber() {
        return decoderSerialNumber;
    }

    public void setDecoderSerialNumber(DecoderSerialNumber decoderSerialNumber) {
        this.decoderSerialNumber = decoderSerialNumber;
    }

    public DecoderReferenceNumberCheckDigit getDecoderReferenceNumberCheckDigit() {
        return decoderReferenceNumberCheckDigit;
    }

    public void setDecoderReferenceNumberCheckDigit(long drnCheckDigit)
        throws InvalidDrnCheckDigitException {
        decoderReferenceNumberCheckDigit = new DecoderReferenceNumberCheckDigit(drnCheckDigit);
    }

    private void generateDrnCheckDigit()
            throws InvalidIssuerIAINComponents, InvalidDrnCheckDigitException {
        if (!(manufacturerCode.getValue().matches("[0-9]{2}") || manufacturerCode.getValue().matches("[0-9]{4}"))
                && (decoderSerialNumber.getValue().matches("[0-9]{8}")))
            throw new InvalidIssuerIAINComponents(Strings.INVALID_IAIN_COMPONENTS) ;

        long combinedComponentsValues = Long.parseLong(manufacturerCode.getValue() + (decoderSerialNumber.getValue())) ;
        long generatedDrnCheckDigit = LuhnAlgorithm.generateCheckDigit(combinedComponentsValues);
        decoderReferenceNumberCheckDigit = new DecoderReferenceNumberCheckDigit(generatedDrnCheckDigit) ;
    }

    private void generateIndividualAccountIdentificationNumber() {
        iain = manufacturerCode.getValue() + decoderSerialNumber.getValue() + decoderReferenceNumberCheckDigit.getValue();
    }
}
