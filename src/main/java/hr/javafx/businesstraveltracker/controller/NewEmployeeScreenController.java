package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.enums.ChangeLogType;
import hr.javafx.businesstraveltracker.enums.Department;
import hr.javafx.businesstraveltracker.enums.ErrorMessage;
import hr.javafx.businesstraveltracker.model.ChangeLog;
import hr.javafx.businesstraveltracker.model.Employee;
import hr.javafx.businesstraveltracker.repository.ChangeLogRepository;
import hr.javafx.businesstraveltracker.repository.EmployeeRepository;
import hr.javafx.businesstraveltracker.util.ComboBoxSetter;
import hr.javafx.businesstraveltracker.util.DataValidation;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * Kontroler za dodavanje novih zaposlenika.
 */
public class NewEmployeeScreenController {

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField roleTextField;

    @FXML
    private ComboBox<Department> departmentComboBox;

    @FXML
    private TextField emailTextField;

    private final EmployeeRepository employeeRepository = new EmployeeRepository();

    private final ChangeLogRepository changeLogRepository = new ChangeLogRepository();
    /**
     * Inicijalizira ekran.
     */
    public void initialize() {
        ComboBoxSetter.setDepartmentComboBox(departmentComboBox);
    }

    /**
     * Zahtjeva unos novog zaposlenika u bazu podataka ukoliko su svi podaci točno uneseni.
     */
    public void createEmployee(){
        StringBuilder errorMessage = new StringBuilder();

        String firstName = firstNameTextField.getText();
        if (firstName.isEmpty()) errorMessage.append(ErrorMessage.FIRST_NAME_REQUIRED.getMessage());

        String lastName = lastNameTextField.getText();
        if (lastName.isEmpty()) errorMessage.append(ErrorMessage.LAST_NAME_REQUIRED.getMessage());

        String role = roleTextField.getText();
        if (role.isEmpty()) errorMessage.append(ErrorMessage.ROLE_REQUIRED.getMessage());

        Department department = departmentComboBox.getSelectionModel().getSelectedItem();
        if (department == null) errorMessage.append(ErrorMessage.DEPARTMENT_REQUIRED.getMessage());

        String email = emailTextField.getText();
        if(!email.isEmpty() && !DataValidation.isValidEmail(email))
            errorMessage.append(ErrorMessage.INVALID_EMAIL.getMessage());

        if(errorMessage.isEmpty()){
            Employee.Builder builder = new Employee.Builder(firstName,lastName,role,department);
            if(!email.isEmpty()) builder.withEmail(email);
            Employee employee = builder.build();

            employeeRepository.save(employee);
            changeLogRepository.log(new ChangeLog<>(employee, ChangeLogType.NEW));

            firstNameTextField.clear();
            lastNameTextField.clear();
            emailTextField.clear();
            roleTextField.clear();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("New Employee");
            alert.setHeaderText("Employee Added Successfully");
            alert.setContentText(employee.toString());
            alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error while adding a new employee");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
        }
    }
}
