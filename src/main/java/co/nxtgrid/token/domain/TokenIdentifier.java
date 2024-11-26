package co.nxtgrid.token.domain;

import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.exceptions.InvalidDateTimeBitsException;
import co.nxtgrid.token.miscellaneous.Strings;
import org.joda.time.DateTime;
import org.joda.time.Minutes;

public class TokenIdentifier implements Entity {

    // here
    private final String NAME = "TokenIdentifier";
    private BitString tidBitString = new BitString();
    private DateTime timeOfIssue = new DateTime();
    private DateTime refDateTime;
    private final int NO_OF_BITS = 24;

    public TokenIdentifier(BaseDate baseDate) {
        setBaseDate(baseDate);
        setTimeOfIssue(DateTime.now());
    }

    public TokenIdentifier(DateTime timeOfIssue, BaseDate baseDate) {
        setBaseDate(baseDate);
        setTimeOfIssue(timeOfIssue);
    }

    public String getName() {
        return NAME;
    }

    public BitString getBitString() {
        return tidBitString;
    }

    public DateTime getTimeOfIssue() {
        return timeOfIssue;
    }

    public void setTimeOfIssue(DateTime timeOfIssue) {
        this.timeOfIssue = timeOfIssue;
        generateTID();
    }

    public String toString() {
        return getBitString().toString();
    }

    private void generateTID() {
        long differenceMinutes = getDifferenceFromBaseTimeInMinutes();
        if (timeOfIssue.getMinuteOfDay() == 1)
                differenceMinutes = getDifferenceFromBaseTimeInMinutes() + 1;
        tidBitString = new BitString(differenceMinutes);
        tidBitString.setLength(NO_OF_BITS);
    }

    public int getDifferenceFromBaseTimeInMinutes() {
        DateTime convertedDateOfIssue = timeOfIssue;
        Minutes difference = Minutes.minutesBetween(refDateTime, convertedDateOfIssue);
        return difference.getMinutes();
    }

    public DateTime getRefBaseTime() {
        return refDateTime;
    }

    public void setBaseDate(BaseDate baseDate) {
        refDateTime = baseDate.dateTime;
    }

    public DateTime getDateTimeOfIssue(BitString tidBitString)
            throws InvalidDateTimeBitsException {
        DateTime obtainedTime;
        if (tidBitString.getLength() == NO_OF_BITS) {
            int timeInMinutes = (int) tidBitString.getValue();
            obtainedTime = getRefBaseTime().plusMinutes(timeInMinutes);
        } else
            throw new InvalidDateTimeBitsException(Strings.BIT_STRING_SIZE_ERROR);

        return obtainedTime;
    }

    public String bitsToString() {
        return String.format("%" + NO_OF_BITS + "s", Long.toBinaryString(tidBitString.getValue())).replace(' ', '0');
    }
}

