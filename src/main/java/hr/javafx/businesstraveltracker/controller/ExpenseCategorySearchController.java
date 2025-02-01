package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.model.ExpenseCategory;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

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

    public void initialize() {}

    public void filterExpenseCategories(){

    }
}
