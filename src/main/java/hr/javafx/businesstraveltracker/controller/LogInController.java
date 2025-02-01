package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.enums.UserPrivileges;
import hr.javafx.businesstraveltracker.model.User;
import hr.javafx.businesstraveltracker.repository.UserDataRepository;
import hr.javafx.businesstraveltracker.util.PasswordHasher;
import hr.javafx.businesstraveltracker.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.util.List;

public class LogInController {

    @FXML
    public TextField usernameTextField;

    @FXML
    public TextField passwordTextField;

    private final UserDataRepository userDataRepository = new UserDataRepository();

    private final SceneManager sceneManager = SceneManager.getInstance();

    private static User currentUser;

    public void logIn(){
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        List<User> users = userDataRepository.findAllUsers();

        boolean userFound = false;
        for(User user : users){
            if(username.equals(user.username())){
                userFound = true;
                if(PasswordHasher.checkPassword(password, user.hashedPassword())){
                    System.out.println("Logged in!");
                    currentUser = user;

                    sceneManager.showEmployeeSearch();
                }else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Password Mismatch");
                    alert.setHeaderText("Incorrect Password Entered!");
                    alert.showAndWait();
                }
                break;
            }
        }
        if(!userFound){
            User newUser = new User(username, PasswordHasher.hashPassword(password), UserPrivileges.LOW);
            userDataRepository.save(newUser);
            currentUser = newUser;

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("New User Created");
            alert.setHeaderText("Welcome " + username + "!");
            alert.showAndWait();

            sceneManager.showEmployeeSearch();
        }
    }

    public static User getCurrentUser() {
        return currentUser;
    }
}
