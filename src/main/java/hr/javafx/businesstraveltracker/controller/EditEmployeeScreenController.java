package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.enums.Department;
import hr.javafx.businesstraveltracker.enums.ErrorMessage;
import hr.javafx.businesstraveltracker.model.ChangeLog;
import hr.javafx.businesstraveltracker.model.Employee;
import hr.javafx.businesstraveltracker.repository.ChangeLogRepository;
import hr.javafx.businesstraveltracker.repository.EmployeeRepository;
import hr.javafx.businesstraveltracker.util.ComboBoxSetter;
import hr.javafx.businesstraveltracker.util.DataValidation;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Optional;

/**
 * Kontroler za uređivanje zaposlenika.
 */
public class EditEmployeeScreenController implements EditScreenController<Employee> {

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

    private Employee employee;

    private final ChangeLogRepository changeLogRepository = new ChangeLogRepository();

    /**
     * Postavlja objekt koji će biti uređivan.
     * @param employee zaposlenik koji će biti uređivan
     */
    public void initData(Employee employee) {
        this.employee = employee;
        firstNameTextField.setText(employee.getFirstName());
        lastNameTextField.setText(employee.getLastName());
        roleTextField.setText(employee.getRole());
        departmentComboBox.getSelectionModel().select(employee.getDepartment());
        emailTextField.setText(employee.getEmail());
    }
    /**
     * Inicijalizira ekran.
     */
    public void initialize(){
        ComboBoxSetter.setDepartmentComboBox(departmentComboBox);
    }

    /**
     * Sprema promjene koje je korisnik napravio.
     */
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

    /**
     * Validira sve unose.
     * @param errorMessage objekt klase StringBuilder koji predstavlja poruku o pogreškama
     * @return boolean koji predstavlja valjanost unosa
     */
    private boolean hasValidationErrors(StringBuilder errorMessage) {
        checkField(errorMessage, firstNameTextField.getText(), ErrorMessage.FIRST_NAME_REQUIRED);
        checkField(errorMessage, lastNameTextField.getText(), ErrorMessage.LAST_NAME_REQUIRED);
        checkField(errorMessage, roleTextField.getText(), ErrorMessage.ROLE_REQUIRED);
        checkEmail(errorMessage, emailTextField.getText(), ErrorMessage.INVALID_EMAIL);
        checkDepartment(errorMessage);

        return !errorMessage.isEmpty();
    }

    /**
     * Provjerava je li unos prazan.
     * @param errorMessage objekt klase StringBuilder koji predstavlja poruku o pogreškama
     * @param field vrijednost polja unosa
     * @param error
     */
    private void checkField(StringBuilder errorMessage, String field, ErrorMessage error) {
        if (field.isEmpty()) {
            errorMessage.append(error.getMessage());
        }
    }

    /**
     * Provjerava ispravnost unesenog emaila
     * @param errorMessage objekt klase StringBuilder koji predstavlja poruku o pogreškama
     * @param email email
     * @param error
     */
    private void checkEmail(StringBuilder errorMessage, String email, ErrorMessage error){
        if(!email.isEmpty() && !DataValidation.isValidEmail(email))
            errorMessage.append(error.getMessage());
    }

    /**
     * Provjerava je li ComboBox za izbor odjela prazan.
     * @param errorMessage objekt klase StringBuilder koji predstavlja poruku o pogreškama
     */
    private void checkDepartment(StringBuilder errorMessage) {
        Department department = departmentComboBox.getSelectionModel().getSelectedItem();
        if (department == null) {
            errorMessage.append(ErrorMessage.DEPARTMENT_REQUIRED.getMessage());
        }
    }
}
