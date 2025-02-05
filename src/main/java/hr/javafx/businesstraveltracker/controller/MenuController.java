package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.enums.UserPrivileges;
import hr.javafx.businesstraveltracker.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

/**
 * Kontroler za MenuBar izbornik
 */
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

    /**
     * Inicijalizira menu bar.
     */
    public void initialize(){
        if(LogInController.getCurrentUser().privileges() == UserPrivileges.LOW){
            addNewEmployeeMenuItem.setDisable(true);
            addNewExpenseCategoryMenuItem.setDisable(true);
            addNewExpenseMenuItem.setDisable(true);
            addNewTravelLogMenuItem.setDisable(true);
            addNewReimbursementMenuItem.setDisable(true);
        }
    }

    /**
     * Otvara pretragu zaposlenika
     */
    public void openEmployeeSearch(){
        sceneManager.showEmployeeSearch();
    }

    /**
     * Otvara ekran za dodavanje novog zaposlenika.
     */
    public void openNewEmployeeScreen(){
        sceneManager.showNewEmployeeScreen();
    }


    /**
     * Otvara pretragu kategorija troškova.
     */
    public void openExpenseCategorySearch(){
        sceneManager.showExpenseCategorySearch();
    }

    /**
     * Otvara ekran za dodavanje nove kategorije troškova.
     */
    public void openNewExpenseCategoryScreen(){
        sceneManager.showNewExpenseCategoryScreen();
    }

    /**
     * Otvara pretragu troškova.
     */
    public void openExpenseSearch(){
        sceneManager.showExpenseSearch();
    }

    /**
     * Otvara ekran za dodavanje novih troškova.
     */
    public void openNewExpenseScreen(){
        sceneManager.showNewExpenseScreen();
    }

    /**
     * Otvara pretragu zabilješki putovanja.
     */
    public void openTravelLogSearch(){
        sceneManager.showTravelLogSearch();
    }

    /**
     * Otvara ekran za dodavanje novih bilješki putovanja.
     */
    public void openNewTravelLogScreen(){
        sceneManager.showNewTravelLogScreen();
    }

    /**
     * Otvara ekran za pretragu nadoknada troškova.
     */
    public void openReimbursementSearch(){
        sceneManager.showReimbursementSearch();
    }

    /**
     * Otvara ekran za dodavanje novih naknada troškova.
     */
    public void openNewReimbursementScreen(){
        sceneManager.showNewReimbursementScreen();
    }

    /**
     * Otvara prikaz bilješki promjena.
     */
    public void openChangeLog(){
        sceneManager.showChangeLogScreen();
    }

    /**
     * Zathjeva prikaz ekrana za prijavu.
     */
    public void demandLogOut(){
        sceneManager.showLogInScene();
    }

    public void openStats(){
        sceneManager.showEmployeeSpendingScreen();
    }
}
