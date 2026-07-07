package co.nxtgrid.strategy;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.joda.time.DateTime;

final class StrategySupport {

    private StrategySupport() {
    }

    static DateTime toJodaDateTime(LocalDateTime issueDate) {
        return new DateTime(issueDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }
}
