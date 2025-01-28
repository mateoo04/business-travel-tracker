package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.enums.Department;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class NewEmployeeScreenController {

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField roleTextField;

    @FXML
    private ChoiceBox<Department> departmentChoiceBox;

    @FXML
    private TextField emailTextField;

    private void createEmployee(){

    }
}
