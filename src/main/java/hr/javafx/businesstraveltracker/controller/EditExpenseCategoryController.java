package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.enums.ErrorMessage;
import hr.javafx.businesstraveltracker.model.ChangeLog;
import hr.javafx.businesstraveltracker.model.Expense;
import hr.javafx.businesstraveltracker.model.ExpenseCategory;
import hr.javafx.businesstraveltracker.repository.ChangeLogRepository;
import hr.javafx.businesstraveltracker.repository.ExpenseCategoryRepository;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Optional;

public class EditExpenseCategoryController {
    @FXML
    public TextField categoryNameTextField;

    @FXML
    public TextArea categoryDescriptionTextArea;

    private final ExpenseCategoryRepository expenseCategoryRepository = new ExpenseCategoryRepository();

    private ExpenseCategory expenseCategory;

    private final ChangeLogRepository changeLogRepository = new ChangeLogRepository();

    public void initData(ExpenseCategory expenseCategory) {
        this.expenseCategory = expenseCategory;

        categoryNameTextField.setText(expenseCategory.getName());
        categoryDescriptionTextArea.setText(expenseCategory.getDescription());
    }

    public void saveChanges() {
        StringBuilder errorMessage = new StringBuilder();

        String categoryName = categoryNameTextField.getText();
        if (categoryName.isEmpty()) errorMessage.append(ErrorMessage.NAME_REQUIRED.getMessage());

        String categoryDescription = categoryDescriptionTextArea.getText();
        if (categoryDescription.isEmpty()) errorMessage.append(ErrorMessage.DESCRIPTION_REQUIRED.getMessage());

        ExpenseCategory prevExpenseCategoryValue = new ExpenseCategory(expenseCategory);

        if (errorMessage.isEmpty()) {
            if(!expenseCategory.getName().equals(categoryName)) expenseCategory.setName(categoryName);
            if(!expenseCategory.getDescription().equals(categoryDescription)) expenseCategory.setDescription(categoryDescription);

            expenseCategoryRepository.update(expenseCategory);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Expense Category");
            dialog.setHeaderText("Are you sure you want to save changes to this expense category?");
            dialog.setContentText(expenseCategory.toString());

            ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

            Optional<ButtonType> result = dialog.showAndWait();
            result.ifPresent(response ->{
                if (response == saveButtonType){
                    expenseCategoryRepository.update(expenseCategory);
                    changeLogRepository.log(new ChangeLog<>(prevExpenseCategoryValue, expenseCategory));
                }
            });

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error while editing the expense category");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
        }
    }
}
