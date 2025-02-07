package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.enums.ChangeLogType;
import hr.javafx.businesstraveltracker.enums.ErrorMessage;
import hr.javafx.businesstraveltracker.enums.UserPrivileges;
import hr.javafx.businesstraveltracker.model.ChangeLog;
import hr.javafx.businesstraveltracker.model.Employee;
import hr.javafx.businesstraveltracker.model.User;
import hr.javafx.businesstraveltracker.repository.ChangeLogRepository;
import hr.javafx.businesstraveltracker.repository.UserDataRepository;
import hr.javafx.businesstraveltracker.util.ComboBoxSetter;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Kontrolira dodavanje novog korisnika.
 */
public class NewUserScreenController {

    @FXML
    public TextField usernameTextField;

    @FXML
    public PasswordField passwordField;

    @FXML
    public PasswordField repeatedPasswordField;

    @FXML
    public ComboBox<UserPrivileges> userPrivilegesComboBox;

    @FXML
    public ComboBox<Employee> employeeComboBox;

    private final UserDataRepository userDataRepository = new UserDataRepository();

    private final ChangeLogRepository changeLogRepository = new ChangeLogRepository();

    /**
     * Postavlja ekran.
     */
    public void initialize(){
        userPrivilegesComboBox.getItems().addAll(UserPrivileges.values());
        userPrivilegesComboBox.getSelectionModel().select(0);

        ComboBoxSetter.setEmployeeComboBox(employeeComboBox);
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
