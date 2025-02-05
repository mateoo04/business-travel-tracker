package hr.javafx.businesstraveltracker.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Klasa za formatiranje datuma i vremena.
 */
public class CustomDateTimeFormatter {

    private CustomDateTimeFormatter() {}

    /**
     * Formatira datum.
     * @param date datum
     * @return String sa datum u standardom europskom formatu
     */
    public static String formatDate(LocalDate date) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d.M.yyyy.");
        return date.format(dateFormatter);
    }

    /**
     * Formatira datum i vrijeme.
     * @param date datum
     * @return String koji sadržava vrijeme i datum formatiran prema europskim standardima odvojene zarezom
     */
    public static String formatDateTime(LocalDateTime date) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("HH:mm, d.M.yyyy.");
        return date.format(dateFormatter);
    }
}
