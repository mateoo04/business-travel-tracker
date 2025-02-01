package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.enums.Department;
import hr.javafx.businesstraveltracker.model.Employee;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class EmployeeSearchController {

    @FXML
    public TextField firstNameTextField;

    @FXML
    public TextField lastNameTextField;

    @FXML
    public TextField roleTextField;

    @FXML
    public TextField emailTextField;

    @FXML
    public ChoiceBox<Department> departmentChoiceBox;

    @FXML
    public TableView<Employee> employeeTableView;

    @FXML
    public TableColumn<Employee, Long> idColumn;

    @FXML
    public TableColumn<Employee, String> firstNameColumn;

    @FXML
    public TableColumn<Employee, String> lastNameColumn;

    @FXML
    public TableColumn<Employee, String> roleColumn;

    @FXML
    public TableColumn<Employee, String> departmentColumn;

    @FXML
    public TableColumn<Employee, String> emailColumn;

    public void initialize() {}

    public void filterEmployees() {}
}
