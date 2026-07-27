package co.nxtgrid.strategy;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

final class StrategySupport {

    private StrategySupport() {
    }

    /**
     * Correlation id for the in-memory token/generator objects (legacy domain field).
     * Not part of the STS crypto or the HTTP response.
     */
    static String newRequestId() {
        return UUID.randomUUID().toString();
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
