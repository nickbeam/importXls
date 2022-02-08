package ru.vitasoft.importxls.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public static String getDateTimeFormatted(LocalDateTime ldt) {
        return ldt.format(formatter);
    }

    public static String getDateTimeFormatted() {
        return getDateTimeFormatted(LocalDateTime.now());
    }
}
