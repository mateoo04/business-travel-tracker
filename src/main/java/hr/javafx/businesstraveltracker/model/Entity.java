package hr.javafx.businesstraveltracker.model;

import java.io.Serializable;

/**
 * Predstavlja entitet
 */
public abstract class Entity implements Serializable {

    private Long id;

    /**
     * Konstruktor entiteta s definiranim ID-jem.
     * @param id
     */
    protected Entity(Long id) {
        this.id = id;
    }

    /**
     * Konstruktor entiteta bez ID-a.
     */
    protected Entity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ID: " + id;
    }
}
