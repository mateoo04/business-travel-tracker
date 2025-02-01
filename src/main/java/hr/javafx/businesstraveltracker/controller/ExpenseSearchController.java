package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.model.Expense;
import hr.javafx.businesstraveltracker.model.ExpenseCategory;
import hr.javafx.businesstraveltracker.model.TravelLog;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseSearchController {

    @FXML
    public ChoiceBox<TravelLog> travelLogChoiceBox;

    @FXML
    public ChoiceBox<ExpenseCategory> expenseCategoryChoiceBox;

    @FXML
    public TextArea descriptionTextArea;

    @FXML
    public TextField minPriceTextField;

    @FXML
    public TextField maxPriceTextField;

    @FXML
    public DatePicker startDatePicker;

    @FXML
    public DatePicker endDatePicker;

    @FXML
    public TableView<Expense> expenseTableView;

    @FXML
    public TableColumn<Expense, Long> idColumn;

    @FXML
    public TableColumn<Expense, String> travelLogColumn;

    @FXML
    public TableColumn<Expense, String> expenseCategoryColumn;

    @FXML
    public TableColumn<Expense, BigDecimal> amountColumn;

    @FXML
    public TableColumn<Expense, LocalDate> dateColumn;

    @FXML
    public TableColumn<Expense, String> descriptionColumn;

    public void initialize() {}

    public void filterExpenses(){}
}
