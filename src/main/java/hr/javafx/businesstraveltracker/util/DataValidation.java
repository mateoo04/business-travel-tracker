package hr.javafx.businesstraveltracker.util;

public class DataValidation {
    public static boolean isValidDecimalNumber(String numberString) {
        if(numberString.matches("\\d+(\\.\\d{1,2})?")) return true;
        return false;
    }
}
