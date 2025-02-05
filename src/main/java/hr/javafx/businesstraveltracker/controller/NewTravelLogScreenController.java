package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.enums.ChangeLogType;
import hr.javafx.businesstraveltracker.enums.ErrorMessage;
import hr.javafx.businesstraveltracker.enums.TripStatus;
import hr.javafx.businesstraveltracker.model.ChangeLog;
import hr.javafx.businesstraveltracker.model.Employee;
import hr.javafx.businesstraveltracker.model.TravelLog;
import hr.javafx.businesstraveltracker.repository.ChangeLogRepository;
import hr.javafx.businesstraveltracker.repository.EmployeeRepository;
import hr.javafx.businesstraveltracker.repository.TravelLogRepository;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

/**
 * Kontroler za dodavanje novih zabilješki putovanja.
 */
public class NewTravelLogScreenController {
    @FXML
    public ComboBox<Employee> employeeComboBox;

    @FXML
    public TextField destinationTextField;

    @FXML
    public DatePicker startDatePicker;

    @FXML
    public DatePicker endDatePicker;

    @FXML
    public ComboBox<TripStatus> statusComboBox;

    private final EmployeeRepository employeeRepository = new EmployeeRepository();

    private final TravelLogRepository travelLogRepository = new TravelLogRepository();

    private final ChangeLogRepository changeLogRepository = new ChangeLogRepository();
    /**
     * Inicijalizira ekran.
     */
    public void initialize() {
        employeeComboBox.getItems().addAll(employeeRepository.findAll());
        employeeComboBox.getSelectionModel().select(0);
        employeeComboBox.setCellFactory(item -> new ListCell<>() {
            @Override
            protected void updateItem(Employee employee, boolean b) {
                super.updateItem(employee, b);
                setText(b || employee == null ? "" : employee.getFirstName() + " " + employee.getLastName());
            }
        });
        employeeComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Employee employee, boolean b) {
                super.updateItem(employee, b);
                setText(b || employee == null ? "" : employee.getFirstName() + " " + employee.getLastName());
            }
        });

        statusComboBox.getItems().addAll(TripStatus.values());
        statusComboBox.getSelectionModel().select(0);
        statusComboBox.setCellFactory(item -> new ListCell<>(){
            @Override
            protected void updateItem(TripStatus tripStatus, boolean b) {
                super.updateItem(tripStatus, b);
                setText(b || tripStatus == null ? "" : tripStatus.getName());
            }
        });
        statusComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(TripStatus tripStatus, boolean b) {
                super.updateItem(tripStatus, b);
                setText(b || tripStatus == null ? "" : tripStatus.getName());
            }
        });
    }

    /**
     * Zahtjeva unos novih zabilješki putovanja u bazu podataka ukoliko su svi podaci točno uneseni.
     */
    public void createTravelLog(){
        StringBuilder errorMessage = new StringBuilder();

        Employee employee = employeeComboBox.getSelectionModel().getSelectedItem();
        if(employee == null) errorMessage.append(ErrorMessage.EMPLOYEE_REQUIRED.getMessage());

        String destination = destinationTextField.getText();
        if(destination.isEmpty()) errorMessage.append(ErrorMessage.DESTINATION_REQUIRED.getMessage());

        LocalDate startDate = startDatePicker.getValue();
        if(startDate == null) errorMessage.append(ErrorMessage.START_DATE_REQUIRED.getMessage());

        LocalDate endDate = endDatePicker.getValue();
        if(endDate == null) errorMessage.append(ErrorMessage.END_DATE_REQUIRED.getMessage());

        TripStatus status = statusComboBox.getSelectionModel().getSelectedItem();
        if(status == null) errorMessage.append(ErrorMessage.TRIP_STATUS_REQUIRED.getMessage());

        if(errorMessage.isEmpty()){
            TravelLog travelLog = new TravelLog(employee, destination, startDate, endDate, status);
            travelLogRepository.save(travelLog);
            changeLogRepository.log(new ChangeLog<>(travelLog, ChangeLogType.NEW));

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("New Travel Log");
            alert.setHeaderText("Travel Log Added Successfully");
            alert.setContentText(travelLog.toString());
            alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error while adding a new travel log");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
        }
    }
}
