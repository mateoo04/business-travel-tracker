package hr.javafx.businesstraveltracker.model;

/**
 * Klasa koja predstavlja kategoriju troška.
 */
public class ExpenseCategory extends Entity {
    private String name;
    private String description;

    /**
     * Konstruktor za objekt s ID-jem.
     * @param id
     * @param name naziv
     * @param description opis
     */
    public ExpenseCategory(Long id, String name, String description) {
        super(id);
        this.name = name;
        this.description = description;
    }

    /**
     * Konstruktor za objekt bez ID-a.
     * @param name naziv
     * @param description opis
     */
    public ExpenseCategory(String name, String description) {
        super();
        this.name = name;
        this.description = description;
    }

    /**
     * Kopirni konstruktor
     * @param other objekt čija će se vrijednost kopirati
     */
    public ExpenseCategory(ExpenseCategory other) {
        super(other.getId());
        this.name = other.getName();
        this.description = other.getDescription();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.name + " (" + this.description + ")";
    }
}
