package hr.javafx.businesstraveltracker.util;

import hr.javafx.businesstraveltracker.controller.LogInController;
import hr.javafx.businesstraveltracker.enums.Department;
import hr.javafx.businesstraveltracker.enums.ReimbursementStatus;
import hr.javafx.businesstraveltracker.enums.TripStatus;
import hr.javafx.businesstraveltracker.enums.UserPrivileges;
import hr.javafx.businesstraveltracker.model.*;
import hr.javafx.businesstraveltracker.repository.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;

import java.util.List;
/**
 * Klasa koja sadrži metode za postavljanje ComboBox elemenata.
 */
public class ComboBoxSetter {

    private static final TravelLogRepository travelLogRepository = new TravelLogRepository();
    private static final ExpenseCategoryRepository expenseCategoryRepository = new ExpenseCategoryRepository();
    private static final EmployeeRepository employeeRepository = new EmployeeRepository();

    private ComboBoxSetter(){}

    /**
     * Postavlja TravelLog ComboBox.
     * @param comboBox
     */
    public static void setTravelLogComboBox(ComboBox<TravelLog> comboBox) {
        if (LogInController.getCurrentUser().getPrivileges().equals(UserPrivileges.LOW) &&
                LogInController.getCurrentUser().getEmployeeId() != null) {
            List<TravelLog> travelLogList = travelLogRepository.findAll().stream()
                    .filter(travelLog -> travelLog.getEmployee().getId().equals(LogInController.getCurrentUser().getEmployeeId()))
                    .toList();
            comboBox.getItems().addAll(travelLogList);
        } else comboBox.getItems().addAll(travelLogRepository.findAll());

        comboBox.getSelectionModel().select(0);
        setCellFactoriesOnTravelLogComboBox(comboBox);
    }

    /**
     * Postavlja ExpenseCategory ComboBox.
     * @param comboBox
     */
    public static void setExpenseCategoryComboBox(ComboBox<ExpenseCategory> comboBox){
        comboBox.getItems().addAll(expenseCategoryRepository.findAll());
        comboBox.getSelectionModel().select(0);
        comboBox.setCellFactory(item -> new ListCell<>(){
            @Override
            protected void updateItem(ExpenseCategory expenseCategory, boolean b) {
                super.updateItem(expenseCategory, b);
                setText(b || expenseCategory == null ? "" : expenseCategory.getName());
            }
        });
        comboBox.setButtonCell(new ListCell<>(){
            @Override
            protected void updateItem(ExpenseCategory expenseCategory, boolean b) {
                super.updateItem(expenseCategory, b);
                setText(b || expenseCategory == null ? "" : expenseCategory.getName());
            }
        });
    }

    /**
     * Postavlja Department ComboBox.
     * @param comboBox
     */
    public static void setDepartmentComboBox(ComboBox<Department> comboBox){
        comboBox.getItems().addAll(Department.values());
        comboBox.setCellFactory(_ -> new ListCell<>(){
            @Override
            protected void updateItem(Department department, boolean empty) {
                super.updateItem(department, empty);
                setText(empty || department == null ? "" : department.getName());
            }
        });
        comboBox.setButtonCell(new ListCell<>(){
            @Override
            protected void updateItem(Department department, boolean empty) {
                super.updateItem(department, empty);
                setText(empty || department == null ? "" : department.getName());
            }
        });
    }

    /**
     * Postavlja Expense ComboBox.
     * @param comboBox
     */
    public static void setExpenseComboBox(ComboBox<Expense> comboBox, List<Expense> expenseList){
        comboBox.getItems().addAll(expenseList);
        comboBox.getSelectionModel().select(0);
        comboBox.setCellFactory(item -> new ListCell<>() {
            @Override
            protected void updateItem(Expense expense, boolean b) {
                super.updateItem(expense, b);
                setText(b || expense == null ? "" : expense.getCategory().getName() + ", " + expense.getAmount() + "€, " +
                        CustomDateTimeFormatter.formatDate(expense.getDate()));
            }
        });
        comboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Expense expense, boolean b) {
                super.updateItem(expense, b);
                setText(b || expense == null ? "" : expense.getCategory().getName() + ", " + expense.getAmount() + "€, " +
                        CustomDateTimeFormatter.formatDate(expense.getDate()));
            }
        });
    }

    /**
     * Postavlja ReimbursementStatus ComboBox.
     * @param comboBox
     */
    public static void setReimbursementStatusComboBox(ComboBox<ReimbursementStatus> comboBox){
        comboBox.getItems().addAll(ReimbursementStatus.values());
        comboBox.getSelectionModel().select(0);
        comboBox.setCellFactory(item -> new ListCell<>() {
            @Override
            protected void updateItem(ReimbursementStatus reimbursementStatus, boolean b) {
                super.updateItem(reimbursementStatus, b);
                setText(b || reimbursementStatus == null ? "" : reimbursementStatus.getStatus());
            }
        });
        comboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(ReimbursementStatus reimbursementStatus, boolean b) {
                super.updateItem(reimbursementStatus, b);
                setText(b || reimbursementStatus == null ? "" : reimbursementStatus.getStatus());
            }
        });
    }

    /**
     * Postavlja Employee ComboBox.
     * @param comboBox
     */
    public static void setEmployeeComboBox(ComboBox<Employee> comboBox){
        comboBox.getItems().addAll(employeeRepository.findAll());
        comboBox.getSelectionModel().select(0);
        setCellFactoriesOnEmployeeComboBox(comboBox);
    }

    /**
     * Postavlja TripStatus ComboBox.
     * @param comboBox
     */
    public static void setTripStatusComboBox(ComboBox<TripStatus> comboBox){
        comboBox.getItems().addAll(TripStatus.values());
        comboBox.getSelectionModel().select(0);
        comboBox.setCellFactory(item -> new ListCell<>(){
            @Override
            protected void updateItem(TripStatus tripStatus, boolean b) {
                super.updateItem(tripStatus, b);
                setText(b || tripStatus == null ? "" : tripStatus.getName());
            }
        });
        comboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(TripStatus tripStatus, boolean b) {
                super.updateItem(tripStatus, b);
                setText(b || tripStatus == null ? "" : tripStatus.getName());
            }
        });
    }

    /**
     * Postavlja izgled elemenata liste ComboBoxa i gumba ComboBoxa.
     * @param comboBox
     */
    public static void setCellFactoriesOnEmployeeComboBox(ComboBox<Employee> comboBox){
        comboBox.setCellFactory(item -> new ListCell<>() {
            @Override
            protected void updateItem(Employee employee, boolean b) {
                super.updateItem(employee, b);
                setText(b || employee == null ? "" : employee.getFirstName() + " " + employee.getLastName());
            }
        });
        comboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Employee employee, boolean b) {
                super.updateItem(employee, b);
                setText(b || employee == null ? "" : employee.getFirstName() + " " + employee.getLastName());
            }
        });
    }

    /**
     * Postavlja izgled elemenata liste ComboBoxa i gumba ComboBoxa.
     * @param comboBox
     */
    public static void setCellFactoriesOnTravelLogComboBox(ComboBox<TravelLog> comboBox){
        comboBox.setCellFactory(item -> new ListCell<>() {
            @Override
            protected void updateItem(TravelLog travelLog, boolean b) {
                super.updateItem(travelLog, b);
                setText(b || travelLog == null ? "" : travelLog.getDestination() + " (" +
                        CustomDateTimeFormatter.formatDate(travelLog.getStartDate()) + "-" +
                        CustomDateTimeFormatter.formatDate(travelLog.getEndDate()) + ")");
            }
        });
        comboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(TravelLog travelLog, boolean b) {
                super.updateItem(travelLog, b);
                setText(b || travelLog == null ? "" : travelLog.getDestination() + " (" +
                        CustomDateTimeFormatter.formatDate(travelLog.getStartDate()) + "-" +
                        CustomDateTimeFormatter.formatDate(travelLog.getEndDate()) + ")");
            }
        });
    }
}
