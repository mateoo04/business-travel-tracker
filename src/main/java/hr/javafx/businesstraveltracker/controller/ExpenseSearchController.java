package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.enums.ChangeLogType;
import hr.javafx.businesstraveltracker.enums.ErrorMessage;
import hr.javafx.businesstraveltracker.enums.UserPrivileges;
import hr.javafx.businesstraveltracker.model.ChangeLog;
import hr.javafx.businesstraveltracker.model.Expense;
import hr.javafx.businesstraveltracker.model.ExpenseCategory;
import hr.javafx.businesstraveltracker.model.TravelLog;
import hr.javafx.businesstraveltracker.repository.ChangeLogRepository;
import hr.javafx.businesstraveltracker.repository.ExpenseRepository;
import hr.javafx.businesstraveltracker.repository.TravelLogRepository;
import hr.javafx.businesstraveltracker.util.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Kontroler za pretraživanje troškova.
 */
public class ExpenseSearchController {

    @FXML
    private ComboBox<TravelLog> travelLogComboBox;

    @FXML
    private ComboBox<ExpenseCategory> expenseCategoryComboBox;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private TextField minAmountTextField;

    @FXML
    private TextField maxAmountTextField;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private TableView<Expense> expenseTableView;

    @FXML
    private TableColumn<Expense, Long> idColumn;

    @FXML
    private TableColumn<Expense, String> travelLogColumn;

    @FXML
    private TableColumn<Expense, String> expenseCategoryColumn;

    @FXML
    private TableColumn<Expense, BigDecimal> amountColumn;

    @FXML
    private TableColumn<Expense, String> dateColumn;

    @FXML
    private TableColumn<Expense, String> descriptionColumn;

    private final ExpenseRepository expenseRepository = new ExpenseRepository();

    private final ChangeLogRepository changeLogRepository = new ChangeLogRepository();

    private final TravelLogRepository travelLogRepository = new TravelLogRepository();
    /**
     * Inicijalizira ekran.
     */
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
        dateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(CustomDateTimeFormatter.formatDate(cellData.getValue().getDate())));
        descriptionColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDescription()));

        travelLogComboBox.getItems().add(null);
        travelLogComboBox.getSelectionModel().select(0);
        travelLogComboBox.getItems().addAll(travelLogRepository.findAll());
        ComboBoxSetter.setCellFactoriesOnTravelLogComboBox(travelLogComboBox);

        expenseCategoryComboBox.getItems().add(null);
        expenseCategoryComboBox.getSelectionModel().select(0);
        ComboBoxSetter.setExpenseCategoryComboBox(expenseCategoryComboBox);

        expenseTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        setContextMenuOnRowItems();
    }
    /**
     * Postavlja ContextMenu izbornik koji se prikazuje klikom sekundarnog gumba miša na redak u tablici.
     */
    private void setContextMenuOnRowItems(){
        ContextMenu contextMenu = new ContextMenu();
        MenuItem editItem = new MenuItem("Edit");
        MenuItem deleteItem = new MenuItem("Delete");
        contextMenu.getItems().addAll(editItem,deleteItem);

        editItem.setOnAction(event ->{
            Expense selectedExpense = expenseTableView.getSelectionModel().getSelectedItem();
            if(selectedExpense != null && (LogInController.getCurrentUser().getPrivileges().equals(UserPrivileges.HIGH) ||
                    LogInController.getCurrentUser().getEmployeeId().equals(selectedExpense.getTravelLog().getEmployee().getId()))) {
                SceneManager.getInstance().showEditExpenseScreen(selectedExpense);
            } else if (selectedExpense != null) UnauthorisedAlert.show();
        });

        deleteItem.setOnAction(event ->{
            Expense selectedExpense = expenseTableView.getSelectionModel().getSelectedItem();
            if(selectedExpense != null && (LogInController.getCurrentUser().getPrivileges().equals(UserPrivileges.HIGH) ||
                    LogInController.getCurrentUser().getEmployeeId().equals(selectedExpense.getTravelLog().getEmployee().getId())))
                handleDelete(selectedExpense);
         else if (selectedExpense != null) UnauthorisedAlert.show();
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

    /**
     * Filtrira podatke prema parametrima koje je korisnik odredio.
     */
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

    /**
     * Validira i pretvara unos u polje novčanog iznosa u odgovrajaući tip podatka
     * @param textField polje unosa
     * @return novčani iznos u obliku BigDecimal-a
     */
    private BigDecimal getAmountFromTextField(TextField textField) {
        String amountString = textField.getText();
        if (amountString.isEmpty() || !DataValidation.isValidDecimalNumber(amountString)) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(amountString);
    }

    /**
     * Izvodi brisanje zapisa.
     * @param expense trošak koji će biti obrisan.
     */
    public void handleDelete(Expense expense){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Delete Expense");
        dialog.setHeaderText("Are you sure you want to delete the expense?");
        ConfirmDeletionDialog.show(expense, dialog, () -> expenseRepository.deleteById(expense.getId()));

        changeLogRepository.log(new ChangeLog<>(expense, ChangeLogType.DELETE));

        filterExpenses();
    }
}
