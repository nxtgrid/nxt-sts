package co.nxtgrid.token.domain;

import co.nxtgrid.token.exceptions.InvalidDateOtExpiryException;
import co.nxtgrid.token.miscellaneous.Strings;

public class DateOfExpiry implements Entity {

    // From 2000 onwards
    private String dateOfExpiry = "0000";
    private final String NAME = "Date Of Expiry";

    public DateOfExpiry(String bitString)
        throws InvalidDateOtExpiryException {
        setValue(bitString);
    }

    public String getValue() {
        return dateOfExpiry;
    }

    public void setValue(String dateOfExpiry)
        throws InvalidDateOtExpiryException {
        boolean a = dateOfExpiry.matches("[0-9]{4}");
        long convertedValue = Long.parseLong(dateOfExpiry.substring(2,4));
        boolean b = Long.parseLong(dateOfExpiry.substring(2,4)) < 12 ;
        if(dateOfExpiry.matches("[0-9]{4}") && Long.parseLong(dateOfExpiry.substring(2,4)) < 12)
            this.dateOfExpiry = dateOfExpiry;
        else
            throw new InvalidDateOtExpiryException(Strings.INVALID_DATE_OF_EXPIRY_EXCEPTION);
    }

    public String getName() {
        return NAME;
    }
}
