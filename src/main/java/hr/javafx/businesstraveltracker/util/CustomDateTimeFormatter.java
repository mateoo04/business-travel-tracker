package hr.javafx.businesstraveltracker.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomDateTimeFormatter {

    private CustomDateTimeFormatter() {}

    public static String formatDate(LocalDate date) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d.M.yyyy");
        return date.format(dateFormatter);
    }

    public static String formatDateTime(LocalDateTime date) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d.M.yyyy H:m");
        return date.format(dateFormatter);
    }
}
