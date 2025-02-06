package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.enums.ErrorMessage;
import hr.javafx.businesstraveltracker.enums.UserPrivileges;
import hr.javafx.businesstraveltracker.model.ChangeLog;
import hr.javafx.businesstraveltracker.model.Employee;
import hr.javafx.businesstraveltracker.model.User;
import hr.javafx.businesstraveltracker.repository.ChangeLogRepository;
import hr.javafx.businesstraveltracker.repository.UserDataRepository;
import hr.javafx.businesstraveltracker.util.ComboBoxSetter;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Optional;

/**
 * Kontrolira ekran za uređivanje korisnika.
 */
public class EditUserScreenController implements EditScreenController<User> {

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

    @FXML
    public CheckBox passwordCheckbox;

    private final UserDataRepository userDataRepository = new UserDataRepository();

    private final ChangeLogRepository changeLogRepository = new ChangeLogRepository();

    private User existingUser;

    public void initData(User user){
        existingUser = user;

        usernameTextField.setText(existingUser.getUsername());

        Optional<UserPrivileges> optionalUserPrivileges = userPrivilegesComboBox.getItems()
                .stream().filter(item -> item.equals(existingUser.getPrivileges()))
                .findFirst();

        optionalUserPrivileges.ifPresent(preselectedPrivileges -> userPrivilegesComboBox.getSelectionModel().select(preselectedPrivileges));

        if(existingUser.getEmployeeId() != null){
            Optional<Employee> optionalEmployee = employeeComboBox.getItems()
                    .stream().filter(item ->
                            existingUser.getEmployeeId().equals(item.getId())).findFirst();

            optionalEmployee.ifPresent(preselectedEmployee -> employeeComboBox.getSelectionModel().select(preselectedEmployee));
        }
    }
    /**
     * Postavlja ekran.
     */
    public void initialize(){
        userPrivilegesComboBox.getItems().addAll(UserPrivileges.values());
        userPrivilegesComboBox.getSelectionModel().select(0);

        ComboBoxSetter.setEmployeeComboBox(employeeComboBox);

        passwordCheckbox.setSelected(true);
        passwordField.setDisable(true);
        repeatedPasswordField.setDisable(true);
        passwordCheckbox.setOnAction(event ->{
            if(passwordCheckbox.isSelected()){
                passwordField.setDisable(true);
                repeatedPasswordField.setDisable(true);
            }else{
                passwordField.setDisable(false);
                repeatedPasswordField.setDisable(false);
            }
        });
    }
    /**
     * Provjerava unesene podatke i šalje zahtjev za ažuriranjem korisnika u repozitorij.
     */
    public void saveChanges(){
        StringBuilder errorMessage = new StringBuilder();

        String username = usernameTextField.getText();
        if(username.isEmpty()) errorMessage.append(ErrorMessage.USERNAME_REQUIRED);

        String password = passwordField.getText();
        String repeatedPassword = repeatedPasswordField.getText();
        if(!isValidPassword(password,repeatedPassword) && !passwordCheckbox.isSelected() ||
                (!passwordCheckbox.isSelected() && (password.isEmpty() || repeatedPassword.isEmpty()))) {
            errorMessage.append(ErrorMessage.PASSWORD_INVALID_INPUT.getMessage());
        }

        UserPrivileges userPrivileges = userPrivilegesComboBox.getSelectionModel().getSelectedItem();
        if(userPrivileges == null) errorMessage.append(ErrorMessage.USER_PRIVILEGES_REQUIRED);

        Employee employee = employeeComboBox.getSelectionModel().getSelectedItem();

        User previousValue = new User(existingUser);

        if(errorMessage.isEmpty()){
            updateExistingUser(username, password, repeatedPassword, userPrivileges, employee);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("User");
            dialog.setHeaderText("Are you sure you want to save changes to this user?");
            dialog.setContentText(existingUser.toString());

            ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

            Optional<ButtonType> result = dialog.showAndWait();
            result.ifPresent(response -> {
                if(response == saveButtonType){
                    userDataRepository.update(existingUser);
                    changeLogRepository.log(new ChangeLog<>(previousValue, existingUser));
                }
            });
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error while editing the user");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
        }
    }

    /**
     * Ažurira objekt korisnika koji se uređuje.
     * @param username korisničko ime
     * @param password zaporka
     * @param repeatedPassword ponovljena zaporka
     * @param userPrivileges privilegije korisnika
     * @param employee zaposlenik koji je povezan sa korisnikom
     */
    private void updateExistingUser(String username, String password, String repeatedPassword,
                                    UserPrivileges userPrivileges, Employee employee){
        if(!username.equals(existingUser.getUsername())) existingUser.setUsername(username);
        if(isValidPassword(password,repeatedPassword) && !passwordCheckbox.isSelected())
            existingUser.setPassword(password);
        if(!existingUser.getPrivileges().equals(userPrivileges))
            existingUser.setPrivileges(userPrivileges);
        if(!employee.getId().equals(existingUser.getEmployeeId()))
            existingUser.setEmployeeId(employee.getId());
    }

    /**
     * Provjerava valjanost zaporki.
     * @param password zaporka
     * @param repeatedPassword ponovljena zaporka
     * @return boolean koji označava valjanost zaporki
     */
    private boolean isValidPassword(String password, String repeatedPassword){
        return !password.isEmpty() && !repeatedPassword.isEmpty() && password.equals(repeatedPassword) && password.length() >= 6;
    }
}
