package co.nxtgrid.token.domain;

import org.joda.time.DateTime;

public enum BaseDate implements Entity {
    _1993("93", new DateTime(1993, 1, 1, 0, 0, 0)),
    _2014("14", new DateTime(2014, 1, 1, 0, 0, 0)),
    _2035("35", new DateTime(2035, 1, 1, 0, 0, 0)); // '+1' added for JodaTime

    public DateTime dateTime;
    public String shortCode;
    private final String NAME = "BaseDate";

    BaseDate(String shortCode, DateTime dateTime) {
        this.shortCode = shortCode;
        this.dateTime = dateTime;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
