package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.enums.ReimbursementStatus;
import hr.javafx.businesstraveltracker.model.Expense;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

public class NewReimbursementScreenController {

    @FXML
    public ChoiceBox<Expense> expenseChoiceBox;

    @FXML
    public ChoiceBox<ReimbursementStatus> reimbursementStatusChoiceBox;

    public void createReimbursement(){}
}
