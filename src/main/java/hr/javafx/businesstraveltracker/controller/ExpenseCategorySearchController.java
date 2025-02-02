package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.model.ExpenseCategory;
import hr.javafx.businesstraveltracker.repository.ExpenseCategoryRepository;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.List;

public class ExpenseCategorySearchController {

    @FXML
    public TextField nameTextField;

    @FXML
    public TextArea descriptionTextArea;

    @FXML
    public TableView<ExpenseCategory> expenseCategoryTableView;

    @FXML
    public TableColumn<ExpenseCategory, Long> idTableColumn;

    @FXML
    public TableColumn<ExpenseCategory, String> nameTableColumn;

    @FXML
    public TableColumn<ExpenseCategory, String> descriptionTableColumn;

    private final ExpenseCategoryRepository expenseCategoryRepository = new ExpenseCategoryRepository();

    public void initialize() {
        idTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        nameTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getName()));
        descriptionTableColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDescription()));
    }

    public void filterExpenseCategories(){
        List<ExpenseCategory> expenseCategories = expenseCategoryRepository.findAll();

        String name = nameTextField.getText();
        String description = descriptionTextArea.getText();

        expenseCategories = expenseCategories.stream()
                .filter(category -> name.isEmpty() || category.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(category -> description.isEmpty() || category.getDescription().toLowerCase().contains(description.toLowerCase()))
                .toList();

        expenseCategoryTableView.setItems(FXCollections.observableList(expenseCategories));
    }
}
