package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.enums.ChangeLogType;
import hr.javafx.businesstraveltracker.enums.ErrorMessage;
import hr.javafx.businesstraveltracker.model.ChangeLog;
import hr.javafx.businesstraveltracker.model.Expense;
import hr.javafx.businesstraveltracker.model.ExpenseCategory;
import hr.javafx.businesstraveltracker.model.TravelLog;
import hr.javafx.businesstraveltracker.repository.ChangeLogRepository;
import hr.javafx.businesstraveltracker.repository.ExpenseRepository;
import hr.javafx.businesstraveltracker.util.ConfirmDeletionDialog;
import hr.javafx.businesstraveltracker.util.CustomDateTimeFormatter;
import hr.javafx.businesstraveltracker.util.DataValidation;
import hr.javafx.businesstraveltracker.util.SceneManager;
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

    private final ChangeLogRepository changeLogRepository = new ChangeLogRepository();

    public void initialize() {
        idColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        travelLogColumn.setCellValueFactory(cellData -> {
            TravelLog travelLog = cellData.getValue().getTravelLog();

            return new SimpleObjectProperty<>(travelLog.getDestination() + " (" +
                    CustomDateTimeFormatter.formatDate(travelLog.getStartDate()) + "-"
                    + CustomDateTimeFormatter.formatDate(travelLog.getEndDate()) + ")");
        });
        expenseCategoryColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCategory().getName()));
        amountColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getAmount()));
        dateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDate()));
        descriptionColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDescription()));

        ContextMenu contextMenu = new ContextMenu();
        MenuItem editItem = new MenuItem("Edit");
        MenuItem deleteItem = new MenuItem("Delete");
        contextMenu.getItems().addAll(editItem,deleteItem);

        editItem.setOnAction(event ->{
            Expense selectedExpense = expenseTableView.getSelectionModel().getSelectedItem();
            if(selectedExpense != null) {
                SceneManager.getInstance().showEditExpenseScreen(selectedExpense);
            }
        });

        deleteItem.setOnAction(event ->{
            Expense selectedExpense = expenseTableView.getSelectionModel().getSelectedItem();
            if(selectedExpense != null) handleDelete(selectedExpense);
        });

        expenseTableView.setRowFactory(tv ->{
            TableRow<Expense> row = new TableRow<>();
            row.setOnContextMenuRequested(event -> {
                if(!row.isEmpty()){
                    expenseTableView.getSelectionModel().select(row.getItem());
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                }
            });

            return row;
        });
    }

    public void filterExpenses(){
        List<Expense> expenses = expenseRepository.findAll();

        TravelLog travelLog = travelLogComboBox.getSelectionModel().getSelectedItem();
        ExpenseCategory expenseCategory = expenseCategoryComboBox.getSelectionModel().getSelectedItem();
        String description = descriptionTextArea.getText();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        BigDecimal minAmount = getAmountFromTextField(minAmountTextField);
        BigDecimal maxAmount = getAmountFromTextField(maxAmountTextField);

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
                    .filter(expense -> minAmountTextField.getText().isEmpty() || expense.getAmount().compareTo(minAmount) >= 0)
                    .filter(expense -> maxAmountTextField.getText().isEmpty() || expense.getAmount().compareTo(maxAmount) <= 0)
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

    private BigDecimal getAmountFromTextField(TextField textField) {
        String amountString = textField.getText();
        if (amountString.isEmpty() || !DataValidation.isValidDecimalNumber(amountString)) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(amountString);
    }

    public void handleDelete(Expense expense){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Delete Expense");
        dialog.setHeaderText("Are you sure you want to delete the expense?");
        ConfirmDeletionDialog.show(expense, dialog, () -> expenseRepository.deleteById(expense.getId()));

        changeLogRepository.log(new ChangeLog<>(expense, ChangeLogType.DELETE));

        filterExpenses();
    }
}
