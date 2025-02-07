package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.enums.ChangeLogType;
import hr.javafx.businesstraveltracker.enums.ErrorMessage;
import hr.javafx.businesstraveltracker.model.ChangeLog;
import hr.javafx.businesstraveltracker.model.Expense;
import hr.javafx.businesstraveltracker.model.ExpenseCategory;
import hr.javafx.businesstraveltracker.model.TravelLog;
import hr.javafx.businesstraveltracker.repository.ChangeLogRepository;
import hr.javafx.businesstraveltracker.repository.ExpenseRepository;
import hr.javafx.businesstraveltracker.util.ComboBoxSetter;
import hr.javafx.businesstraveltracker.util.DataValidation;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Kontroler za dodavanje novih troškova.
 */
public class NewExpenseScreenController {

    @FXML
    public ComboBox<TravelLog> travelLogComboBox;

    @FXML
    public ComboBox<ExpenseCategory> expenseCategoryComboBox;

    @FXML
    public TextField expenseAmountTextField;

    @FXML
    public TextArea descriptionTextArea;

    @FXML
    public DatePicker expenseDatePicker;

    private final ExpenseRepository expenseRepository = new ExpenseRepository();

    private final ChangeLogRepository changeLogRepository = new ChangeLogRepository();

    /**
     * Inicijalizira ekran.
     */
    public void initialize() {
        ComboBoxSetter.setTravelLogComboBox(travelLogComboBox);
        ComboBoxSetter.setExpenseCategoryComboBox(expenseCategoryComboBox);
    }

    /**
     * Zahtjeva dodavanje novih troškova u bazu podataka ukoliko su svi podaci točno uneseni.
     */
    public void createExpense() {
        StringBuilder errorMessage = new StringBuilder();

        TravelLog travelLog = travelLogComboBox.getSelectionModel().getSelectedItem();
        if (travelLog == null) errorMessage.append(ErrorMessage.TRAVEL_LOG_REQUIRED.getMessage());

        ExpenseCategory expenseCategory = expenseCategoryComboBox.getSelectionModel().getSelectedItem();
        if (expenseCategory == null) errorMessage.append(ErrorMessage.EXPENSE_CATEGORY_REQUIRED.getMessage());

        String amountString = expenseAmountTextField.getText();
        BigDecimal amount = BigDecimal.ZERO;
        if (amountString != null && !amountString.isEmpty() && DataValidation.isValidDecimalNumber(amountString))
            amount = new BigDecimal(amountString);
        else errorMessage.append(ErrorMessage.AMOUNT_INPUT_ERROR.getMessage());

        String description = descriptionTextArea.getText();
        if (description.isEmpty()) errorMessage.append(ErrorMessage.DESCRIPTION_REQUIRED.getMessage());

        LocalDate expenseDate = expenseDatePicker.getValue();
        if (expenseDate == null) errorMessage.append(ErrorMessage.DATE_REQUIRED.getMessage());

        if (errorMessage.isEmpty()) {
            Expense expense = new Expense(travelLog, expenseCategory, amount, description, expenseDate);
            expenseRepository.save(expense);
            changeLogRepository.log(new ChangeLog<>(expense, ChangeLogType.NEW));

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("New Expense");
            alert.setHeaderText("Expense Added Successfully");
            alert.setContentText(expense.toString());
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error while adding a new expense");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
        }
    }
}
