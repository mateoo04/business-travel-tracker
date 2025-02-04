package hr.javafx.businesstraveltracker.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Expense extends Entity {
    private TravelLog travelLog;
    private ExpenseCategory category;
    private BigDecimal amount;
    private String description;
    private LocalDate date;

    public Expense(Long id, TravelLog travelLog, ExpenseCategory category, BigDecimal amount, String description, LocalDate date) {
        super(id);
        this.travelLog = travelLog;
        this.category = category;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }
    public Expense(TravelLog travelLog, ExpenseCategory category, BigDecimal amount, String description, LocalDate date) {
        super();
        this.travelLog = travelLog;
        this.category = category;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }
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
                "\nDate: " + this.date;
    }
}
