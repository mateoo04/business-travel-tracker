package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.enums.ChangeLogType;
import hr.javafx.businesstraveltracker.enums.ReimbursementStatus;
import hr.javafx.businesstraveltracker.enums.UserPrivileges;
import hr.javafx.businesstraveltracker.model.*;
import hr.javafx.businesstraveltracker.repository.ChangeLogRepository;
import hr.javafx.businesstraveltracker.repository.EmployeeRepository;
import hr.javafx.businesstraveltracker.repository.ExpenseRepository;
import hr.javafx.businesstraveltracker.repository.ReimbursementRepository;
import hr.javafx.businesstraveltracker.util.ComboBoxSetter;
import hr.javafx.businesstraveltracker.util.ConfirmDeletionDialog;
import hr.javafx.businesstraveltracker.util.CustomDateTimeFormatter;
import hr.javafx.businesstraveltracker.util.SceneManager;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ReimbursementSearchController {

    @FXML
    private ComboBox<Expense> expenseComboBox;

    @FXML
    private ComboBox<ReimbursementStatus> reimbursementStatusComboBox;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private ComboBox<Employee> employeeComboBox;

    @FXML
    private TableView<Reimbursement> reimbursementTableView;

    @FXML
    private TableColumn<Reimbursement, Long> idColumn;

    @FXML
    private TableColumn<Reimbursement, String> expenseColumn;

    @FXML
    private TableColumn<Reimbursement, String> statusColumn;

    @FXML
    private TableColumn<Reimbursement, String> approvalDateColumn;

    private final ReimbursementRepository reimbursementRepository = new ReimbursementRepository();

    private final ExpenseRepository expenseRepository = new ExpenseRepository();

    private final EmployeeRepository employeeRepository = new EmployeeRepository();

    private final ChangeLogRepository changeLogRepository = new ChangeLogRepository();
    /**
     * Inicijalizira ekran.
     */
    public void initialize() {
        reimbursementStatusComboBox.getItems().add(null);
        reimbursementStatusComboBox.getSelectionModel().select(0);
        ComboBoxSetter.setReimbursementStatusComboBox(reimbursementStatusComboBox);

        idColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));

        expenseColumn.setCellValueFactory(cellData -> {
            Expense expense = cellData.getValue().getExpense();

            return new SimpleObjectProperty<>(expense.getCategory().getName() + ", " + expense.getAmount() + "€, " +
                    CustomDateTimeFormatter.formatDate(expense.getDate()));
        });

        statusColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getStatus().getStatus()));

        approvalDateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>
                (CustomDateTimeFormatter.formatDate(cellData.getValue().getApprovalDate())));

        List<Expense> expenseList = expenseRepository.findAll();
        expenseComboBox.getItems().add(null);
        expenseComboBox.getSelectionModel().select(0);
        ComboBoxSetter.setExpenseComboBox(expenseComboBox, expenseRepository.findAll());
        setUpEmployeeComboBox(expenseList);

        reimbursementTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        if(LogInController.getCurrentUser().getPrivileges().equals(UserPrivileges.HIGH))
            setContextMenuOnRowItems();
    }

    /**
     * Filtrira podatke prema parametrima koje je korisnik odredio.
     */
    public void filterReimbursements() {
        List<Reimbursement> reimbursements = reimbursementRepository.findAll();

        Expense expense = expenseComboBox.getSelectionModel().getSelectedItem();
        ReimbursementStatus status = reimbursementStatusComboBox.getSelectionModel().getSelectedItem();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        Employee employee = employeeComboBox.getSelectionModel().getSelectedItem();

        reimbursements = reimbursements.stream()
                .filter(reimbursement -> expense == null || reimbursement.getExpense().getId().equals(expense.getId()))
                .filter(reimbursement -> status == null || reimbursement.getStatus().getId().equals(status.getId()))
                .filter(reimbursement -> startDate == null || reimbursement.getApprovalDate().isAfter(startDate) ||
                        reimbursement.getApprovalDate().equals(startDate))
                .filter(reimbursement -> endDate == null || reimbursement.getApprovalDate().isBefore(endDate) ||
                        reimbursement.getApprovalDate().isEqual(endDate))
                .filter(reimbursement -> employee == null ||
                        reimbursement.getExpense().getTravelLog().getEmployee().getId().equals(employee.getId()))
                .toList();

        reimbursementTableView.setItems(FXCollections.observableList(reimbursements));
    }
    /**
     * Postavlja ContextMenu izbornik koji se prikazuje klikom sekundarnog gumba miša na redak u tablici.
     */
    private void setContextMenuOnRowItems(){
        ContextMenu contextMenu = new ContextMenu();
        MenuItem editItem = new MenuItem("Edit");
        MenuItem deleteItem = new MenuItem("Delete");
        contextMenu.getItems().addAll(editItem,deleteItem);

        editItem.setOnAction(event ->{
            Reimbursement selectedReimbursement = reimbursementTableView.getSelectionModel().getSelectedItem();
            if(selectedReimbursement != null) {
                SceneManager.getInstance().showEditReimbursementScreen(selectedReimbursement);
            }
        });

        deleteItem.setOnAction(event ->{
            Reimbursement selectedReimbursement = reimbursementTableView.getSelectionModel().getSelectedItem();
            if(selectedReimbursement != null) handleDelete(selectedReimbursement);
        });

        reimbursementTableView.setRowFactory(tv ->{
            TableRow<Reimbursement> row = new TableRow<>();
            row.setOnContextMenuRequested(event -> {
                if(!row.isEmpty()){
                    reimbursementTableView.getSelectionModel().select(row.getItem());
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                }
            });

            return row;
        });
    }

    /**
     * Postavlja izbornik zaposlenika
     * @param expenseList lista troškova
     */
    private void setUpEmployeeComboBox(List<Expense> expenseList){
        employeeComboBox.getItems().add(null);
        employeeComboBox.getSelectionModel().select(0);
        ComboBoxSetter.setEmployeeComboBox(employeeComboBox);

        employeeComboBox.setOnAction(event -> {
            Employee selectedEmployee = employeeComboBox.getValue();
            if(selectedEmployee != null) {
                EntitySetOfEntitiesPair<Employee, Expense> entitySetOfEntitiesPair =
                        new EntitySetOfEntitiesPair<>(selectedEmployee,
                                expenseList.stream()
                                        .filter(expense -> expense.getTravelLog().getEmployee().getId().equals(selectedEmployee.getId()))
                                        .collect(Collectors.toSet()));

                expenseComboBox.getItems().clear();
                expenseComboBox.getItems().add(null);
                expenseComboBox.getItems().addAll(entitySetOfEntitiesPair.getSetOfEntities());
                expenseComboBox.getSelectionModel().select(0);
            }else{
                employeeComboBox.getItems().add(null);
                employeeComboBox.getItems().addAll(employeeRepository.findAll());
                employeeComboBox.getSelectionModel().select(0);
            }
        });
    }

    private void handleDelete(Reimbursement reimbursement){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Delete Reimbursement");
        dialog.setHeaderText("Are you sure you want to delete the reimbursement?");
        ConfirmDeletionDialog.show(reimbursement, dialog,
                () -> reimbursementRepository.deleteById(reimbursement.getId()));

        changeLogRepository.log(new ChangeLog<>(reimbursement, ChangeLogType.DELETE));

        filterReimbursements();
    }
}
