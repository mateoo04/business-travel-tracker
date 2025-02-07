package hr.javafx.businesstraveltracker.repository;

import hr.javafx.businesstraveltracker.model.Entity;

import java.util.List;
import java.util.Optional;

/**
 * Sučelje za repozitorije koje sadrži metode za spremanje, čitanje, ažuriranje i brisanje podataka iz baze podataka.
 * @param <T>
 */
public interface CrudRepository<T extends Entity> {
    /**
     * Pronalazi i vraća sve zapise u bazi podataka.
     * @return sve zapise u bazi podataka
     */
    List<T> findAll();

    /**
     * Pronalazi podatak s odgovarajućim ID-jem.
     * @param id ID zapisa
     * @return Optional koji sadržava podatak sa zadanim ID-jem ako je pronađen
     */
    Optional<T> findById(Long id);

    /**
     * Sprema podatak u bazu podataka.
     * @param entity
     */
    void save(T entity);

    /**
     * Ažurira podatak u bazi podataka.
     * @param entity
     */
    void update(T entity);

    /**
     * Briše zapis u bazi podataka prema zadanim ID-ju.
     * @param id
     */
    void deleteById(Long id);
}
