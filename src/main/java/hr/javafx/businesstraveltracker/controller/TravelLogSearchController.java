package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.enums.TripStatus;
import hr.javafx.businesstraveltracker.model.Employee;
import hr.javafx.businesstraveltracker.model.TravelLog;
import hr.javafx.businesstraveltracker.repository.EmployeeRepository;
import hr.javafx.businesstraveltracker.repository.TravelLogRepository;
import hr.javafx.businesstraveltracker.util.CustomDateFormatter;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

import java.util.List;

public class TravelLogSearchController {

    @FXML
    public ListView<Employee> employeeListView;

    @FXML
    public TextField destinationTextField;

    @FXML
    public DatePicker startDatePicker;

    @FXML
    public DatePicker endDatePicker;

    @FXML
    public ComboBox<TripStatus> tripStatusComboBox;

    @FXML
    public TableView<TravelLog> travelLogTableView;

    @FXML
    public TableColumn<TravelLog, Long> idColumn;

    @FXML
    public TableColumn<TravelLog, String> employeeColumn;

    @FXML
    public TableColumn<TravelLog, String> destinationColumn;

    @FXML
    public TableColumn<TravelLog, String> startDateColumn;

    @FXML
    public TableColumn<TravelLog, String> endDateColumn;

    @FXML
    public TableColumn<TravelLog, String> statusColumn;

    private final EmployeeRepository employeeRepository = new EmployeeRepository();

    private final TravelLogRepository travelLogRepository = new TravelLogRepository();

    public void initialize() {
        idColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        employeeColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>
                (cellData.getValue().getEmployee().getFirstName() + " " + cellData.getValue().getEmployee().getLastName()));
        destinationColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDestination()));
        startDateColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(CustomDateFormatter.formatDate(cellData.getValue().getStartDate())));
        endDateColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(CustomDateFormatter.formatDate(cellData.getValue().getEndDate())));
        statusColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getStatus().getName()));

        employeeListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        employeeListView.getItems().addAll(employeeRepository.findAll());
        employeeListView.setCellFactory(item -> new ListCell<>() {
            @Override
            protected void updateItem(Employee employee, boolean b) {
                super.updateItem(employee, b);
                setText(b || employee == null ? "" : employee.getFirstName() + " " + employee.getLastName() );
            }
        });

        tripStatusComboBox.getItems().add(null);
        tripStatusComboBox.getItems().addAll(TripStatus.values());
        tripStatusComboBox.setCellFactory(item -> new ListCell<>() {
            @Override
            protected void updateItem(TripStatus tripStatus, boolean b) {
                super.updateItem(tripStatus, b);
                setText(b || tripStatus == null ? "" : tripStatus.getName());
            }
        });
        tripStatusComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(TripStatus tripStatus, boolean b) {
                super.updateItem(tripStatus, b);
                setText(b || tripStatus == null ? "" : tripStatus.getName());
            }
        });
    }

    public void filterTravelLogs(){
        List<TravelLog> travelLogs = travelLogRepository.findAll();

        List<Long> employeeIds = employeeListView.getSelectionModel().getSelectedItems().stream()
                .map(Employee::getId).toList();
        String destination = destinationTextField.getText();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        TripStatus tripStatus = tripStatusComboBox.getSelectionModel().getSelectedItem();

        travelLogs = travelLogs.stream()
                .filter(travelLog -> employeeIds.isEmpty() || employeeIds.contains(travelLog.getEmployee().getId()))
                .filter(travelLog -> destination.isEmpty() || travelLog.getDestination().toLowerCase()
                        .contains(destination.toLowerCase()))
                .filter(travelLog -> startDate == null || travelLog.getStartDate().equals(startDate))
                .filter(travelLog -> endDate == null || travelLog.getEndDate().equals(endDate))
                .filter(travelLog -> tripStatus == null || travelLog.getStatus().getId().equals(tripStatus.getId()))
                .toList();

        travelLogTableView.setItems(FXCollections.observableList(travelLogs));
    }
}
