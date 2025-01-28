package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.enums.TripStatus;
import hr.javafx.businesstraveltracker.model.Employee;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class NewTravelLogScreenController {
    @FXML
    public ChoiceBox<Employee> employeeChoiceBox;

    @FXML
    public TextField destinationTextField;

    @FXML
    public DatePicker startDatePicker;

    @FXML
    public DatePicker endDatePicker;

    @FXML
    public ChoiceBox<TripStatus> statusChoiceBox;

    public void createTravelLog(){

    }
}
