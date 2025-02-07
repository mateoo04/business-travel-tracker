package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.enums.ChangeLogType;
import hr.javafx.businesstraveltracker.enums.UserPrivileges;
import hr.javafx.businesstraveltracker.model.ChangeLog;
import hr.javafx.businesstraveltracker.model.Employee;
import hr.javafx.businesstraveltracker.model.User;
import hr.javafx.businesstraveltracker.repository.ChangeLogRepository;
import hr.javafx.businesstraveltracker.repository.EmployeeRepository;
import hr.javafx.businesstraveltracker.repository.UserDataRepository;
import hr.javafx.businesstraveltracker.util.ConfirmDeletionDialog;
import hr.javafx.businesstraveltracker.util.SceneManager;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;
import java.util.Optional;

/**
 * Kontrolira pretragu spremljenih korisnika.
 */
public class UserSearchController {

    @FXML
    public ComboBox<UserPrivileges> userPrivilegesComboBox;

    @FXML
    public TableView<User> userTableView;

    @FXML
    public TableColumn<User, String> usernameColumn;

    @FXML
    public TableColumn<User, UserPrivileges> privilegesColumn;

    @FXML
    public TableColumn<User, String> employeeIdColumn;

    @FXML
    public TableColumn<User, String> employeeDetailsColumn;

    @FXML
    public TableColumn<User, Long> userIdColumn;

    private final UserDataRepository userDataRepository = new UserDataRepository();

    private final EmployeeRepository employeeRepository = new EmployeeRepository();

    private final ChangeLogRepository changeLogRepository = new ChangeLogRepository();

    /**
     * Inicijalizira ekran.
     */
    public void initialize(){
        userPrivilegesComboBox.getItems().add(null);
        userPrivilegesComboBox.getItems().addAll(UserPrivileges.values());
        userPrivilegesComboBox.getSelectionModel().select(0);

        userIdColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        usernameColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getUsername()));
        privilegesColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPrivileges()));
        employeeIdColumn.setCellValueFactory(cellData -> {
            if(cellData.getValue().getEmployeeId() != null)
                return new SimpleObjectProperty<>(cellData.getValue().getEmployeeId().toString());

            return  new SimpleObjectProperty<>("No data");
        });

        employeeDetailsColumn.setCellValueFactory(cellData ->{
            if(cellData.getValue().getEmployeeId() != null) {
                Optional<Employee> employeeOptional = employeeRepository.findById(cellData.getValue().getEmployeeId());
                if (employeeOptional.isPresent()) {
                    return new SimpleObjectProperty<>(employeeOptional.get().getFirstName() + " " + employeeOptional.get().getLastName());
                }
            }
            return  new SimpleObjectProperty<>("No data");
        });

        setContextMenuOnRowItems();

        userTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    /**
     * Filtrira korisnike prema parametrima određenima od strane trenutnog korisnika.
     */
    public void filterUsers(){
        List<User> userList = userDataRepository.findAllUsers();

        UserPrivileges userPrivileges = userPrivilegesComboBox.getValue();

        userList = userList.stream().filter(user -> userPrivileges == null || user.getPrivileges().equals(userPrivileges))
                .toList();

        userTableView.setItems(FXCollections.observableList(userList));
    }

    /**
     * Postavlja menu koji se prikazuje klikom sekundarne tipke miša na red tablice.
     */
    private void setContextMenuOnRowItems(){
        ContextMenu contextMenu = new ContextMenu();
        MenuItem editItem = new MenuItem("Edit");
        MenuItem deleteItem = new MenuItem("Delete");
        contextMenu.getItems().addAll(editItem,deleteItem);

        editItem.setOnAction(event ->{
            User selectedUser = userTableView.getSelectionModel().getSelectedItem();
            if(selectedUser != null) {
                SceneManager.getInstance().showEditUserScreen(selectedUser);
            }
        });

        deleteItem.setOnAction(event ->{
            User selectedUser = userTableView.getSelectionModel().getSelectedItem();
            if(selectedUser != null){
                handleDelete(selectedUser);
            }
        });

        userTableView.setRowFactory(tv ->{
            TableRow<User> row = new TableRow<>();
            row.setOnContextMenuRequested(event -> {
                if(!row.isEmpty()){
                    userTableView.getSelectionModel().select(row.getItem());
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                }
            });

            return row;
        });
    }

    /**
     * Upravlja procesom brisanja korisnika.
     * @param user korisnik koji će potencijalno biti obrisan
     */
    public void handleDelete(User user){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Delete User");
        dialog.setHeaderText("Are you sure you want to delete the user " + user.getUsername() +"?");
        ConfirmDeletionDialog.show(user, dialog, () -> {
            if(!LogInController.getCurrentUser().getId().equals(user.getId())) {
                userDataRepository.delete(user);
                changeLogRepository.log(new ChangeLog<>(user, ChangeLogType.DELETE));
                filterUsers();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("User cannot be deleted");
                alert.showAndWait();
            }
        });

    }
}
