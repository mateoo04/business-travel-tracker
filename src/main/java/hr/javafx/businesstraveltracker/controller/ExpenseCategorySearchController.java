package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.model.Employee;
import hr.javafx.businesstraveltracker.model.ExpenseCategory;
import hr.javafx.businesstraveltracker.repository.ExpenseCategoryRepository;
import hr.javafx.businesstraveltracker.util.ConfirmDeletionDialog;
import hr.javafx.businesstraveltracker.util.SceneManager;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;
import java.util.Optional;

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

        ContextMenu contextMenu = new ContextMenu();
        MenuItem editItem = new MenuItem("Edit");
        MenuItem deleteItem = new MenuItem("Delete");
        contextMenu.getItems().addAll(editItem,deleteItem);

        editItem.setOnAction(event ->{
            ExpenseCategory selectedCategory = expenseCategoryTableView.getSelectionModel().getSelectedItem();
            if(selectedCategory != null) {
                SceneManager.getInstance().showEditExpenseCategoryScreen(selectedCategory);
            }
        });

        deleteItem.setOnAction(event ->{
            ExpenseCategory selectedCategory = expenseCategoryTableView.getSelectionModel().getSelectedItem();
            if(selectedCategory != null) handleDelete(selectedCategory);
        });

        expenseCategoryTableView.setRowFactory(tv ->{
            TableRow<ExpenseCategory> row = new TableRow<>();
            row.setOnContextMenuRequested(event -> {
                if(!row.isEmpty()){
                    expenseCategoryTableView.getSelectionModel().select(row.getItem());
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                }
            });

            return row;
        });
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

    public void handleDelete(ExpenseCategory expenseCategory){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Delete Employee");
        dialog.setHeaderText("Are you sure you want to delete the employee?");
        ConfirmDeletionDialog.show(expenseCategory, dialog,
                () -> expenseCategoryRepository.deleteById(expenseCategory.getId()));

        filterExpenseCategories();
    }
}
