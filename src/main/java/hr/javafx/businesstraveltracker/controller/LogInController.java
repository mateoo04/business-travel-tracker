package hr.javafx.businesstraveltracker.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LogInController {

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField passwordTextField;

    public void logIn(){
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
    }

}
