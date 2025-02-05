package hr.javafx.businesstraveltracker.exception;

/**
 * Iznimka koja se baca u slučaju neuspješnog upravljanja Scene-om.
 */
public class SceneManagerException extends RuntimeException {
    public SceneManagerException(String message) {
        super(message);
    }

    public SceneManagerException(String message, Throwable cause) {
        super(message, cause);
    }

    public SceneManagerException(Throwable cause) {
        super(cause);
    }

    public SceneManagerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public SceneManagerException() {
    }
}
