package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.enums.UserPrivileges;
import hr.javafx.businesstraveltracker.model.User;
import hr.javafx.businesstraveltracker.repository.UserDataRepository;
import hr.javafx.businesstraveltracker.util.PasswordHasher;
import hr.javafx.businesstraveltracker.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.List;

/**
 * Kontroler za prijavu.
 */
public class LogInController {

    @FXML
    public TextField usernameTextField;

    @FXML
    public PasswordField passwordTextField;

    private final UserDataRepository userDataRepository = new UserDataRepository();

    private final SceneManager sceneManager = SceneManager.getInstance();
    
    private static final String ADMIN = "admin";

    private static User currentUser;
    /**
     * Inicijalizira ekran za prijavu.
     */
    public void initialize() {
        usernameTextField.setOnKeyPressed(this::handleKeyPressed);
        passwordTextField.setOnKeyPressed(this::handleKeyPressed);
    }

    /**
     * Olakšava prijavu upotrebom tipkovnike.
     * @param keyEvent događaj tipkovnice
     */
    private void handleKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ENTER) {
            if(usernameTextField.isFocused())
                passwordTextField.requestFocus();
            else if(passwordTextField.isFocused())
                logIn();
        }
    }

    /**
     * Provodi prijavu pri čemu provjerava unesene podatke.
     */
    public void logIn(){
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        
        if(username.equals(ADMIN) && password.equals(ADMIN)){
            setCurrentUser(new User.Builder(ADMIN, UserPrivileges.HIGH).build());
            
            sceneManager.showEmployeeSearch();
        }else {
            List<User> users = userDataRepository.findAllUsers();

            boolean userFound = false;
            for (User user : users) {
                if (username.equals(user.getUsername())) {
                    userFound = true;
                    if (PasswordHasher.checkPassword(password, user.getHashedPassword())) {
                        setCurrentUser(user);

                        sceneManager.showEmployeeSearch();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Password Mismatch");
                        alert.setHeaderText("Incorrect Password Entered!");
                        alert.showAndWait();
                    }
                    break;
                }
            }
            if (!userFound) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("No such user");
                alert.setHeaderText("User not found!");
                alert.showAndWait();
            }
        }
    }

    /**
     * Postavlja trenutnog korisnika
     * @param user korisnik
     */
    private static void setCurrentUser(User user){
        currentUser = user;
    }

    /**
     * Vraća trenutnog korisnika
     * @return trenutni korisnik
     */
    public static User getCurrentUser() {
        return currentUser;
    }
}
