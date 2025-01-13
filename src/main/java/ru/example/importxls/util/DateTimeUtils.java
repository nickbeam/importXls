package ru.example.importxls.util;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateTimeUtils {
    private static final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private static final DateTimeFormatter dFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter dFormatterWithDot = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter dtFormatExt = DateTimeFormatter.ofPattern( "E MMM dd HH:mm:ss z uuuu" )
            .withLocale( Locale.US );

    public static String getDateTime(LocalDateTime ldt) {
        return ldt.format(dtFormatter);
    }

    public static String getCurrentDateTime() {
        return getDateTime(LocalDateTime.now());
    }

    public static String getDate(LocalDateTime ldt) {
        return ldt.format(dFormatter);
    }

    public static Date getDate(String str) {
        return Date.valueOf(LocalDate.parse(str, dtFormatExt));
    }

    public static String getCurrentDate() {
        return getDate(LocalDateTime.now());
    }
}
