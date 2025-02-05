package hr.javafx.businesstraveltracker.enums;

import hr.javafx.businesstraveltracker.exception.InvalidEnumValueException;

/**
 * Predstavlja odjel unutar tvrtke.
 */
public enum Department {

    HUMAN_RESOURCES(0L, "Human Resources Department"),
    MARKETING_AND_SALES(1L, "Marketing and Sales Department"),
    ACCOUNTING_AND_FINANCE(2L, "Accounting And Finance Department"),
    PRODUCTION(3L, "Production Department"),
    RESEARCH_AND_DEVELOPMENT(4L, "Research And Development Department"),
    IT(5L, "IT Department"),
    CUSTOMER_SERVICE(6L, "Customer Service Department"),
    OPERATIONS_MANAGER(7L, "Operations Management Department"),
    ;

    private final Long id;
    private final String name;

    Department(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    /**
     * Dohvaća odjel prema ID-u.
     * @param id
     * @return odjel
     * @throws InvalidEnumValueException
     */
    public static Department getById(Long id) throws InvalidEnumValueException {
        for(Department department : values()) {
            if(department.getId().equals(id)) return department;
        }

        throw new InvalidEnumValueException("Department with id " + id + " not found");
    }
}
