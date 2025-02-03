package hr.javafx.businesstraveltracker.enums;

public enum ErrorMessage {

    FIRST_NAME_REQUIRED("First name is required!"),
    LAST_NAME_REQUIRED("Last name is required!"),
    ROLE_REQUIRED("Role is required!"),
    DEPARTMENT_REQUIRED("Selecting a department is required!"),
    NAME_REQUIRED("Name is required!"),
    DESCRIPTION_REQUIRED("Description is required!"),
    EMPLOYEE_REQUIRED("At least one employee must be selected!"),
    DESTINATION_REQUIRED("Destination is required!"),
    START_DATE_REQUIRED("Selecting a start date is required!"),
    END_DATE_REQUIRED("Selecting a end date is required!"),
    TRIP_STATUS_REQUIRED("Selecting a trip status is required!"),
    TRAVEL_LOG_REQUIRED("Travel Log selection is required!"),
    EXPENSE_CATEGORY_REQUIRED("Expense Category selection is required!"),
    EXPENSE_AMOUNT_REQUIRED("Expense amount field must be filled!"),
    EXPENSE_DESCRIPTION_REQUIRED("Expense description is required!"),
    DATE_REQUIRED("Date selection is required!"),
    AMOUNT_INPUT_ERROR("Amount is required and must be a whole or a decimal number, for example: 60 or 870.50!"),
    EXPENSE_REQUIRED("Expense must be selected!"),
    REIMBURSEMENT_STATUS_REQUIRED("Selecting a reimbursement status is required!"),;

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message + '\n';
    }
}
