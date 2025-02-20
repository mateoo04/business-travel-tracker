package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.enums.ChangeLogType;
import hr.javafx.businesstraveltracker.enums.ErrorMessage;
import hr.javafx.businesstraveltracker.enums.UserPrivileges;
import hr.javafx.businesstraveltracker.model.ChangeLog;
import hr.javafx.businesstraveltracker.model.Employee;
import hr.javafx.businesstraveltracker.model.User;
import hr.javafx.businesstraveltracker.repository.ChangeLogRepository;
import hr.javafx.businesstraveltracker.repository.EmployeeRepository;
import hr.javafx.businesstraveltracker.repository.UserDataRepository;
import hr.javafx.businesstraveltracker.util.ComboBoxSetter;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.List;

/**
 * Kontrolira dodavanje novog korisnika.
 */
public class NewUserScreenController {

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField repeatedPasswordField;

    @FXML
    private ComboBox<UserPrivileges> userPrivilegesComboBox;

    @FXML
    private ComboBox<Employee> employeeComboBox;

    private final UserDataRepository userDataRepository = new UserDataRepository();

    private final EmployeeRepository employeeRepository = new EmployeeRepository();

    private final ChangeLogRepository changeLogRepository = new ChangeLogRepository();

    /**
     * Postavlja ekran.
     */
    public void initialize(){
        userPrivilegesComboBox.getItems().addAll(UserPrivileges.values());
        userPrivilegesComboBox.getSelectionModel().select(0);

        adjustEmployeeComboBox();
    }

    /**
     * Postavlja ComboBox za izbor zaposlenika.
     */
    private void adjustEmployeeComboBox(){
        ComboBoxSetter.setEmployeeComboBox(employeeComboBox);

        List<Long> usersByEmployeeId = userDataRepository.findAllUsers().stream().map(User::getEmployeeId).toList();
        List<Employee> employeesWithoutAssociatedUser = employeeRepository.findAll().stream()
                        .filter(employee -> !usersByEmployeeId.contains(employee.getId()))
                        .toList();

        employeeComboBox.setItems(FXCollections.observableArrayList(employeesWithoutAssociatedUser));
    }

    /**
     * Provjerava unesene podatke i šalje zahtjev za dodavanjem korisnika u repozitorij.
     */
    public void createUser(){
        StringBuilder errorMessage = new StringBuilder();

        String username = usernameTextField.getText();
        if(username == null) errorMessage.append(ErrorMessage.USERNAME_REQUIRED.getMessage());

        String password = passwordField.getText();
        String repeatedPassword = repeatedPasswordField.getText();
        if(password.isEmpty() || repeatedPassword.isEmpty() || !password.equals(repeatedPassword) || password.length() < 6)
            errorMessage.append(ErrorMessage.PASSWORD_INVALID_INPUT.getMessage());

        UserPrivileges userPrivileges = userPrivilegesComboBox.getSelectionModel().getSelectedItem();
        if(userPrivileges == null) errorMessage.append(ErrorMessage.USER_PRIVILEGES_REQUIRED.getMessage());

        Employee employee = employeeComboBox.getSelectionModel().getSelectedItem();

        if(errorMessage.isEmpty()){
            User.Builder userBuilder = new User.Builder(username, userPrivileges).withPassword(password);
            if(employee != null) userBuilder.withEmployeeId(employee.getId());
            User user = userBuilder.build();

            userDataRepository.save(user);
            changeLogRepository.log(new ChangeLog<>(user, ChangeLogType.NEW));

            adjustEmployeeComboBox();

            usernameTextField.clear();
            passwordField.clear();
            repeatedPasswordField.clear();
            userPrivilegesComboBox.getSelectionModel().select(0);
            employeeComboBox.getSelectionModel().select(0);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("New User");
            alert.setHeaderText("User Added Successfully");
            alert.setContentText(user.toString());
            alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error while adding a new user");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
        }
    }
}
