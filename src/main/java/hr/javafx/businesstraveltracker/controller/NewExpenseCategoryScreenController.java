package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.enums.ChangeLogType;
import hr.javafx.businesstraveltracker.enums.ErrorMessage;
import hr.javafx.businesstraveltracker.model.ChangeLog;
import hr.javafx.businesstraveltracker.model.ExpenseCategory;
import hr.javafx.businesstraveltracker.repository.ChangeLogRepository;
import hr.javafx.businesstraveltracker.repository.ExpenseCategoryRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Kontroler za dodavanje nove kategorije troškova.
 */
public class NewExpenseCategoryScreenController {

    @FXML
    private TextField categoryNameTextField;

    @FXML
    private TextArea categoryDescriptionTextArea;

    private final ExpenseCategoryRepository expenseCategoryRepository = new ExpenseCategoryRepository();

    private final ChangeLogRepository changeLogRepository = new ChangeLogRepository();

    /**
     * Zahtjeva unos nove kategorije troškova u bazu podataka ukoliko su svi podaci točno uneseni.
     */
    public void createExpenseCategory() {
        StringBuilder errorMessage = new StringBuilder();

        String categoryName = categoryNameTextField.getText();
        if (categoryName.isEmpty()) errorMessage.append(ErrorMessage.NAME_REQUIRED.getMessage());

        String categoryDescription = categoryDescriptionTextArea.getText();
        if (categoryDescription.isEmpty()) errorMessage.append(ErrorMessage.DESCRIPTION_REQUIRED.getMessage());

        Set<String> existingCategoryNames =expenseCategoryRepository.findAll().stream()
                .map(ExpenseCategory::getName)
                .collect(Collectors.toSet());

        if(existingCategoryNames.contains(categoryName))
            errorMessage.append("Expense category with name ").append(categoryName).append(" already exists!");

        if (errorMessage.isEmpty()) {
            ExpenseCategory expenseCategory = new ExpenseCategory(categoryName, categoryDescription);

            expenseCategoryRepository.save(expenseCategory);
            changeLogRepository.log(new ChangeLog<>(expenseCategory, ChangeLogType.NEW));

            categoryNameTextField.clear();
            categoryDescriptionTextArea.clear();

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
