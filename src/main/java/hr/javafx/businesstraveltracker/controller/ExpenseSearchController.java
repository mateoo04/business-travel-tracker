package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.enums.ErrorMessage;
import hr.javafx.businesstraveltracker.model.Expense;
import hr.javafx.businesstraveltracker.model.ExpenseCategory;
import hr.javafx.businesstraveltracker.model.TravelLog;
import hr.javafx.businesstraveltracker.repository.ExpenseRepository;
import hr.javafx.businesstraveltracker.util.CustomDateFormatter;
import hr.javafx.businesstraveltracker.util.DataValidation;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ExpenseSearchController {

    @FXML
    public ComboBox<TravelLog> travelLogComboBox;

    @FXML
    public ComboBox<ExpenseCategory> expenseCategoryComboBox;

    @FXML
    public TextArea descriptionTextArea;

    @FXML
    public TextField minAmountTextField;

    @FXML
    public TextField maxAmountTextField;

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

    private final ExpenseRepository expenseRepository = new ExpenseRepository();

    public void initialize() {
        idColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        travelLogColumn.setCellValueFactory(cellData -> {
            TravelLog travelLog = cellData.getValue().getTravelLog();

            return new SimpleObjectProperty<>(travelLog.getDestination() + " (" +
                    CustomDateFormatter.formatDate(travelLog.getStartDate()) + "-"
                    + CustomDateFormatter.formatDate(travelLog.getEndDate()) + ")");
        });
        expenseCategoryColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCategory().getName()));
        amountColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getAmount()));
        dateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDate()));
        descriptionColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDescription()));
    }

    public void filterExpenses(){
        List<Expense> expenses = expenseRepository.findAll();

        TravelLog travelLog = travelLogComboBox.getSelectionModel().getSelectedItem();
        ExpenseCategory expenseCategory = expenseCategoryComboBox.getSelectionModel().getSelectedItem();
        String description = descriptionTextArea.getText();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        BigDecimal minPrice = getPriceFromTextField(minAmountTextField);
        BigDecimal maxPrice = getPriceFromTextField(maxAmountTextField);

        if(minAmountTextField.getText().isEmpty() || DataValidation.isValidDecimalNumber(minAmountTextField.getText()) &&
                maxAmountTextField.getText().isEmpty() || DataValidation.isValidDecimalNumber(maxAmountTextField.getText())){
            expenses = expenses.stream()
                    .filter(expense -> travelLog == null || expense.getTravelLog().getId().equals(travelLog.getId()))
                    .filter(expense -> expenseCategory == null || expense.getCategory().getId().equals(expenseCategory.getId()))
                    .filter(expense -> description == null || description.isEmpty() ||
                            expense.getDescription().toLowerCase().contains(description.toLowerCase()))
                    .filter(expense -> startDate == null || expense.getDate().isAfter(startDate)
                            || expense.getDate().isEqual(startDate))
                    .filter(expense -> endDate == null || expense.getDate().isBefore(endDate)
                    || expense.getDate().isEqual(endDate))
                    .filter(expense -> minAmountTextField.getText().isEmpty() || expense.getAmount().compareTo(minPrice) >= 0)
                    .filter(expense -> maxAmountTextField.getText().isEmpty() || expense.getAmount().compareTo(maxPrice) <= 0)
                    .toList();

            expenseTableView.setItems(FXCollections.observableList(expenses));
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid number format for expense amount");
            alert.setContentText(ErrorMessage.AMOUNT_INPUT_ERROR.getMessage());
            alert.showAndWait();
        }
    }

    private BigDecimal getPriceFromTextField(TextField textField) {
        String priceString = textField.getText();
        if (priceString.isEmpty() || !DataValidation.isValidDecimalNumber(priceString)) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(priceString);
    }
}
