package hr.javafx.businesstraveltracker.exception;

/**
 * Iznimka koja se baca u slučaju pogreške tijekom pokušaja dohvaćanja podataka od API-a.
 */
public class ApiRequestException extends RuntimeException {
    public ApiRequestException() {
    }

    public ApiRequestException(String message) {
        super(message);
    }

    public ApiRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiRequestException(Throwable cause) {
        super(cause);
    }

    public ApiRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
