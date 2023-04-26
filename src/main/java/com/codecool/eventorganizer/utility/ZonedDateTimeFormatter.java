package com.codecool.eventorganizer.utility;

import java.time.format.DateTimeFormatter;

public abstract class ZonedDateTimeFormatter {
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd - HH:mm:ss z");
}
