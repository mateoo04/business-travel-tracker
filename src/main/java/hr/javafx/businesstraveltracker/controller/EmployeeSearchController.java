package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.enums.Department;
import hr.javafx.businesstraveltracker.model.Employee;
import hr.javafx.businesstraveltracker.repository.EmployeeRepository;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;
import java.util.stream.Collectors;

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
    public ComboBox<Department> departmentComboBox;

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

    private final EmployeeRepository employeeRepository = new EmployeeRepository();

    public void initialize() {
        departmentComboBox.getItems().add(null);
        departmentComboBox.getItems().addAll(Department.values());
        departmentComboBox.getSelectionModel().select(0);
        departmentComboBox.setCellFactory(department -> new ListCell<Department>(){
            @Override
            protected void updateItem(Department department, boolean empty) {
                super.updateItem(department, empty);
                setText(empty || department == null ? "" : department.getName());
            }
        });
        departmentComboBox.setButtonCell(new ListCell<Department>(){
            @Override
            protected void updateItem(Department department, boolean empty) {
                super.updateItem(department, empty);
                setText(empty || department == null ? "" : department.getName());
            }
        });

        idColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        firstNameColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getFirstName()));
        lastNameColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getLastName()));
        roleColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getRole()));
        departmentColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDepartment().getName()));
        emailColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getEmail()));
    }

    public void filterEmployees() {
        List<Employee> employees = employeeRepository.findAll();

        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String role = roleTextField.getText();
        String email = emailTextField.getText();
        Department department = departmentComboBox.getValue();

        employees = employees.stream()
                .filter(employee -> firstName.isEmpty() || employee.getFirstName().toLowerCase().contains(firstName.toLowerCase()))
                .filter(employee -> lastName.isEmpty() || employee.getLastName().toLowerCase().contains(lastName.toLowerCase()))
                .filter(employee -> role.isEmpty() || employee.getRole().toLowerCase().contains(role.toLowerCase()))
                .filter(employee -> email.isEmpty() || employee.getEmail().toLowerCase().contains(email.toLowerCase()))
                .filter(employee -> department == null || employee.getDepartment().getId().equals(department.getId()))
                .toList();

        employeeTableView.setItems(FXCollections.observableList(employees));
    }
}
