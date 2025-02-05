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
     * @param title    Title koji će biti u zaglavlju prozora
     */
    private void showScene(String resource, String title) {
        FXMLLoader fxmlLoader = new FXMLLoader(BusinessTravelTrackerApplication.class.getResource(resource));
        Scene scene = null;

        try {
            scene = new Scene(fxmlLoader.load(), getWidth(), getHeight());
        } catch (IOException e) {
            throw new SceneManagerException(e);
        }

        BusinessTravelTrackerApplication.getPrimaryStage().setScene(scene);
        BusinessTravelTrackerApplication.getPrimaryStage().setTitle(title);
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
        showScene("login-view.fxml", "Business Travel Expenses Tracker - Log in");
    }

    /**
     * Prikazuje pretragu zaposlenika
     */
    public void showEmployeeSearch() {
        showScene("employee-search.fxml", "Employee Search");
    }

    /**
     * Prikazuje Scene za dodavanje novog zaposlenika.
     */
    public void showNewEmployeeScreen() {
        showScene("new-employee-screen.fxml", "New Employee");
    }


    /**
     * Prikazuje pretragu kategorija troškova
     */
    public void showExpenseCategorySearch() {
        showScene("expense-category-search.fxml", "Expense Category Search");
    }

    /**
     * Prikazuje Scene za dodavanje nove kategorije troškova
     */
    public void showNewExpenseCategoryScreen() {
        showScene("new-expense-category-screen.fxml", "New Expense Category");
    }


    /**
     * Prikazuje pretragu troškova
     */
    public void showExpenseSearch() {
        showScene("expense-search.fxml", "Expense Search");
    }

    /**
     * Prikazuje Scene za dodavanje novog troška.
     */
    public void showNewExpenseScreen() {
        showScene("new-expense-screen.fxml", "New Expense");
    }


    /**
     * Prikazuje pretragu zabilješki putovanja
     */
    public void showTravelLogSearch() {
        showScene("travel-log-search.fxml", "Travel Log Search");
    }

    /**
     * Prikazuje Scene za dodavanje nove zabilješke putovanja.
     */
    public void showNewTravelLogScreen() {
        showScene("new-travel-log-screen.fxml", "New Travel Log");
    }

    /**
     * Prikazuje pretragu nadoknada troškova
     */
    public void showReimbursementSearch() {
        showScene("reimbursement-search.fxml", "Reimbursement Search");
    }

    /**
     * Prikazuje Scene za dodavanje nove zabilješke nadoknade troška.
     */
    public void showNewReimbursementScreen() {
        showScene("new-reimbursement-screen.fxml", "New Reimbursement");
    }

    /**
     * Prikazuje Scene za popisom zabilješki promjena
     */
    public void showChangeLogScreen() {
        showScene("change-log-screen.fxml", "Change Log");
    }

    /**
     * Prikazuje Scene s grafovima o potrošnji.
     */
    public void showEmployeeSpendingScreen() {
        showScene("stats-screen.fxml", "Stats");
    }

    public void showUserSearchScreen() {
        showScene("user-search.fxml", "Users");
    }

    public void showNewUserScreen() {
        showScene("new-user-screen.fxml", "New User");
    }

    public <T extends Entity> void showEditScreen(String resource, String title, T entity) {
        try {
            FXMLLoader loader = new FXMLLoader(BusinessTravelTrackerApplication.class.getResource(resource));
            Scene scene = new Scene(loader.load(), getWidth(), getHeight());
            EditScreenController<Entity> controller = loader.getController();
            controller.initData(entity);
            BusinessTravelTrackerApplication.getPrimaryStage().setScene(scene);
            BusinessTravelTrackerApplication.getPrimaryStage().setTitle(title);
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
    public void showEditEmployeeScreen(Employee employee) { showEditScreen("edit-employee-screen.fxml", "Edit Employee", employee);}

    /**
     * Prikazuje Scene za uređivanje kategorije troškova.
     *
     * @param category
     */
    public void showEditExpenseCategoryScreen(ExpenseCategory category) {showEditScreen("edit-expense-category-screen.fxml", "Edit Expense Category", category); }

    /**
     * Prikazuje Scene za uređivanje troška.
     *
     * @param expense
     */
    public void showEditExpenseScreen(Expense expense) {showEditScreen("edit-expense-screen.fxml", "Edit Expense", expense);}

    /**
     * Prikazuje Scene za uređivanje zabilješke putovanja.
     *
     * @param log
     */
    public void showEditTravelLogScreen(TravelLog log) {showEditScreen("edit-travel-log-screen.fxml", "Edit Travel Log", log);}

    /**
     * Prikazuje Scene za uređivanje nadoknade troška.
     *
     * @param reimbursement
     */
    public void showEditReimbursementScreen(Reimbursement reimbursement) {showEditScreen("edit-reimbursement-screen.fxml", "Edit Reimbursement", reimbursement);}

    public void showEditUserScreen(User user) {showEditScreen("edit-user-screen.fxml", "Edit User", user);}
}
