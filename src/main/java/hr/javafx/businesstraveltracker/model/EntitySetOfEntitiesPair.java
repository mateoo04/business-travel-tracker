package hr.javafx.businesstraveltracker.model;

import java.util.Set;

/**
 * Klasa koja predstvalja zapis entiteta i set entiteta druge klase.
 * @param <T>
 * @param <G>
 */
public class EntitySetOfEntitiesPair<T extends Entity, G extends Entity> {
    private T entity;
    private Set<G> setOfEntities;

    public EntitySetOfEntitiesPair(T entity, Set<G> setOfEntities) {
        this.entity = entity;
        this.setOfEntities = setOfEntities;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public Set<G> getSetOfEntities() {
        return setOfEntities;
    }

    public void setSetOfEntities(Set<G> setOfEntities) {
        this.setOfEntities = setOfEntities;
    }
}
