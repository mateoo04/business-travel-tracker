package hr.javafx.businesstraveltracker.controller;

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
import java.util.Optional;

/**
 * Kontroler za uređivanje zapisa putovanja.
 */
public class EditTravelLogController {
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

    private TravelLog travelLog;

    /**
     * Postavlja objekt koji će biti uređivan.
     * @param travelLog objekt koji će biti uređivan
     */
    public void initData(TravelLog travelLog) {
        this.travelLog = travelLog;

        Optional<Employee> employee = employeeComboBox.getItems().stream()
                .filter(item -> item.getId().equals(travelLog.getEmployee().getId()))
                .findFirst();

        employee.ifPresent(value -> employeeComboBox.getSelectionModel().select(value));

        destinationTextField.setText(travelLog.getDestination());
        startDatePicker.setValue(travelLog.getStartDate());
        endDatePicker.setValue(travelLog.getEndDate());
        statusComboBox.setValue(travelLog.getStatus());
    }
    /**
     * Inicijalizira ekran.
     */
    public void initialize(){
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
     * Sprema promjene koje je korisnik napravio.
     */
    public void saveChanges() {
        StringBuilder errorMessage = validateFields();

        if (errorMessage.isEmpty()) {
            showConfirmationDialog();
        } else {
            showErrorDialog(errorMessage.toString());
        }
    }

    /**
     * Validira polja unosa
     * @return StringBuilder za poruku s pogreškama kod unosa.
     */
    private StringBuilder validateFields() {
        StringBuilder errorMessage = new StringBuilder();

        Employee employee = employeeComboBox.getSelectionModel().getSelectedItem();
        if (employee == null) errorMessage.append(ErrorMessage.EMPLOYEE_REQUIRED.getMessage());

        String destination = destinationTextField.getText();
        if (destination.isEmpty()) errorMessage.append(ErrorMessage.DESTINATION_REQUIRED.getMessage());

        LocalDate startDate = startDatePicker.getValue();
        if (startDate == null) errorMessage.append(ErrorMessage.START_DATE_REQUIRED.getMessage());

        LocalDate endDate = endDatePicker.getValue();
        if (endDate == null) errorMessage.append(ErrorMessage.END_DATE_REQUIRED.getMessage());

        TripStatus status = statusComboBox.getSelectionModel().getSelectedItem();
        if (status == null) errorMessage.append(ErrorMessage.TRIP_STATUS_REQUIRED.getMessage());

        return errorMessage;
    }

    /**
     * Prikazuje dijalog potvrde ažuriranja.
     */
    private void showConfirmationDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Travel Log");
        dialog.setHeaderText("Are you sure you want to save changes to this travel log?");
        dialog.setContentText(travelLog.toString());

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == saveButtonType) {
            updateTravelLog();
        }
    }

    /**
     * Šalje zahtjev za ažuriranje u repozitorij.
     */
    private void updateTravelLog() {
        TravelLog prevTravelLog = new TravelLog(travelLog);

        Employee employee = employeeComboBox.getSelectionModel().getSelectedItem();
        String destination = destinationTextField.getText();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        TripStatus status = statusComboBox.getSelectionModel().getSelectedItem();

        if (employee != null && !travelLog.getEmployee().getId().equals(employee.getId())) {
            travelLog.setEmployee(employee);
        }
        if (!travelLog.getDestination().equals(destination)) travelLog.setDestination(destination);
        if (!travelLog.getStartDate().equals(startDate)) travelLog.setStartDate(startDate);
        if (!travelLog.getEndDate().equals(endDate)) travelLog.setEndDate(endDate);
        if (!travelLog.getStatus().equals(status)) travelLog.setStatus(status);

        travelLogRepository.update(travelLog);
        changeLogRepository.log(new ChangeLog<>(prevTravelLog, travelLog));
    }

    /**
     * Prikazuje dijalog pogreškaka.
     * @param errorMessage poruka o pogreškama
     */
    private void showErrorDialog(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error while editing the travel log");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

}
