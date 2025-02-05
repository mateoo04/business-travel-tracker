package hr.javafx.businesstraveltracker.model;

import hr.javafx.businesstraveltracker.controller.LogInController;
import hr.javafx.businesstraveltracker.enums.ChangeLogType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Predstavlja bilješku promjene.
 * @param <T> klasa entiteta koji je promijenjen
 */
public record ChangeLog<T extends Entity>(
        T previousValue,
        T logValue,
        User user,
        LocalDateTime dateTime,
        ChangeLogType type
) implements Serializable {

    /**
     * Konstruktor za objekt koji sadrži bilješku modifikacije entiteta.
     * @param previousValue prošla vrijednost
     * @param logValue nova vrijednost
     */
    public ChangeLog(T previousValue, T logValue) {
        this(previousValue, logValue, LogInController.getCurrentUser(), LocalDateTime.now(), ChangeLogType.MODIFICATION);
    }

    /**
     * Konstruktor koji predstavlja bilješku stvaranja novog entiteta ili brisanja postojećeg.
     * @param logValue vrijednost entiteta
     * @param type vrsta promjene
     */
    public ChangeLog(T logValue, ChangeLogType type) {
        this(null, logValue, LogInController.getCurrentUser(), LocalDateTime.now(), type);
    }
}
