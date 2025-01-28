package hr.javafx.businesstraveltracker.exception;

public class UserDataRepositoryAccessException extends RuntimeException {
    public UserDataRepositoryAccessException() {
    }

    public UserDataRepositoryAccessException(String message) {
        super(message);
    }

    public UserDataRepositoryAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserDataRepositoryAccessException(Throwable cause) {
        super(cause);
    }

    public UserDataRepositoryAccessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
