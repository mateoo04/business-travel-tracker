package hr.javafx.businesstraveltracker.repository;

import hr.javafx.businesstraveltracker.model.Employee;
import hr.javafx.businesstraveltracker.model.Entity;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T extends Entity> {
    List<T> findAll();
    Optional<T> findById(Long id);
    void save(T entity);
}
