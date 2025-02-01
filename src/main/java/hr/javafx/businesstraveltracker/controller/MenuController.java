package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.enums.UserPrivileges;
import hr.javafx.businesstraveltracker.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

public class MenuController {

    @FXML
    public MenuItem addNewEmployeeMenuItem;

    @FXML
    public MenuItem addNewExpenseCategoryMenuItem;

    @FXML
    public MenuItem addNewExpenseMenuItem;

    @FXML
    public MenuItem addNewTravelLogMenuItem;

    @FXML
    public MenuItem addNewReimbursementMenuItem;

    private final SceneManager sceneManager = SceneManager.getInstance();

    public void initialize(){
        if(LogInController.getCurrentUser().privileges() == UserPrivileges.LOW){
            addNewEmployeeMenuItem.setDisable(true);
            addNewExpenseCategoryMenuItem.setDisable(true);
            addNewExpenseMenuItem.setDisable(true);
            addNewTravelLogMenuItem.setDisable(true);
            addNewReimbursementMenuItem.setDisable(true);
        }
    }

    public void openEmployeeSearch(){
        sceneManager.showEmployeeSearch();
    }

    public void openNewEmployeeScreen(){
        sceneManager.showNewEmployeeScreen();
    }

    public void openExpenseCategorySearch(){
        sceneManager.showExpenseCategorySearch();
    }

    public void openNewExpenseCategoryScreen(){
        sceneManager.showNewExpenseCategoryScreen();
    }

    public void openExpenseSearch(){
        sceneManager.showExpenseSearch();
    }

    public void openNewExpenseScreen(){
        sceneManager.showNewExpenseScreen();
    }
    public void openTravelLogSearch(){
        sceneManager.showTravelLogSearch();
    }

    public void openNewTravelLogScreen(){
        sceneManager.showNewTravelLogScreen();
    }
    public void openReimbursementSearch(){
        sceneManager.showReimbursementSearch();
    }

    public void openNewReimbursementScreen(){
        sceneManager.showNewReimbursementScreen();
    }
}
