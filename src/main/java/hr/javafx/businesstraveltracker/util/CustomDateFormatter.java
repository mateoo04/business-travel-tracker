package hr.javafx.businesstraveltracker.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CustomDateFormatter {

    private CustomDateFormatter() {}

    public static String formatDate(LocalDate date) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d.M.yyyy");
        return date.format(dateFormatter);
    }
}
