package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.enums.ChangeLogType;
import hr.javafx.businesstraveltracker.model.ChangeLog;
import hr.javafx.businesstraveltracker.model.Entity;
import hr.javafx.businesstraveltracker.repository.ChangeLogRepository;
import hr.javafx.businesstraveltracker.util.CustomDateTimeFormatter;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.List;

public class ChangeLogController {
    @FXML
    public ComboBox<ChangeLogType> changeLogTypeComboBox;

    @FXML
    public DatePicker startDatePicker;

    @FXML
    public DatePicker endDatePicker;

    @FXML
    public TableView<ChangeLog<Entity>> changeLogTableView;

    @FXML
    public TableColumn<ChangeLog<Entity>, String> dateTimeColumn;

    @FXML
    public TableColumn<ChangeLog<Entity>, String> changeLogTypeColumn;

    @FXML
    public TableColumn<ChangeLog<Entity>, String> userColumn;

    @FXML
    public TableColumn<ChangeLog<Entity>, String> previousValueColumn;

    @FXML
    public TableColumn<ChangeLog<Entity>, String> newValueColumn;

    private final ChangeLogRepository changeLogRepository  = new ChangeLogRepository();

    public void initialize(){
        changeLogTypeComboBox.getItems().add(null);
        changeLogTypeComboBox.getItems().addAll(ChangeLogType.values());
        changeLogTypeComboBox.getSelectionModel().select(0);
        changeLogTypeComboBox.setCellFactory(item -> new ListCell<ChangeLogType>() {
            @Override
            protected void updateItem(ChangeLogType changeLogType, boolean b) {
                super.updateItem(changeLogType, b);
                setText(b || changeLogType == null ? "" : changeLogType.getName());
            }
        });
        changeLogTypeComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(ChangeLogType changeLogType, boolean b) {
                super.updateItem(changeLogType, b);
                setText(b || changeLogType == null ? "" : changeLogType.getName());
            }
        });

        dateTimeColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(CustomDateTimeFormatter.formatDateTime(cellData.getValue().getDateTime())));

        changeLogTypeColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getType().getName()));

        userColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getUser().username()));

        previousValueColumn.setCellValueFactory(cellData ->
        {   if(cellData.getValue().getPreviousValue() == null) return new SimpleObjectProperty<>("");
            return new SimpleObjectProperty<>(cellData.getValue().getPreviousValue().toString());
        });

        newValueColumn.setCellValueFactory(cellData ->
        {   if(cellData.getValue().getLogValue() == null) return new SimpleObjectProperty<>("");
            return new SimpleObjectProperty<>(cellData.getValue().getLogValue().toString());
        });

    }

    public void filterChanges(){
        List<ChangeLog<Entity>> changeLogs = changeLogRepository.findAll();

        ChangeLogType changeLogType = changeLogTypeComboBox.getSelectionModel().getSelectedItem();

        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        changeLogs = changeLogs.stream()
                .filter(log -> changeLogType == null || log.getType().equals(changeLogType))
                .filter(log -> startDate == null || log.getDateTime().isAfter(startDate.atStartOfDay()))
                .filter(log -> endDate == null || log.getDateTime().isBefore(endDate.atStartOfDay()))
                .toList();

        changeLogTableView.setItems(FXCollections.observableList(changeLogs.reversed()));
    }
}
