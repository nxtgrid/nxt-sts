package co.nxtgrid.strategy;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

final class StrategySupport {

    private StrategySupport() {
    }

    static DateTime toJodaDateTime(LocalDateTime issueDate) {
        return new DateTime(
            issueDate.getYear(),
            issueDate.getMonthValue(),
            issueDate.getDayOfMonth(),
            issueDate.getHour(),
            issueDate.getMinute(),
            issueDate.getSecond(),
            issueDate.get(ChronoField.MILLI_OF_SECOND),
            DateTimeZone.UTC
        );
    }
}
