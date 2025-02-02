package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.enums.ReimbursementStatus;
import hr.javafx.businesstraveltracker.model.Expense;
import hr.javafx.businesstraveltracker.model.Reimbursement;
import hr.javafx.businesstraveltracker.repository.ExpenseRepository;
import hr.javafx.businesstraveltracker.repository.ReimbursementRepository;
import hr.javafx.businesstraveltracker.util.CustomDateFormatter;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.List;

public class ReimbursementSearchController {

    @FXML
    public ComboBox<Expense> expenseComboBox;

    @FXML
    public ComboBox<ReimbursementStatus> reimbursementStatusComboBox;

    @FXML
    public DatePicker startDatePicker;

    @FXML
    public DatePicker endDatePicker;

    @FXML
    public TableView<Reimbursement> reimbursementTableView;

    @FXML
    public TableColumn<Reimbursement, Long> idColumn;

    @FXML
    public TableColumn<Reimbursement, String> expenseColumn;

    @FXML
    public TableColumn<Reimbursement, String> statusColumn;

    @FXML
    public TableColumn<Reimbursement, String> approvalDateColumn;

    private final ReimbursementRepository reimbursementRepository = new ReimbursementRepository();

    private final ExpenseRepository expenseRepository = new ExpenseRepository();

    public void initialize() {
        expenseComboBox.getItems().add(null);
        expenseComboBox.getItems().addAll(expenseRepository.findAll());
        expenseComboBox.getSelectionModel().select(0);
        expenseComboBox.setCellFactory(item -> new ListCell<>() {
            @Override
            protected void updateItem(Expense expense, boolean b) {
                super.updateItem(expense, b);
                setText(b || expense == null ? "" : expense.getCategory().getName() + ", " + expense.getAmount() + "€, " +
                        CustomDateFormatter.formatDate(expense.getDate()));
            }
        });
        expenseComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Expense expense, boolean b) {
                super.updateItem(expense, b);
                setText(b || expense == null ? "" : expense.getCategory().getName() + ", " + expense.getAmount() + "€, " +
                        CustomDateFormatter.formatDate(expense.getDate()));
            }
        });

        reimbursementStatusComboBox.getItems().add(null);
        reimbursementStatusComboBox.getItems().addAll(ReimbursementStatus.values());
        reimbursementStatusComboBox.getSelectionModel().select(0);
        reimbursementStatusComboBox.setCellFactory(item -> new ListCell<>() {
            @Override
            protected void updateItem(ReimbursementStatus reimbursementStatus, boolean b) {
                super.updateItem(reimbursementStatus, b);
                setText(b || reimbursementStatus == null ? "" : reimbursementStatus.getStatus());
            }
        });
        reimbursementStatusComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(ReimbursementStatus reimbursementStatus, boolean b) {
                super.updateItem(reimbursementStatus, b);
                setText(b || reimbursementStatus == null ? "" : reimbursementStatus.getStatus());
            }
        });

        idColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));

        expenseColumn.setCellValueFactory(cellData -> {
            Expense expense = cellData.getValue().getExpense();

            return new SimpleObjectProperty<>(expense.getCategory().getName() + ", " + expense.getAmount() + "€, " +
                    CustomDateFormatter.formatDate(expense.getDate()));
        });

        statusColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getStatus().getStatus()));

        approvalDateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>
                (CustomDateFormatter.formatDate(cellData.getValue().getApprovalDate())));
    }

    public void filterReimbursements() {
        List<Reimbursement> reimbursements = reimbursementRepository.findAll();

        Expense expense = expenseComboBox.getSelectionModel().getSelectedItem();
        ReimbursementStatus status = reimbursementStatusComboBox.getSelectionModel().getSelectedItem();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        reimbursements = reimbursements.stream()
                .filter(reimbursement -> expense == null || reimbursement.getExpense().getId().equals(expense.getId()))
                .filter(reimbursement -> status == null || reimbursement.getStatus().getId().equals(status.getId()))
                .filter(reimbursement -> startDate == null || reimbursement.getApprovalDate().isAfter(startDate) ||
                        reimbursement.getApprovalDate().equals(startDate))
                .filter(reimbursement -> endDate == null || reimbursement.getApprovalDate().isBefore(endDate) ||
                        reimbursement.getApprovalDate().isEqual(endDate))
                .toList();

        reimbursementTableView.setItems(FXCollections.observableList(reimbursements));
    }
}
