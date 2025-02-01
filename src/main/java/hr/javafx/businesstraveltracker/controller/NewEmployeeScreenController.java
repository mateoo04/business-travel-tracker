package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.enums.Department;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class NewEmployeeScreenController {

    @FXML
    public TextField firstNameTextField;

    @FXML
    public TextField lastNameTextField;

    @FXML
    public TextField roleTextField;

    @FXML
    public ChoiceBox<Department> departmentChoiceBox;

    @FXML
    public TextField emailTextField;

    public void createEmployee(){

    }
}
