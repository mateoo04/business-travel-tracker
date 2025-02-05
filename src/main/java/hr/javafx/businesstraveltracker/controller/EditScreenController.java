package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.model.Entity;

/**
 * Sučelje kojeg implementiraju klase kontrolera za ekrane kojima se uređuju podaci.
 * @param <T> klasa objekta koji se uređuje
 */
public interface EditScreenController<T extends Entity> {
    void initData(T entity);
}
