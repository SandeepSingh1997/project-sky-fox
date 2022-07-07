package com.booking.utilities.serializers.time;

import org.springframework.stereotype.Component;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Locale;

@Component
public class TimeStringConverter {

    public String convert(Time time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
        return simpleDateFormat.format(time).toUpperCase();
    }
}
