package hr.javafx.businesstraveltracker.model;

import hr.javafx.businesstraveltracker.enums.ReimbursementStatus;

import java.time.LocalDate;

public class Reimbursement extends Entity {
    private Expense expense;
    private Employee employee;
    private ReimbursementStatus status;
    private LocalDate dateProcessed;

    public Reimbursement(Long id, Expense expense, Employee employee, ReimbursementStatus status, LocalDate dateProcessed) {
        super(id);
        this.expense = expense;
        this.employee = employee;
        this.status = status;
        this.dateProcessed = dateProcessed;
    }

    public Expense getExpense() {
        return expense;
    }

    public void setExpense(Expense expense) {
        this.expense = expense;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public ReimbursementStatus getStatus() {
        return status;
    }

    public void setStatus(ReimbursementStatus status) {
        this.status = status;
    }

    public LocalDate getDateProcessed() {
        return dateProcessed;
    }

    public void setDateProcessed(LocalDate dateProcessed) {
        this.dateProcessed = dateProcessed;
    }
}
