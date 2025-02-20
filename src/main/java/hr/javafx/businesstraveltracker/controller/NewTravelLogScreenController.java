package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.enums.ChangeLogType;
import hr.javafx.businesstraveltracker.enums.ErrorMessage;
import hr.javafx.businesstraveltracker.enums.TripStatus;
import hr.javafx.businesstraveltracker.enums.UserPrivileges;
import hr.javafx.businesstraveltracker.model.ChangeLog;
import hr.javafx.businesstraveltracker.model.Employee;
import hr.javafx.businesstraveltracker.model.TravelLog;
import hr.javafx.businesstraveltracker.repository.ChangeLogRepository;
import hr.javafx.businesstraveltracker.repository.EmployeeRepository;
import hr.javafx.businesstraveltracker.repository.TravelLogRepository;
import hr.javafx.businesstraveltracker.util.ComboBoxSetter;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Kontroler za dodavanje novih zabilješki putovanja.
 */
public class NewTravelLogScreenController {
    @FXML
    private ComboBox<Employee> employeeComboBox;

    @FXML
    private TextField destinationTextField;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private ComboBox<TripStatus> statusComboBox;

    private final EmployeeRepository employeeRepository = new EmployeeRepository();

    private final TravelLogRepository travelLogRepository = new TravelLogRepository();

    private final ChangeLogRepository changeLogRepository = new ChangeLogRepository();
    /**
     * Inicijalizira ekran.
     */
    public void initialize() {
        setEmployeeComboBox();
        ComboBoxSetter.setTripStatusComboBox(statusComboBox);
    }

    /**
     * Postavlja Employee ComboBox dodavajući raličite elemente obzirom tko je korisnik.
     */
    private void setEmployeeComboBox(){
        if(LogInController.getCurrentUser().getPrivileges().equals(UserPrivileges.LOW) &&
                LogInController.getCurrentUser().getEmployeeId() != null) {
            Optional<Employee> employeeOptional = employeeRepository.findById(LogInController.getCurrentUser().getEmployeeId());
            employeeOptional.ifPresent(employee -> employeeComboBox.getItems().add(employee));
        }
        else employeeComboBox.getItems().addAll(employeeRepository.findAll());

        employeeComboBox.getSelectionModel().select(0);
        ComboBoxSetter.setCellFactoriesOnEmployeeComboBox(employeeComboBox);
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

        if(startDate != null && endDate != null && startDate.isAfter(endDate))
            errorMessage.append(ErrorMessage.INVALID_DATE_RANGE.getMessage());

        TripStatus status = statusComboBox.getSelectionModel().getSelectedItem();
        if(status == null) errorMessage.append(ErrorMessage.TRIP_STATUS_REQUIRED.getMessage());

        if(errorMessage.isEmpty()){
            TravelLog travelLog = new TravelLog(employee, destination, startDate, endDate, status);
            travelLogRepository.save(travelLog);
            changeLogRepository.log(new ChangeLog<>(travelLog, ChangeLogType.NEW));

            destinationTextField.clear();
            startDatePicker.setValue(null);
            endDatePicker.setValue(null);

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
