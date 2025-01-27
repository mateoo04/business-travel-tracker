package hr.javafx.businesstraveltracker.enums;

public enum ReimbursementStatus {
    UNAPPROVED(0L),
    APPROVED(1L);

    private Long id;

    ReimbursementStatus(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
