package hr.javafx.businesstraveltracker.model;

import hr.javafx.businesstraveltracker.enums.ReimbursementStatus;

import java.time.LocalDate;

public final class Reimbursement extends Entity implements Approvable{
    private Expense expense;
    private ReimbursementStatus status;
    private LocalDate approvalDate;

    public Reimbursement(Long id, Expense expense, ReimbursementStatus status, LocalDate approvalDate) {
        super(id);
        this.expense = expense;
        this.status = status;
        this.approvalDate = approvalDate;
    }
    public Reimbursement(Expense expense, ReimbursementStatus status, LocalDate approvalDate) {
        super();
        this.expense = expense;
        this.status = status;
        this.approvalDate = approvalDate;
    }

    public Expense getExpense() {
        return expense;
    }

    public void setExpense(Expense expense) {
        this.expense = expense;
    }

    public LocalDate getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(LocalDate approvalDate) {
        this.approvalDate = approvalDate;
    }

    @Override
    public void approve() {
        this.status = ReimbursementStatus.APPROVED;
    }

    @Override
    public void unapprove() {
        this.status = ReimbursementStatus.UNAPPROVED;
    }

    public ReimbursementStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Reimbursement for Expense: " + this.expense.toString() + "\nStatus: " + this.status.getStatus() +
                (this.status.equals(ReimbursementStatus.APPROVED) ? "\nDate of approval: " + this.approvalDate : "");
    }
}
