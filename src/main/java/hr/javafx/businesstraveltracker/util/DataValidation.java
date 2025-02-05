package hr.javafx.businesstraveltracker.util;

/**
 * Klasa za validaciju podataka.
 */
public class DataValidation {

    private DataValidation(){}

    /**
     * Provjerava je li String u formatu koji može biti pretvoren u BigDecimal.
     * @param numberString
     * @return boolean koji predstavlja je li String u formatu koji može biti pretvoren u BigDecimal
     */
    public static boolean isValidDecimalNumber(String numberString) {
        return numberString.matches("\\d+(\\.\\d{1,2})?");
    }

    /**
     * Provjerava ispravnost emaila
     * @param email
     * @return boolean koji predstavlja ispravnost emaila
     */
    public static boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }
}
