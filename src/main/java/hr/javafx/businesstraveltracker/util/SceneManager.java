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

    private double getWidth(){
        return BusinessTravelTrackerApplication.getPrimaryStage().getWidth() == 0 ?
                800 : BusinessTravelTrackerApplication.getPrimaryStage().getWidth();
    }

    private double getHeight(){
        return BusinessTravelTrackerApplication.getPrimaryStage().getHeight() == 0 ?
                600 : BusinessTravelTrackerApplication.getPrimaryStage().getHeight();
    }

    /**
     * Prikazuje ekran za prijavu.
     */
    public void showLogInScene(){
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
     * Prikazuje Scene za uređivanje zaposlenika
     *
     * @param employee zaposlenik
     */
    public void showEditEmployeeScreen(Employee employee) {
        FXMLLoader fxmlLoader = new FXMLLoader(BusinessTravelTrackerApplication.class.
                getResource("edit-employee-screen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), getWidth(), getHeight());

            EditEmployeeScreenController controller = fxmlLoader.getController();
            controller.initData(employee);
        } catch (IOException e) {
            throw new SceneManagerException(e);
        }

        BusinessTravelTrackerApplication.getPrimaryStage().setScene(scene);
        BusinessTravelTrackerApplication.getPrimaryStage().setTitle("Edit Employee");
        BusinessTravelTrackerApplication.getPrimaryStage().show();
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
     * Prikazuje Scene za uređivanje kategorije troškova.
     *
     * @param expenseCategory
     */
    public void showEditExpenseCategoryScreen(ExpenseCategory expenseCategory) {
        FXMLLoader fxmlLoader = new FXMLLoader(BusinessTravelTrackerApplication.class.
                getResource("edit-expense-category-screen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), getWidth(), getHeight());

            EditExpenseCategoryController controller = fxmlLoader.getController();
            controller.initData(expenseCategory);
        } catch (IOException e) {
            throw new SceneManagerException(e);
        }

        BusinessTravelTrackerApplication.getPrimaryStage().setScene(scene);
        BusinessTravelTrackerApplication.getPrimaryStage().setTitle("Edit Expense Category");
        BusinessTravelTrackerApplication.getPrimaryStage().show();
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
     * Prikazuje Scene za uređivanje troška.
     *
     * @param expense
     */
    public void showEditExpenseScreen(Expense expense) {
        FXMLLoader fxmlLoader = new FXMLLoader(BusinessTravelTrackerApplication.class.
                getResource("edit-expense-screen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), getWidth(), getHeight());

            EditExpenseScreenController controller = fxmlLoader.getController();
            controller.initData(expense);
        } catch (IOException e) {
            throw new SceneManagerException(e);
        }

        BusinessTravelTrackerApplication.getPrimaryStage().setScene(scene);
        BusinessTravelTrackerApplication.getPrimaryStage().setTitle("Edit Expense");
        BusinessTravelTrackerApplication.getPrimaryStage().show();
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
     * Prikazuje Scene za uređivanje zabilješke putovanja.
     *
     * @param travelLog
     */
    public void showEditTravelLogScreen(TravelLog travelLog) {
        FXMLLoader fxmlLoader = new FXMLLoader(BusinessTravelTrackerApplication.class.
                getResource("edit-travel-log-screen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), getWidth(), getHeight());

            EditTravelLogController controller = fxmlLoader.getController();
            controller.initData(travelLog);
        } catch (IOException e) {
            throw new SceneManagerException(e);
        }

        BusinessTravelTrackerApplication.getPrimaryStage().setScene(scene);
        BusinessTravelTrackerApplication.getPrimaryStage().setTitle("Edit Travel Log");
        BusinessTravelTrackerApplication.getPrimaryStage().show();
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
     * Prikazuje Scene za uređivanje nadoknade troška.
     *
     * @param reimbursement
     */
    public void showEditReimbursementScreen(Reimbursement reimbursement) {
        FXMLLoader fxmlLoader = new FXMLLoader(BusinessTravelTrackerApplication.class.
                getResource("edit-reimbursement-screen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), getWidth(), getHeight());

            EditReimbursementScreenController controller = fxmlLoader.getController();
            controller.initData(reimbursement);
        } catch (IOException e) {
            throw new SceneManagerException(e);
        }

        BusinessTravelTrackerApplication.getPrimaryStage().setScene(scene);
        BusinessTravelTrackerApplication.getPrimaryStage().setTitle("Edit Reimbursement");
        BusinessTravelTrackerApplication.getPrimaryStage().show();
    }

    /**
     * Prikazuje Scene za popisom zabilješki promjena
     */
    public void showChangeLogScreen() {
        showScene("change-log-screen.fxml", "Change Log");
    }

    public void showEmployeeSpendingScreen(){
        showScene("stats-screen.fxml","Stats");
    }

}
