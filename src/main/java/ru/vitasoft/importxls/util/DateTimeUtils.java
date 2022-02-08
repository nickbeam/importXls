package ru.vitasoft.importxls.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {
    private static final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private static final DateTimeFormatter dFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static String getDateTime(LocalDateTime ldt) {
        return ldt.format(dtFormatter);
    }

    public static String getCurrentDateTime() {
        return getDateTime(LocalDateTime.now());
    }

    public static String getDate(LocalDateTime ldt) {
        return ldt.format(dFormatter);
    }

    public static String getCurrentDate() {
        return getDate(LocalDateTime.now());
    }
}
