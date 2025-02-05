package hr.javafx.businesstraveltracker.model;

import hr.javafx.businesstraveltracker.controller.LogInController;
import hr.javafx.businesstraveltracker.enums.ChangeLogType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Predstavlja bilješku promjene.
 * @param <T> klasa entiteta koji je promijenjen
 */
public class ChangeLog<T extends Entity> implements Serializable {
    private T previousValue;
    private T value;
    private User user;
    private LocalDateTime dateTime;
    private ChangeLogType type;

    /**
     * Konstruktor za objekt koji sadrži bilješku modifikacije entiteta.
     * @param previousValue prošla vrijednost
     * @param value nova vrijednost
     */
    public ChangeLog(T previousValue, T value){
        this.previousValue = previousValue;
        this.value = value;
        this.user = LogInController.getCurrentUser();
        this.dateTime = LocalDateTime.now();
        this.type = ChangeLogType.MODIFICATION;
    }

    /**
     * Konstruktor koji predstavlja bilješku stvaranje novog entiteta ili brisanje postojećeg
     * @param value vrijednost entiteta
     * @param type vrsta promjene
     */
    public ChangeLog(T value, ChangeLogType type){
        this.value = value;
        this.user = LogInController.getCurrentUser();
        this.dateTime = LocalDateTime.now();
        this.type = type;
    }

    public T getPreviousValue() {
        return previousValue;
    }

    public T getLogValue() {
        return value;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public ChangeLogType getType() {
        return type;
    }
}
