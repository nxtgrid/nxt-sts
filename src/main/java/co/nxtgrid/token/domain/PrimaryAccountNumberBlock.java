package co.nxtgrid.token.domain;

import co.nxtgrid.token.exceptions.InvalidPrimaryAccountNumberBlockComponentsException;
import co.nxtgrid.token.miscellaneous.Strings;

public class PrimaryAccountNumberBlock implements Entity {

    private final String NAME = "Primary Account Number Block";
    private IssuerIdentificationNumber issuerIdentificationNumber;
    private IndividualAccountIdentificationNumber individualAccountIdentificationNumber;
    private KeyType keyType;
    private String generatedPrimaryAccountNumberBlock = null ;

    public PrimaryAccountNumberBlock(IssuerIdentificationNumber issuerIdentificationNumber,
                                     IndividualAccountIdentificationNumber individualAccountIdentificationNumber,
                                     KeyType keyType){
        setIssuerIdentificationNumber(issuerIdentificationNumber);
        setIndividualAccountIdentificationNumber(individualAccountIdentificationNumber);
        setKeyType(keyType);
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

    public String getValue() throws InvalidPrimaryAccountNumberBlockComponentsException {
        if (null == generatedPrimaryAccountNumberBlock)
            generatedPrimaryAccountNumberBlock = generate() ;
        return generatedPrimaryAccountNumberBlock;
    }

    public KeyType getKeyType() {
        return keyType;
    }

    public void setKeyType(KeyType keyType) {
        this.keyType = keyType;
    }

    private String generate()
        throws InvalidPrimaryAccountNumberBlockComponentsException {
        /**
         * NOTE:
         *
         * keyType == 0 should never be used to generate
         * tokens as a meter with this kind of decoder
         * type should never leave the factory. Left this
         * here however until the vendors stop shipping
         * meters with this decoder type!
         *
         */
        if (keyType.getValue() == 0 || keyType.getValue() == 1 || keyType.getValue() == 2) {
            if (issuerIdentificationNumber.getValue().length() == 6)
                return issuerIdentificationNumber.getValue().substring(issuerIdentificationNumber.getValue().length() - 5)
                        + individualAccountIdentificationNumber.getValue().substring(individualAccountIdentificationNumber.getValue().length() - 11);
            else if (issuerIdentificationNumber.getValue().length() == 4)
                return issuerIdentificationNumber.getValue().substring(issuerIdentificationNumber.getValue().length() - 3)
                        + individualAccountIdentificationNumber.getValue().substring(individualAccountIdentificationNumber.getValue().length() - 13);
        } else if (keyType.getValue() == 3) {
            if (issuerIdentificationNumber.getValue().length() == 6)
                return issuerIdentificationNumber.getValue().substring(issuerIdentificationNumber.getValue().length() - 5)
                        + "00000000000";
            else if (issuerIdentificationNumber.getValue().length() == 4)
                return issuerIdentificationNumber.getValue().substring(issuerIdentificationNumber.getValue().length() - 3)
                        +  "0000000000000";
        }
        throw new InvalidPrimaryAccountNumberBlockComponentsException(Strings.INVALID_PRIMARY_ACCOUNT_NUMBER_BLOCK);
    }
}
