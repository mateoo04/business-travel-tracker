package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.enums.ErrorMessage;
import hr.javafx.businesstraveltracker.enums.ReimbursementStatus;
import hr.javafx.businesstraveltracker.model.Expense;
import hr.javafx.businesstraveltracker.model.Reimbursement;
import hr.javafx.businesstraveltracker.repository.ExpenseRepository;
import hr.javafx.businesstraveltracker.repository.ReimbursementRepository;
import hr.javafx.businesstraveltracker.util.CustomDateFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;

import java.time.LocalDate;

public class NewReimbursementScreenController {

    @FXML
    public ComboBox<Expense> expenseComboBox;

    @FXML
    public ComboBox<ReimbursementStatus> reimbursementStatusComboBox;

    private final ExpenseRepository expenseRepository = new ExpenseRepository();

    private final ReimbursementRepository reimbursementRepository = new ReimbursementRepository();

    public void initialize() {
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
    }

    public void createReimbursement(){
        StringBuilder errorMessage = new StringBuilder();

        Expense expense = expenseComboBox.getSelectionModel().getSelectedItem();
        if (expense == null) errorMessage.append(ErrorMessage.EXPENSE_REQUIRED);

        ReimbursementStatus reimbursementStatus = reimbursementStatusComboBox.getSelectionModel().getSelectedItem();
        if (reimbursementStatus == null) errorMessage.append(ErrorMessage.REIMBURSEMENT_STATUS_REQUIRED);

        if(errorMessage.isEmpty()){
            Reimbursement reimbursement = new Reimbursement(expense, reimbursementStatus, LocalDate.now());
            reimbursementRepository.save(reimbursement);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("New Reimbursement");
            alert.setHeaderText("Reimbursement Added Successfully");
            alert.setContentText(reimbursement.toString());
            alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error while adding a new reimbursement");
            alert.setContentText(errorMessage.toString());
        }
    }
}
