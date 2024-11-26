package co.nxtgrid;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DateTimeConverter implements Converter<String, DateTime> {

    @Override
    public DateTime convert(String source) {
        // Parse ISO 8601 date string into Joda-Time DateTime
        return ISODateTimeFormat.dateTimeParser().parseDateTime(source);
    }
}
