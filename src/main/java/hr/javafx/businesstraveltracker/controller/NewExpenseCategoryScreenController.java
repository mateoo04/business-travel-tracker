package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.enums.ErrorMessage;
import hr.javafx.businesstraveltracker.model.Employee;
import hr.javafx.businesstraveltracker.model.ExpenseCategory;
import hr.javafx.businesstraveltracker.repository.ExpenseCategoryRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class NewExpenseCategoryScreenController {

    @FXML
    public TextField categoryNameTextField;

    @FXML
    public TextArea categoryDescriptionTextArea;

    private final ExpenseCategoryRepository expenseCategoryRepository = new ExpenseCategoryRepository();

    public void createExpenseCategory() {
        StringBuilder errorMessage = new StringBuilder();

        String categoryName = categoryNameTextField.getText();
        if (categoryName.isEmpty()) errorMessage.append(ErrorMessage.NAME_REQUIRED.getMessage());

        String categoryDescription = categoryDescriptionTextArea.getText();
        if (categoryDescription.isEmpty()) errorMessage.append(ErrorMessage.DESCRIPTION_REQUIRED.getMessage());

        if (errorMessage.isEmpty()) {
            ExpenseCategory expenseCategory = new ExpenseCategory(categoryName, categoryDescription);

            expenseCategoryRepository.save(expenseCategory);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("New Expense Category");
            alert.setHeaderText("Expense Category Added Successfully");
            alert.setContentText(expenseCategory.toString());
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error while adding a new expense category");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
        }
    }
}
