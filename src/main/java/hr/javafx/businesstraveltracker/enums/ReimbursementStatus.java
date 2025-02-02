package hr.javafx.businesstraveltracker.enums;

import hr.javafx.businesstraveltracker.exception.InvalidEnumValueException;

public enum ReimbursementStatus {
    UNAPPROVED(0L, "Unapproved"),
    APPROVED(1L, "Approved");

    private Long id;
    private String status;

    ReimbursementStatus(Long id, String status) {
        this.id = id;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public static ReimbursementStatus getByStatus(String status) throws InvalidEnumValueException {
        for (ReimbursementStatus reimbursementStatus : ReimbursementStatus.values())
            if(reimbursementStatus.status.equals(status)) return reimbursementStatus;

        throw new InvalidEnumValueException("Invalid reimbursement status:" + status);
    }
}
