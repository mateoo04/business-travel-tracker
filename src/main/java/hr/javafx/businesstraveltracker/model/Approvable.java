package hr.javafx.businesstraveltracker.model;

/**
 * Predstavlja entitet koji može biti approved li unapproved.
 */
public sealed interface Approvable permits Reimbursement{
    /**
     * Označava entitet kao approved.
     */
    void approve();

    /**
     * Označava entitet kao unapproved.
     */
    void unapprove();
}
