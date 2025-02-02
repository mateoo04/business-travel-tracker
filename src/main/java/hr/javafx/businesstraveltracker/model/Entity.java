package hr.javafx.businesstraveltracker.model;

public abstract class Entity {

    private Long id;

    public Entity(Long id) {
        this.id = id;
    }

    public Entity() {
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
