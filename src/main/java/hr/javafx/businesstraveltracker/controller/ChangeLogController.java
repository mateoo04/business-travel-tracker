package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.enums.ChangeLogType;
import hr.javafx.businesstraveltracker.exception.NoDataFoundException;
import hr.javafx.businesstraveltracker.model.*;
import hr.javafx.businesstraveltracker.repository.ChangeLogRepository;
import hr.javafx.businesstraveltracker.util.CustomDateTimeFormatter;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Kontroler za prikaz zabilješki promjena.
 */
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

    @FXML
    public TableColumn<ChangeLog<Entity>, String> entityColumn;

    private final ChangeLogRepository changeLogRepository  = new ChangeLogRepository();

    /**
     * Inicijalizira ekran.
     */
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
        {   if(cellData.getValue().getType().equals(ChangeLogType.DELETE))
                return new SimpleObjectProperty<>(cellData.getValue().getLogValue().toString());
            else if(cellData.getValue().getPreviousValue() == null)
                return new SimpleObjectProperty<>("");

                return new SimpleObjectProperty<>(cellData.getValue().getPreviousValue().toString());
        });

        newValueColumn.setCellValueFactory(cellData ->
        {   if(cellData.getValue().getLogValue() == null || cellData.getValue().getType().equals(ChangeLogType.DELETE))
                return new SimpleObjectProperty<>("");

            return new SimpleObjectProperty<>(cellData.getValue().getLogValue().toString());
        });

        setUpEntityColumn();

        changeLogTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    /**
     * Filtrira podatke prema parametrima koje je korisnik odredio.
     */
    public void filterChanges(){
        List<ChangeLog<Entity>> changeLogs = null;
        try {
            changeLogs = changeLogRepository.findAll();

            ChangeLogType changeLogType = changeLogTypeComboBox.getSelectionModel().getSelectedItem();

            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();

            changeLogs = changeLogs.stream()
                    .filter(log -> changeLogType == null || log.getType().equals(changeLogType))
                    .filter(log -> startDate == null || log.getDateTime().isAfter(startDate.atStartOfDay()))
                    .filter(log -> endDate == null || log.getDateTime().isBefore(endDate.atStartOfDay()))
                    .toList();

            changeLogTableView.setItems(FXCollections.observableList(changeLogs.reversed()));
        } catch (NoDataFoundException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No data");
            alert.setHeaderText(e.getMessage());
            alert.setContentText("All changes that you make will be logged here.");
            alert.showAndWait();
        }
    }

    /**
     * Uređuje stupac u kojem se nalazi naziv entiteta koji je promijenjen.
     */
    private void setUpEntityColumn(){
        entityColumn.setCellValueFactory(cellData -> {
            String entityName = "Employee";
            if(cellData.getValue().getLogValue() instanceof Expense) entityName = "Expense";
            else if(cellData.getValue().getLogValue() instanceof ExpenseCategory) entityName = "Expense Category";
            else if(cellData.getValue().getLogValue() instanceof TravelLog) entityName = "Travel Log";
            else if(cellData.getValue().getLogValue() instanceof Reimbursement) entityName = "Reimbursement";

            return new SimpleObjectProperty<>(entityName);
        });
    }
}
