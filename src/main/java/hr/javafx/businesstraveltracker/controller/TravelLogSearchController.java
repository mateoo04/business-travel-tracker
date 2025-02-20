package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.enums.ChangeLogType;
import hr.javafx.businesstraveltracker.enums.TripStatus;
import hr.javafx.businesstraveltracker.enums.UserPrivileges;
import hr.javafx.businesstraveltracker.model.ChangeLog;
import hr.javafx.businesstraveltracker.model.Employee;
import hr.javafx.businesstraveltracker.model.TravelLog;
import hr.javafx.businesstraveltracker.repository.ChangeLogRepository;
import hr.javafx.businesstraveltracker.repository.EmployeeRepository;
import hr.javafx.businesstraveltracker.repository.TravelLogRepository;
import hr.javafx.businesstraveltracker.util.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.List;

public class TravelLogSearchController {

    @FXML
    private ListView<Employee> employeeListView;

    @FXML
    private TextField destinationTextField;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private ComboBox<TripStatus> tripStatusComboBox;

    @FXML
    private TableView<TravelLog> travelLogTableView;

    @FXML
    private TableColumn<TravelLog, Long> idColumn;

    @FXML
    private TableColumn<TravelLog, String> employeeColumn;

    @FXML
    private TableColumn<TravelLog, String> destinationColumn;

    @FXML
    private TableColumn<TravelLog, String> startDateColumn;

    @FXML
    private TableColumn<TravelLog, String> endDateColumn;

    @FXML
    private TableColumn<TravelLog, String> statusColumn;

    private final EmployeeRepository employeeRepository = new EmployeeRepository();

    private final TravelLogRepository travelLogRepository = new TravelLogRepository();

    private final ChangeLogRepository changeLogRepository = new ChangeLogRepository();

    /**
     * Inicijalizira ekran.
     */
    public void initialize() {
        idColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        employeeColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>
                (cellData.getValue().getEmployee().getFirstName() + " " + cellData.getValue().getEmployee().getLastName()));
        destinationColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDestination()));
        startDateColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(CustomDateTimeFormatter.formatDate(cellData.getValue().getStartDate())));
        endDateColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(CustomDateTimeFormatter.formatDate(cellData.getValue().getEndDate())));
        statusColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getStatus().getName()));

        employeeListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        employeeListView.getItems().addAll(employeeRepository.findAll());
        employeeListView.setCellFactory(item -> new ListCell<>() {
            @Override
            protected void updateItem(Employee employee, boolean b) {
                super.updateItem(employee, b);
                setText(b || employee == null ? "" : employee.getFirstName() + " " + employee.getLastName());
            }
        });

        tripStatusComboBox.getItems().add(null);
        ComboBoxSetter.setTripStatusComboBox(tripStatusComboBox);

        travelLogTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        setContextMenuOnRowItemsOnRowItems();
    }

    /**
     * Filtrira podatke prema parametrima koje je korisnik odredio.
     */
    public void filterTravelLogs() {
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
                .filter(travelLog -> startDate == null || travelLog.getStartDate().equals(startDate) ||
                        travelLog.getStartDate().isAfter(startDate))
                .filter(travelLog -> endDate == null || travelLog.getEndDate().equals(endDate) ||
                        travelLog.getEndDate().isBefore(endDate))
                .filter(travelLog -> tripStatus == null || travelLog.getStatus().getId().equals(tripStatus.getId()))
                .toList();

        travelLogTableView.setItems(FXCollections.observableList(travelLogs));
    }

    /**
     * Postavlja ContextMenu izbornik koji se prikazuje klikom sekundarnog gumba miša na redak u tablici.
     */
    private void setContextMenuOnRowItemsOnRowItems() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem editItem = new MenuItem("Edit");
        MenuItem deleteItem = new MenuItem("Delete");
        contextMenu.getItems().addAll(editItem, deleteItem);

        editItem.setOnAction(event -> {
            TravelLog selectedTravelLog = travelLogTableView.getSelectionModel().getSelectedItem();
            if (selectedTravelLog != null && (LogInController.getCurrentUser().getPrivileges().equals(UserPrivileges.HIGH) ||
                    LogInController.getCurrentUser().getEmployeeId().equals(selectedTravelLog.getEmployee().getId()))) {
                SceneManager.getInstance().showEditTravelLogScreen(selectedTravelLog);
            } else if (selectedTravelLog != null) UnauthorisedAlert.show();
        });

        deleteItem.setOnAction(event -> {
            TravelLog selectedTravelLog = travelLogTableView.getSelectionModel().getSelectedItem();
            if (selectedTravelLog != null && (LogInController.getCurrentUser().getPrivileges().equals(UserPrivileges.HIGH) ||
                    LogInController.getCurrentUser().getEmployeeId().equals(selectedTravelLog.getEmployee().getId()))) {
                handleDelete(selectedTravelLog);
            } else if (selectedTravelLog != null) UnauthorisedAlert.show();
        });

        travelLogTableView.setRowFactory(tv -> {
            TableRow<TravelLog> row = new TableRow<>();
            row.setOnContextMenuRequested(event -> {
                if (!row.isEmpty()) {
                    travelLogTableView.getSelectionModel().select(row.getItem());
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                }
            });

            return row;
        });
    }

    public void handleDelete(TravelLog travelLog) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Delete Travel Log");
        dialog.setHeaderText("Are you sure you want to delete the travel log?");
        ConfirmDeletionDialog.show(travelLog, dialog,
                () -> travelLogRepository.deleteById(travelLog.getId()));

        changeLogRepository.log(new ChangeLog<>(travelLog, ChangeLogType.DELETE));

        filterTravelLogs();
    }
}
