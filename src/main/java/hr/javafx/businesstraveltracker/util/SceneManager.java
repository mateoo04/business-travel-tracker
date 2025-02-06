package hr.javafx.businesstraveltracker.util;

import hr.javafx.businesstraveltracker.BusinessTravelTrackerApplication;
import hr.javafx.businesstraveltracker.controller.*;
import hr.javafx.businesstraveltracker.exception.SceneManagerException;
import hr.javafx.businesstraveltracker.model.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

/**
 * Klasa koja upravlja prikazom Scena.
 */
public class SceneManager {

    private static SceneManager instance;

    public static final String APP_TITLE = "Business Trip Expense Tracker";

    private SceneManager() {
    }

    /**
     * Vraća instancu ove Singleton klase.
     *
     * @return
     */
    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }

        return instance;
    }

    /**
     * Prikazuje Scene
     *
     * @param resource FXML file
     */
    private void showScene(String resource) {
        FXMLLoader fxmlLoader = new FXMLLoader(BusinessTravelTrackerApplication.class.getResource(resource));
        Scene scene = null;

        try {
            scene = new Scene(fxmlLoader.load(), getWidth(), getHeight());
        } catch (IOException e) {
            throw new SceneManagerException(e);
        }

        BusinessTravelTrackerApplication.getPrimaryStage().setScene(scene);
        BusinessTravelTrackerApplication.getPrimaryStage().show();
    }

    /**
     * Vraća širinu prozora ili zadanu širinu ako je aplikacija tek pokrenuta.
     *
     * @return širinu prozora
     */
    private double getWidth(){
        double width = BusinessTravelTrackerApplication.getPrimaryStage().getWidth();
        if(width >700 && width < 900) return 800;
        return width == 0 ?800 : width;
    }

    /**
     * Vraća visinu prozora ili zadanu visinu ako je aplikacija tek pokrenuta.
     *
     * @return visinu prozora
     */
    private double getHeight(){
        double height = BusinessTravelTrackerApplication.getPrimaryStage().getHeight();
        if(height >500 && height < 700) return 600;
        return height == 0 ? 600 : height;
    }

    /**
     * Prikazuje ekran za prijavu.
     */
    public void showLogInScene() {
        showScene("login-view.fxml");
    }

    /**
     * Prikazuje pretragu zaposlenika
     */
    public void showEmployeeSearch() {
        showScene("employee-search.fxml");
    }

    /**
     * Prikazuje Scene za dodavanje novog zaposlenika.
     */
    public void showNewEmployeeScreen() {
        showScene("new-employee-screen.fxml");
    }


    /**
     * Prikazuje pretragu kategorija troškova
     */
    public void showExpenseCategorySearch() {
        showScene("expense-category-search.fxml");
    }

    /**
     * Prikazuje Scene za dodavanje nove kategorije troškova
     */
    public void showNewExpenseCategoryScreen() {
        showScene("new-expense-category-screen.fxml");
    }


    /**
     * Prikazuje pretragu troškova
     */
    public void showExpenseSearch() {
        showScene("expense-search.fxml");
    }

    /**
     * Prikazuje Scene za dodavanje novog troška.
     */
    public void showNewExpenseScreen() {
        showScene("new-expense-screen.fxml");
    }


    /**
     * Prikazuje pretragu zabilješki putovanja
     */
    public void showTravelLogSearch() {
        showScene("travel-log-search.fxml");
    }

    /**
     * Prikazuje Scene za dodavanje nove zabilješke putovanja.
     */
    public void showNewTravelLogScreen() {
        showScene("new-travel-log-screen.fxml");
    }

    /**
     * Prikazuje pretragu nadoknada troškova
     */
    public void showReimbursementSearch() {
        showScene("reimbursement-search.fxml");
    }

    /**
     * Prikazuje Scene za dodavanje nove zabilješke nadoknade troška.
     */
    public void showNewReimbursementScreen() {
        showScene("new-reimbursement-screen.fxml");
    }

    /**
     * Prikazuje Scene za popisom zabilješki promjena
     */
    public void showChangeLogScreen() {
        showScene("change-log-screen.fxml");
    }

    /**
     * Prikazuje Scene s grafovima o potrošnji.
     */
    public void showEmployeeSpendingScreen() {
        showScene("stats-screen.fxml");
    }

    public void showUserSearchScreen() {
        showScene("user-search.fxml");
    }

    public void showNewUserScreen() {
        showScene("new-user-screen.fxml");
    }

    /**
     * Prikazuje ekran za uređivanje podataka.
     * @param resource FXML datoteka
     * @param entity objekt koji će biti uređivan
     * @param <T> klasa elementa koji će biti uređivan
     */
    public <T extends Entity> void showEditScreen(String resource, T entity) {
        try {
            FXMLLoader loader = new FXMLLoader(BusinessTravelTrackerApplication.class.getResource(resource));
            Scene scene = new Scene(loader.load(), getWidth(), getHeight());
            EditScreenController<Entity> controller = loader.getController();
            controller.initData(entity);
            BusinessTravelTrackerApplication.getPrimaryStage().setScene(scene);
            BusinessTravelTrackerApplication.getPrimaryStage().show();
        } catch (IOException e) {
            throw new SceneManagerException(e);
        }
    }

    /**
     * Prikazuje Scene za uređivanje zaposlenika
     *
     * @param employee zaposlenik
     */
    public void showEditEmployeeScreen(Employee employee) { showEditScreen("edit-employee-screen.fxml", employee);}

    /**
     * Prikazuje Scene za uređivanje kategorije troškova.
     *
     * @param category
     */
    public void showEditExpenseCategoryScreen(ExpenseCategory category) {showEditScreen("edit-expense-category-screen.fxml", category); }

    /**
     * Prikazuje Scene za uređivanje troška.
     *
     * @param expense
     */
    public void showEditExpenseScreen(Expense expense) {showEditScreen("edit-expense-screen.fxml", expense);}

    /**
     * Prikazuje Scene za uređivanje zabilješke putovanja.
     *
     * @param log
     */
    public void showEditTravelLogScreen(TravelLog log) {showEditScreen("edit-travel-log-screen.fxml", log);}

    /**
     * Prikazuje Scene za uređivanje nadoknade troška.
     *
     * @param reimbursement
     */
    public void showEditReimbursementScreen(Reimbursement reimbursement) {showEditScreen("edit-reimbursement-screen.fxml", reimbursement);}

    /**
     * Prikazuje ekran za uređivanje korisnika.
     * @param user korisnik koji će biti uređivan
     */
    public void showEditUserScreen(User user) {showEditScreen("edit-user-screen.fxml", user);}
}
