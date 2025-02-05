package hr.javafx.businesstraveltracker.model;

import hr.javafx.businesstraveltracker.util.CustomDateTimeFormatter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Klasa koja predstavlja trošak.
 */
public class Expense extends Entity {
    private TravelLog travelLog;
    private ExpenseCategory category;
    private BigDecimal amount;
    private String description;
    private LocalDate date;

    /**
     * Konstruktor objekta s ID-jem
     * @param id
     * @param travelLog bilješka putovanja
     * @param category kategorija troška
     * @param amount novčani iznos
     * @param description opis
     * @param date datum
     */
    public Expense(Long id, TravelLog travelLog, ExpenseCategory category, BigDecimal amount, String description, LocalDate date) {
        super(id);
        this.travelLog = travelLog;
        this.category = category;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    /**
     * Konstruktor objekta bez ID-a
     * @param travelLog bilješka putovanja
     * @param category kategorija troška
     * @param amount novčani iznos
     * @param description opis
     * @param date datum
     */
    public Expense(TravelLog travelLog, ExpenseCategory category, BigDecimal amount, String description, LocalDate date) {
        super();
        this.travelLog = travelLog;
        this.category = category;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    /**
     * Kopirni konstruktor
     * @param other objekt čija će se vrijednost kopirati
     */
    public Expense(Expense other) {
        super(other.getId());
        this.travelLog = other.getTravelLog();
        this.category = other.getCategory();
        this.amount = other.getAmount();
        this.description = other.getDescription();
        this.date = other.getDate();
    }

    public TravelLog getTravelLog() {
        return travelLog;
    }

    public void setTravelLog(TravelLog travelLog) {
        this.travelLog = travelLog;
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    public void setCategory(ExpenseCategory category) {
        this.category = category;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Category: " + this.category + "\nAmount: " + this.amount + "\nDescription: " + this.description +
                "\nDate: " + CustomDateTimeFormatter.formatDate(this.date);
    }
}
