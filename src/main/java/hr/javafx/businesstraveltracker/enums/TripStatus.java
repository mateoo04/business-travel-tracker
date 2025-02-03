package hr.javafx.businesstraveltracker.enums;

import hr.javafx.businesstraveltracker.exception.InvalidEnumValueException;

public enum TripStatus {

    PLANNED(0L, "Planned"),
    APPROVED(1L, "Approved"),
    IN_PROGRESS(2L, "In Progress"),
    COMPLETED(3L, "Completed"),
    CANCELLED(4L, "Cancelled");

    private final Long id;
    private final String name;

    TripStatus(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static TripStatus getByName(String name) throws InvalidEnumValueException {
        for (TripStatus status : TripStatus.values()) {
            if (status.getName().equals(name)) return status;
        }

        throw new InvalidEnumValueException("Invalid trip status: " + name);
    }
}
