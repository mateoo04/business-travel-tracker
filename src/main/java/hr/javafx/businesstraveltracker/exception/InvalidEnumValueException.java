package hr.javafx.businesstraveltracker.exception;

public class InvalidEnumValueException extends Exception {
    public InvalidEnumValueException() {
    }

    public InvalidEnumValueException(String message) {
        super(message);
    }

    public InvalidEnumValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidEnumValueException(Throwable cause) {
        super(cause);
    }

    public InvalidEnumValueException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
