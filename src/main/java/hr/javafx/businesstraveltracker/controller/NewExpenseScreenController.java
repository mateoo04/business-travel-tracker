package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.model.Expense;
import hr.javafx.businesstraveltracker.model.TravelLog;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class NewExpenseScreenController {

    @FXML
    public ChoiceBox<TravelLog> travelLogChoiceBox;

    @FXML
    public ChoiceBox<Expense> expenseChoiceBox;

    @FXML
    public TextField expenseAmountTextField;

    @FXML
    public TextField descriptionTextField;

    @FXML
    public DatePicker expenseDatePicker;

    public void createExpense() {}
}
