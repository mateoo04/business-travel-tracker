package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.enums.ChangeLogType;
import hr.javafx.businesstraveltracker.enums.ErrorMessage;
import hr.javafx.businesstraveltracker.enums.ReimbursementStatus;
import hr.javafx.businesstraveltracker.model.ChangeLog;
import hr.javafx.businesstraveltracker.model.Expense;
import hr.javafx.businesstraveltracker.model.Reimbursement;
import hr.javafx.businesstraveltracker.repository.ChangeLogRepository;
import hr.javafx.businesstraveltracker.repository.ReimbursementRepository;
import hr.javafx.businesstraveltracker.util.ComboBoxSetter;
import hr.javafx.businesstraveltracker.util.ExpensesWithoutReimbursementRecordsFinder;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;

import java.time.LocalDate;
/**
 * Kontroler za unos novih nadoknada troškova u bazu podataka.
 */
public class NewReimbursementScreenController {

    @FXML
    public ComboBox<Expense> expenseComboBox;

    @FXML
    public ComboBox<ReimbursementStatus> reimbursementStatusComboBox;

    private final ReimbursementRepository reimbursementRepository = new ReimbursementRepository();

    private final ChangeLogRepository changeLogRepository = new ChangeLogRepository();
    /**
     * Inicijalizira ekran.
     */
    public void initialize() {
        ComboBoxSetter.setExpenseComboBox(expenseComboBox, ExpensesWithoutReimbursementRecordsFinder.find());
        ComboBoxSetter.setReimbursementStatusComboBox(reimbursementStatusComboBox);
    }

    /**
     * Zahtjeva unos novih nadoknada troškova u bazu podataka ukoliko su svi podaci točno uneseni.
     */
    public void createReimbursement(){
        StringBuilder errorMessage = new StringBuilder();

        Expense expense = expenseComboBox.getSelectionModel().getSelectedItem();
        if (expense == null) errorMessage.append(ErrorMessage.EXPENSE_REQUIRED);

        ReimbursementStatus reimbursementStatus = reimbursementStatusComboBox.getSelectionModel().getSelectedItem();
        if (reimbursementStatus == null) errorMessage.append(ErrorMessage.REIMBURSEMENT_STATUS_REQUIRED);

        if(errorMessage.isEmpty()){
            Reimbursement reimbursement = new Reimbursement(expense, reimbursementStatus, LocalDate.now());
            reimbursementRepository.save(reimbursement);
            changeLogRepository.log(new ChangeLog<>(reimbursement, ChangeLogType.NEW));

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
