package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.enums.ChangeLogType;
import hr.javafx.businesstraveltracker.enums.Department;
import hr.javafx.businesstraveltracker.enums.ErrorMessage;
import hr.javafx.businesstraveltracker.model.ChangeLog;
import hr.javafx.businesstraveltracker.model.Employee;
import hr.javafx.businesstraveltracker.repository.ChangeLogRepository;
import hr.javafx.businesstraveltracker.repository.EmployeeRepository;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Optional;

public class EditEmployeeScreenController {

    @FXML
    public TextField firstNameTextField;

    @FXML
    public TextField lastNameTextField;

    @FXML
    public TextField roleTextField;

    @FXML
    public ComboBox<Department> departmentComboBox;

    @FXML
    public TextField emailTextField;

    private final EmployeeRepository employeeRepository = new EmployeeRepository();

    private Employee employee;

    private final ChangeLogRepository changeLogRepository = new ChangeLogRepository();

    public void initData(Employee employee) {
        this.employee = employee;
        firstNameTextField.setText(employee.getFirstName());
        lastNameTextField.setText(employee.getLastName());
        roleTextField.setText(employee.getRole());
        departmentComboBox.getSelectionModel().select(employee.getDepartment());
        emailTextField.setText(employee.getEmail());
    }

    public void initialize(){
        departmentComboBox.getItems().addAll(Department.values());
        departmentComboBox.setCellFactory(department -> new ListCell<>(){
            @Override
            protected void updateItem(Department department, boolean empty) {
                super.updateItem(department, empty);
                setText(empty || department == null ? "" : department.getName());
            }
        });
        departmentComboBox.setButtonCell(new ListCell<>(){
            @Override
            protected void updateItem(Department department, boolean empty) {
                super.updateItem(department, empty);
                setText(empty || department == null ? "" : department.getName());
            }
        });
    }

    public void saveChanges(){
        StringBuilder errorMessage = new StringBuilder();

        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String role = roleTextField.getText();
        Department department = departmentComboBox.getSelectionModel().getSelectedItem();
        String email = emailTextField.getText();

        Employee prevEmployeeValue = new Employee(employee);

        if(!hasValidationErrors(errorMessage)){
            if(!employee.getFirstName().equals(firstName)) employee.setFirstName(firstName);
            if(!employee.getLastName().equals(lastName)) employee.setLastName(lastName);
            if(!employee.getRole().equals(role)) employee.setRole(role);
            if(!employee.getDepartment().equals(department)) employee.setDepartment(department);
            if(!employee.getEmail().equals(email)) employee.setEmail(email);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Employee");
            dialog.setHeaderText("Are you sure you want to save changes to this employee?");
            dialog.setContentText(employee.toString());

            ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

            Optional<ButtonType> result = dialog.showAndWait();
            result.ifPresent(response ->{
                if (response == saveButtonType){
                    employeeRepository.update(employee);
                    changeLogRepository.log(new ChangeLog<>(prevEmployeeValue, employee));
                }
            });
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error while editing the employee");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
        }}

    private boolean hasValidationErrors(StringBuilder errorMessage) {
        checkField(errorMessage, firstNameTextField.getText(), ErrorMessage.FIRST_NAME_REQUIRED);
        checkField(errorMessage, lastNameTextField.getText(), ErrorMessage.LAST_NAME_REQUIRED);
        checkField(errorMessage, roleTextField.getText(), ErrorMessage.ROLE_REQUIRED);
        checkDepartment(errorMessage);

        return !errorMessage.isEmpty();
    }

    private void checkField(StringBuilder errorMessage, String field, ErrorMessage error) {
        if (field.isEmpty()) {
            errorMessage.append(error.getMessage());
        }
    }

    private void checkDepartment(StringBuilder errorMessage) {
        Department department = departmentComboBox.getSelectionModel().getSelectedItem();
        if (department == null) {
            errorMessage.append(ErrorMessage.DEPARTMENT_REQUIRED.getMessage());
        }
    }
}
