package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.enums.ChangeLogType;
import hr.javafx.businesstraveltracker.enums.Department;
import hr.javafx.businesstraveltracker.enums.UserPrivileges;
import hr.javafx.businesstraveltracker.model.ChangeLog;
import hr.javafx.businesstraveltracker.model.Employee;
import hr.javafx.businesstraveltracker.repository.ChangeLogRepository;
import hr.javafx.businesstraveltracker.repository.EmployeeRepository;
import hr.javafx.businesstraveltracker.repository.UserDataRepository;
import hr.javafx.businesstraveltracker.util.ComboBoxSetter;
import hr.javafx.businesstraveltracker.util.ConfirmDeletionDialog;
import hr.javafx.businesstraveltracker.util.SceneManager;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

/**
 * Kontroler za pretraživanje zaposlenika.
 */
public class EmployeeSearchController {

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField roleTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private ComboBox<Department> departmentComboBox;

    @FXML
    private TableView<Employee> employeeTableView;

    @FXML
    private TableColumn<Employee, Long> idColumn;

    @FXML
    private TableColumn<Employee, String> firstNameColumn;

    @FXML
    private TableColumn<Employee, String> lastNameColumn;

    @FXML
    private TableColumn<Employee, String> roleColumn;

    @FXML
    private TableColumn<Employee, String> departmentColumn;

    @FXML
    private TableColumn<Employee, String> emailColumn;

    private final EmployeeRepository employeeRepository = new EmployeeRepository();

    private final UserDataRepository userDataRepository = new UserDataRepository();

    private final ChangeLogRepository changeLogRepository = new ChangeLogRepository();
    /**
     * Inicijalizira ekran.
     */
    public void initialize() {
        departmentComboBox.getItems().add(null);
        departmentComboBox.getSelectionModel().select(0);
        ComboBoxSetter.setDepartmentComboBox(departmentComboBox);

        idColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        firstNameColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getFirstName()));
        lastNameColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getLastName()));
        roleColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getRole()));
        departmentColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDepartment().getName()));
        emailColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getEmail()));

        employeeTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        if(LogInController.getCurrentUser().getPrivileges().equals(UserPrivileges.HIGH))
            setContextMenuOnRowItems();
    }

    /**
     * Postavlja ContextMenu izbornik koji se prikazuje klikom sekundarnog gumba miša na redak u tablici.
     */
    private void setContextMenuOnRowItems(){
        ContextMenu contextMenu = new ContextMenu();
        MenuItem editItem = new MenuItem("Edit");
        MenuItem deleteItem = new MenuItem("Delete");
        contextMenu.getItems().addAll(editItem,deleteItem);

        editItem.setOnAction(event ->{
            Employee selectedEmployee = employeeTableView.getSelectionModel().getSelectedItem();
            if(selectedEmployee != null) {
                SceneManager.getInstance().showEditEmployeeScreen(selectedEmployee);
            }
        });

        deleteItem.setOnAction(event ->{
            Employee selectedEmployee = employeeTableView.getSelectionModel().getSelectedItem();
            if(selectedEmployee != null) handleDelete(selectedEmployee);
        });

        employeeTableView.setRowFactory(tv ->{
            TableRow<Employee> row = new TableRow<>();
            row.setOnContextMenuRequested(event -> {
                if(!row.isEmpty()){
                    employeeTableView.getSelectionModel().select(row.getItem());
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                }
            });

            return row;
        });
    }

    /**
     * Filtrira podatke prema parametrima koje je korisnik odredio.
     */
    public void filterEmployees() {
        List<Employee> employees = employeeRepository.findAll();

        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String role = roleTextField.getText();
        String email = emailTextField.getText();
        Department department = departmentComboBox.getValue();

        employees = employees.stream()
                .filter(employee -> firstName.isEmpty() || employee.getFirstName().toLowerCase().contains(firstName.toLowerCase()))
                .filter(employee -> lastName.isEmpty() || employee.getLastName().toLowerCase().contains(lastName.toLowerCase()))
                .filter(employee -> role.isEmpty() || employee.getRole().toLowerCase().contains(role.toLowerCase()))
                .filter(employee -> email.isEmpty() || employee.getEmail().toLowerCase().contains(email.toLowerCase()))
                .filter(employee -> department == null || employee.getDepartment().getId().equals(department.getId()))
                .toList();

        employeeTableView.setItems(FXCollections.observableList(employees));
    }

    /**
     * Izvodi brisanje zapisa.
     * @param employee zaposlenik koji će biti izbrisan
     */
    public void handleDelete(Employee employee){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Delete Employee");
        dialog.setHeaderText("Are you sure you want to delete the employee?");
        ConfirmDeletionDialog.show(employee, dialog, () -> {
            employeeRepository.deleteById(employee.getId());
            userDataRepository.deleteByEmployeeId(employee.getId());
        });

        changeLogRepository.log(new ChangeLog<>(employee, ChangeLogType.DELETE));

        filterEmployees();
    }
}
