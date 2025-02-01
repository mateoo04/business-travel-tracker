package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.enums.TripStatus;
import hr.javafx.businesstraveltracker.model.Employee;
import hr.javafx.businesstraveltracker.model.TravelLog;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Date;

public class TravelLogController {

    @FXML
    public ListView<Employee> employeeList;

    @FXML
    public TextField destinationTextField;

    @FXML
    public DatePicker startDatePicker;

    @FXML
    public DatePicker endDatePicker;

    @FXML
    public ChoiceBox<TripStatus> tripStatusChoiceBox;

    @FXML
    public TableView<TravelLog> travelLogTableView;

    @FXML
    public TableColumn<TravelLog, LogInController> idColumn;

    @FXML
    public TableColumn<TravelLog, String> employeeColumn;

    @FXML
    public TableColumn<TravelLog, String> destinationColumn;

    @FXML
    public TableColumn<TravelLog, Date> startDateColumn;

    @FXML
    public TableColumn<TravelLog, Date> endDateColumn;

    @FXML
    public TableColumn<TravelLog, TripStatus> statusColumn;

    public void initialize() {
    }

    public void filterTravelLogs(){}
}
