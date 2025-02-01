package hr.javafx.businesstraveltracker.model;

public sealed interface Approvable permits Reimbursement{
    void approve();
    void unapprove();
}
