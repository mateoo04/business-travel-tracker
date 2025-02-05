package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.enums.ErrorMessage;
import hr.javafx.businesstraveltracker.model.ChangeLog;
import hr.javafx.businesstraveltracker.model.Expense;
import hr.javafx.businesstraveltracker.model.ExpenseCategory;
import hr.javafx.businesstraveltracker.model.TravelLog;
import hr.javafx.businesstraveltracker.repository.ChangeLogRepository;
import hr.javafx.businesstraveltracker.repository.ExpenseCategoryRepository;
import hr.javafx.businesstraveltracker.repository.ExpenseRepository;
import hr.javafx.businesstraveltracker.repository.TravelLogRepository;
import hr.javafx.businesstraveltracker.util.CustomDateTimeFormatter;
import hr.javafx.businesstraveltracker.util.DataValidation;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Kontroler za uređivanje troška.
 */
public class EditExpenseScreenController {
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

    private final TravelLogRepository travelLogRepository = new TravelLogRepository();

    private final ExpenseCategoryRepository expenseCategoryRepository = new ExpenseCategoryRepository();

    private final ExpenseRepository expenseRepository = new ExpenseRepository();

    private final ChangeLogRepository changeLogRepository = new ChangeLogRepository();

    private Expense expense;

    /**
     * Postavlja objekt troška koji će biti uređivan.
     * @param expense objekt troška koji će biti uređivan
     */
    public void initData(Expense expense){
        this.expense = expense;

        Optional<TravelLog> preselectedTravelLog = travelLogComboBox.getItems().stream()
                .filter(item -> item.getId().equals(expense.getTravelLog().getId()))
                .findFirst();

        preselectedTravelLog.ifPresent(travelLog -> travelLogComboBox.getSelectionModel().select(travelLog));

        Optional<ExpenseCategory> preselectedCategory = expenseCategoryComboBox.getItems().stream()
                        .filter(item -> item.getId().equals(expense.getCategory().getId()))
                                .findFirst();

        preselectedCategory.ifPresent(expenseCategory -> expenseCategoryComboBox.getSelectionModel().select(expenseCategory));

        expenseAmountTextField.setText(expense.getAmount().toString());
        descriptionTextArea.setText(expense.getDescription());
        expenseDatePicker.setValue(expense.getDate());
    }
    /**
     * Inicijalizira ekran.
     */
    public void initialize() {
        travelLogComboBox.getItems().addAll(travelLogRepository.findAll());
        travelLogComboBox.getSelectionModel().select(0);
        travelLogComboBox.setCellFactory(item -> new ListCell<>(){
            @Override
            protected void updateItem(TravelLog travelLog, boolean b) {
                super.updateItem(travelLog, b);
                setText(b || travelLog == null ? "" : travelLog.getDestination() + " (" +
                        CustomDateTimeFormatter.formatDate(travelLog.getStartDate()) + "-" +
                        CustomDateTimeFormatter.formatDate(travelLog.getEndDate()) + ")");
            }
        });
        travelLogComboBox.setButtonCell(new ListCell<>(){
            @Override
            protected void updateItem(TravelLog travelLog, boolean b) {
                super.updateItem(travelLog, b);
                setText(b || travelLog == null ? "" : travelLog.getDestination() + " (" +
                        CustomDateTimeFormatter.formatDate(travelLog.getStartDate()) + "-" +
                        CustomDateTimeFormatter.formatDate(travelLog.getEndDate()) + ")");
            }
        });

        expenseCategoryComboBox.getItems().addAll(expenseCategoryRepository.findAll());
        expenseCategoryComboBox.getSelectionModel().select(0);
        expenseCategoryComboBox.setCellFactory(item -> new ListCell<>(){
            @Override
            protected void updateItem(ExpenseCategory expenseCategory, boolean b) {
                super.updateItem(expenseCategory, b);
                setText(b || expenseCategory == null ? "" : expenseCategory.getName());
            }
        });
        expenseCategoryComboBox.setButtonCell(new ListCell<>(){
            @Override
            protected void updateItem(ExpenseCategory expenseCategory, boolean b) {
                super.updateItem(expenseCategory, b);
                setText(b || expenseCategory == null ? "" : expenseCategory.getName());
            }
        });
    }

    /**
     * Sprema promjene koje je korisnik napravio.
     */
    public void saveChanges(){
        StringBuilder errorMessage = new StringBuilder();

        TravelLog travelLog = travelLogComboBox.getSelectionModel().getSelectedItem();
        ExpenseCategory expenseCategory = expenseCategoryComboBox.getSelectionModel().getSelectedItem();
        String amountString = expenseAmountTextField.getText();

        BigDecimal amount = BigDecimal.ZERO;
        if(amountString != null && !amountString.isEmpty() && DataValidation.isValidDecimalNumber(amountString))
            amount = new BigDecimal(amountString);

        String description = descriptionTextArea.getText();
        LocalDate expenseDate = expenseDatePicker.getValue();

        Expense prevExpenseValue = new Expense(expense);

        if(!hasValidationErrors(errorMessage)){
            updateExistingExpenseObject(travelLog, expenseCategory, amount, description, expenseDate);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Expense");
            dialog.setHeaderText("Are you sure you want to save changes to this expense?");
            dialog.setContentText(expense.toString());

            ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

            Optional<ButtonType> result = dialog.showAndWait();
            result.ifPresent(response ->{
                if (response == saveButtonType){
                    expenseRepository.update(expense);
                    changeLogRepository.log(new ChangeLog<>(prevExpenseValue, expense));
                }
            });
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error while editing the expense");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
        }
    }

    /**
     * Ažurira postojeći objekt troška
     * @param travelLog bilješka putovanja
     * @param expenseCategory kategorija troška
     * @param amount novčani iznos
     * @param description opis
     * @param expenseDate datum troška
     */
    private void updateExistingExpenseObject(TravelLog travelLog, ExpenseCategory expenseCategory, BigDecimal amount, String description, LocalDate expenseDate){
        if(travelLog != null && !expense.getTravelLog().getId().equals(travelLog.getId()))
            expense.setTravelLog(travelLog);
        if(expenseCategory != null && !expense.getCategory().getId().equals(expenseCategory.getId()))
            expense.setCategory(expenseCategory);
        if(expense.getAmount().compareTo(amount) != 0)
            expense.setAmount(amount);
        if(!expense.getDescription().equals(description))
            expense.setDescription(description);
        if(!expense.getDate().equals(expenseDate))
            expense.setDate(expenseDate);
    }
    /**
     * Validira sve unose.
     * @param errorMessage objekt klase StringBuilder koji predstavlja poruku o pogreškama
     * @return boolean koji predstavlja valjanost unosa
     */
    private boolean hasValidationErrors(StringBuilder errorMessage) {
        validateTravelLog(errorMessage);
        validateExpenseCategory(errorMessage);
        validateAmount(errorMessage);
        validateDescription(errorMessage);
        validateExpenseDate(errorMessage);

        return !errorMessage.isEmpty();
    }

    /**
     * Validira zapis putovanja
     * @param errorMessage objekt klase StringBuilder koji predstavlja poruku o pogreškama
     */
    private void validateTravelLog(StringBuilder errorMessage) {
        TravelLog travelLog = travelLogComboBox.getSelectionModel().getSelectedItem();
        if (travelLog == null) errorMessage.append(ErrorMessage.TRAVEL_LOG_REQUIRED.getMessage());
    }

    /**
     * Validira kategoriju troška.
     * @param errorMessage objekt klase StringBuilder koji predstavlja poruku o pogreškama
     */
    private void validateExpenseCategory(StringBuilder errorMessage) {
        ExpenseCategory expenseCategory = expenseCategoryComboBox.getSelectionModel().getSelectedItem();
        if (expenseCategory == null) errorMessage.append(ErrorMessage.EXPENSE_CATEGORY_REQUIRED.getMessage());
    }

    /**
     * Validira novčani iznos.
     * @param errorMessage objekt klase StringBuilder koji predstavlja poruku o pogreškama
     */
    private void validateAmount(StringBuilder errorMessage) {
        String amountString = expenseAmountTextField.getText();
        if (amountString == null || amountString.isEmpty() || !DataValidation.isValidDecimalNumber(amountString)) {
            errorMessage.append(ErrorMessage.AMOUNT_INPUT_ERROR.getMessage());
        }
    }

    /**
     * Validira opis.
     * @param errorMessage objekt klase StringBuilder koji predstavlja poruku o pogreškama
     */
    private void validateDescription(StringBuilder errorMessage) {
        String description = descriptionTextArea.getText();
        if (description.isEmpty()) errorMessage.append(ErrorMessage.DESCRIPTION_REQUIRED.getMessage());
    }

    /**
     * Validira datum troška.
     * @param errorMessage objekt klase StringBuilder koji predstavlja poruku o pogreškama
     */
    private void validateExpenseDate(StringBuilder errorMessage) {
        LocalDate expenseDate = expenseDatePicker.getValue();
        if (expenseDate == null) errorMessage.append(ErrorMessage.DATE_REQUIRED.getMessage());
    }
}
