package co.nxtgrid.token.domain.meterprimaryaccountnumber;

import co.nxtgrid.token.domain.*;
import co.nxtgrid.token.exceptions.*;
import co.nxtgrid.token.generators.utils.LuhnAlgorithm;
import co.nxtgrid.token.miscellaneous.Strings;

public class MeterPrimaryAccountNumber implements Entity {

    private final String NAME = "MeterPAN";
    private final int LEGACY_IIN = 600727;
    private IssuerIdentificationNumber issuerIdentificationNumber;
    private IndividualAccountIdentificationNumber individualAccountIdentificationNumber;
    private PrimaryAccountNumberCheckDigit primaryAccountNumberCheckDigit;
    private String meterPanValue;
    private int checkDigit;

    public enum Validate {
        NO_METER_PAN_VALIDATION, VALIDATE_METER_PAN
    }

    public MeterPrimaryAccountNumber(String meterPrimaryAccountNumber)
        throws InvalidMeterPrimaryAccountNumberException, InvalidIssuerIdentificationNumberException,
                InvalidManufacturerCodeException, InvalidDecoderSerialNumberException,
                InvalidIssuerIAINComponents, InvalidDrnCheckDigitException, InvalidPanCheckDigitException,
                BitConcatOverflowError, InvalidMeterPANComponentsException, InvalidIAINNumberException,
                InvalidBitStringException {
        this(meterPrimaryAccountNumber, Validate.VALIDATE_METER_PAN);
    }

    public MeterPrimaryAccountNumber(String meterPrimaryAccountNumber, Validate validateMeterPan)
            throws InvalidMeterPrimaryAccountNumberException, InvalidIssuerIdentificationNumberException,
            InvalidManufacturerCodeException, InvalidDecoderSerialNumberException,
            InvalidIssuerIAINComponents, InvalidDrnCheckDigitException, InvalidPanCheckDigitException,
            BitConcatOverflowError, InvalidMeterPANComponentsException, InvalidIAINNumberException,
            InvalidBitStringException {
        setIssuerIdentificationNumber(extractIssuerIdentificationNumber(meterPrimaryAccountNumber));
        setIndividualAccountIdentificationNumber(extractIndividualAccountIdentificationNumber(meterPrimaryAccountNumber, validateMeterPan));
        setPrimaryAccountNumberCheckDigit(extractPrimaryAccountNumberCheckDigit(meterPrimaryAccountNumber));
        generateMeterPanNo();
        if(validateMeterPan == Validate.VALIDATE_METER_PAN && (getCheckDigit() != Integer.parseInt(meterPrimaryAccountNumber.substring(meterPrimaryAccountNumber.length()-1))))
            throw new InvalidMeterPrimaryAccountNumberException(Strings.INVALID_METER_PAN) ;
    }

    public MeterPrimaryAccountNumber(IssuerIdentificationNumber issuerIdentificationNumber,
                                     IndividualAccountIdentificationNumber individualAccountIdentificationNumber)
            throws  BitConcatOverflowError, InvalidMeterPANComponentsException,
            InvalidPanCheckDigitException, InvalidMeterPrimaryAccountNumberException {
        setIssuerIdentificationNumber(issuerIdentificationNumber);
        setIndividualAccountIdentificationNumber(individualAccountIdentificationNumber);
        generateMeterPanNo();
    }

    private IssuerIdentificationNumber extractIssuerIdentificationNumber(String meterPrimaryAccountNumber)
        throws InvalidIssuerIdentificationNumberException {
        if (meterPrimaryAccountNumber.startsWith(String.valueOf(LEGACY_IIN)))
            return new IssuerIdentificationNumber("600727");
        else return new IssuerIdentificationNumber("0000");
    }

    private IndividualAccountIdentificationNumber
        extractIndividualAccountIdentificationNumber(String meterPrimaryAccountNumber)
            throws InvalidManufacturerCodeException, InvalidDecoderSerialNumberException,
                    InvalidIssuerIAINComponents, InvalidDrnCheckDigitException,
                    InvalidIAINNumberException, InvalidBitStringException {
        return extractIndividualAccountIdentificationNumber(meterPrimaryAccountNumber, Validate.VALIDATE_METER_PAN);
    }

    private IndividualAccountIdentificationNumber
        extractIndividualAccountIdentificationNumber(String meterPrimaryAccountNumber, Validate validateMeterPan)
            throws InvalidManufacturerCodeException, InvalidDecoderSerialNumberException,
            InvalidIssuerIAINComponents, InvalidDrnCheckDigitException, InvalidIAINNumberException,
            InvalidBitStringException {
        ManufacturerCode manufacturerCode;
        DecoderSerialNumber decoderSerialNumber;
        IndividualAccountIdentificationNumber iain = null;

        if (meterPrimaryAccountNumber.startsWith(String.valueOf(LEGACY_IIN))) {
            manufacturerCode = new ManufacturerCode(meterPrimaryAccountNumber.substring(6,8));
        } else {
            manufacturerCode = new ManufacturerCode(meterPrimaryAccountNumber.substring(4,8));
        }
        decoderSerialNumber = new DecoderSerialNumber(meterPrimaryAccountNumber.substring(8,16));
        int extractedDrnCheckDigit = Integer.parseInt(meterPrimaryAccountNumber.substring(meterPrimaryAccountNumber.length()-2, meterPrimaryAccountNumber.length()-1));
        if (validateMeterPan == Validate.VALIDATE_METER_PAN) {
            iain = new IndividualAccountIdentificationNumber(manufacturerCode, decoderSerialNumber);
            if (extractedDrnCheckDigit != iain.getDecoderReferenceNumberCheckDigit().getValue())
                throw new InvalidIAINNumberException(Strings.INVALID_IAIN);
        } else if (validateMeterPan == Validate.NO_METER_PAN_VALIDATION){
            iain = new IndividualAccountIdentificationNumber(manufacturerCode, decoderSerialNumber, extractedDrnCheckDigit);
        }
        return iain;
    }

    private PrimaryAccountNumberCheckDigit extractPrimaryAccountNumberCheckDigit(String meterPrimaryAccountNumber)
        throws InvalidPanCheckDigitException {
        return new PrimaryAccountNumberCheckDigit(Integer.parseInt(meterPrimaryAccountNumber.substring(17,18)));
    }

    public String getName() {
        return NAME;
    }

    public IssuerIdentificationNumber getIssuerIdentificationNumber() {
        return issuerIdentificationNumber;
    }

    public void setIssuerIdentificationNumber(IssuerIdentificationNumber issuerIdentificationNumber) {
        this.issuerIdentificationNumber = issuerIdentificationNumber;
    }

    public IndividualAccountIdentificationNumber getIndividualAccountIdentificationNumber() {
        return individualAccountIdentificationNumber;
    }

    public void setIndividualAccountIdentificationNumber(IndividualAccountIdentificationNumber individualAccountIdentificationNumber) {
        this.individualAccountIdentificationNumber = individualAccountIdentificationNumber;
    }

    public PrimaryAccountNumberCheckDigit getPrimaryAccountNumberCheckDigit() {
        return primaryAccountNumberCheckDigit;
    }

    public void setPrimaryAccountNumberCheckDigit(PrimaryAccountNumberCheckDigit primaryAccountNumberCheckDigit) {
        this.primaryAccountNumberCheckDigit = primaryAccountNumberCheckDigit;
    }

    public int getCheckDigit() {
        return checkDigit;
    }

    private void setCheckDigit(int checkDigit) {
        this.checkDigit = checkDigit;
    }

    public String getMeterPanValue() {
        return meterPanValue;
    }

    public void setMeterPanValue(String meterPanValue)
        throws InvalidMeterPrimaryAccountNumberException {
        if (!meterPanValue.matches("[0-9]{18}"))
            throw new InvalidMeterPrimaryAccountNumberException(Strings.INVALID_METER_PAN) ;
        this.meterPanValue = meterPanValue;
    }

    private void generateMeterPanNo() throws BitConcatOverflowError, InvalidMeterPANComponentsException,
                                                    InvalidPanCheckDigitException, InvalidMeterPrimaryAccountNumberException {
        if (!((issuerIdentificationNumber.getValue().matches("[0-9]{4}") && individualAccountIdentificationNumber.getValue().matches("[0-9]{13}")) ||
                (issuerIdentificationNumber.getValue().matches("[0-9]{6}") && individualAccountIdentificationNumber.getValue().matches("[0-9]{11}"))))
            throw new InvalidMeterPANComponentsException(Strings.METER_COMPONENT_LENGTHS_INVALID) ;

        if (individualAccountIdentificationNumber.getValue().matches("[0-9]{13}") && !issuerIdentificationNumber.getValue().equals("0000"))
            throw new InvalidMeterPANComponentsException(Strings.INVALID_IIN_ZEROS);

        String combinedIainIinBitString =   issuerIdentificationNumber.getValue() +
                                            individualAccountIdentificationNumber.getValue();
        long iainIinBitString = Long.parseLong(combinedIainIinBitString) ;
        int checkDigit = (int) LuhnAlgorithm.generateCheckDigit(iainIinBitString);
        setCheckDigit(checkDigit);
        primaryAccountNumberCheckDigit = new PrimaryAccountNumberCheckDigit(checkDigit);
        setMeterPanValue(combinedIainIinBitString + Long.toString(checkDigit));
    }
}
