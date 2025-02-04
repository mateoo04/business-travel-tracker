package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.enums.ErrorMessage;
import hr.javafx.businesstraveltracker.enums.ReimbursementStatus;
import hr.javafx.businesstraveltracker.model.ChangeLog;
import hr.javafx.businesstraveltracker.model.Expense;
import hr.javafx.businesstraveltracker.model.Reimbursement;
import hr.javafx.businesstraveltracker.repository.ChangeLogRepository;
import hr.javafx.businesstraveltracker.repository.ExpenseRepository;
import hr.javafx.businesstraveltracker.repository.ReimbursementRepository;
import hr.javafx.businesstraveltracker.util.CustomDateTimeFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Optional;

public class EditReimbursementScreenController {
    @FXML
    public ComboBox<Expense> expenseComboBox;

    @FXML
    public ComboBox<ReimbursementStatus> reimbursementStatusComboBox;

    private final ExpenseRepository expenseRepository = new ExpenseRepository();

    private final ReimbursementRepository reimbursementRepository = new ReimbursementRepository();

    private final ChangeLogRepository changeLogRepository = new ChangeLogRepository();

    private Reimbursement reimbursement;

    public void initData(Reimbursement reimbursement){
        this.reimbursement = reimbursement;

        Optional<Expense> preselectedExpense = expenseComboBox.getItems().stream()
                .filter(item -> item.getId().equals(reimbursement.getId()))
                .findFirst();

        preselectedExpense.ifPresent(expense -> expenseComboBox.getSelectionModel().select(expense));

        reimbursementStatusComboBox.getSelectionModel().select(reimbursement.getStatus());
    }

    public void initialize() {
        expenseComboBox.getItems().addAll(expenseRepository.findAll());
        expenseComboBox.getSelectionModel().select(0);
        expenseComboBox.setCellFactory(item -> new ListCell<>() {
            @Override
            protected void updateItem(Expense expense, boolean b) {
                super.updateItem(expense, b);
                setText(b || expense == null ? "" : expense.getCategory().getName() + ", " + expense.getAmount() + "€, " +
                        CustomDateTimeFormatter.formatDate(expense.getDate()));
            }
        });
        expenseComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Expense expense, boolean b) {
                super.updateItem(expense, b);
                setText(b || expense == null ? "" : expense.getCategory().getName() + ", " + expense.getAmount() + "€, " +
                        CustomDateTimeFormatter.formatDate(expense.getDate()));
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

    public void saveChanges(){
        StringBuilder errorMessage = new StringBuilder();

        Expense expense = expenseComboBox.getSelectionModel().getSelectedItem();
        if (expense == null) errorMessage.append(ErrorMessage.EXPENSE_REQUIRED);

        ReimbursementStatus reimbursementStatus = reimbursementStatusComboBox.getSelectionModel().getSelectedItem();
        if (reimbursementStatus == null) errorMessage.append(ErrorMessage.REIMBURSEMENT_STATUS_REQUIRED);

        Reimbursement prevReimbursementValue = new Reimbursement(reimbursement);

        if(errorMessage.isEmpty()){
            if(!reimbursement.getExpense().equals(expense))
                reimbursement.setExpense(expense);
            if(!reimbursement.getStatus().equals(reimbursementStatus)) {
                if (reimbursement.getStatus().equals(ReimbursementStatus.APPROVED))
                    reimbursement.unapprove();
                else if (reimbursement.getStatus().equals(ReimbursementStatus.UNAPPROVED))
                    reimbursement.approve();
            }

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Reimbursement");
            dialog.setHeaderText("Are you sure you want to save changes to this reimbursement?");
            dialog.setContentText(reimbursement.toString());

            ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

            Optional<ButtonType> result = dialog.showAndWait();
            result.ifPresent(response ->{
                if (response == saveButtonType){
                    reimbursementRepository.update(reimbursement);
                    changeLogRepository.log(new ChangeLog<>(prevReimbursementValue, reimbursement));
                }
            });
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error while editing the reimbursement");
            alert.setContentText(errorMessage.toString());
        }
    }
}
