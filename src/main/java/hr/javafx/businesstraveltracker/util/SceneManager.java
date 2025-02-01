package hr.javafx.businesstraveltracker.util;

import hr.javafx.businesstraveltracker.BusinessTravelTrackerApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class SceneManager {

    private static SceneManager instance;

    private SceneManager() {}

    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }

        return instance;
    }

    private void showScene(String resource, String title){
        FXMLLoader fxmlLoader = new FXMLLoader(BusinessTravelTrackerApplication.class.getResource(resource));
        Scene scene = null;
        try{
            scene = new Scene(fxmlLoader.load(), 800, 600);
        }catch (IOException e){
            throw new RuntimeException(e);
        }

        BusinessTravelTrackerApplication.getPrimaryStage().setScene(scene);
        BusinessTravelTrackerApplication.getPrimaryStage().setTitle(title);
        BusinessTravelTrackerApplication.getPrimaryStage().show();
    }

    public void showEmployeeSearch(){
        showScene("employee-search.fxml", "Employee Search");
    }

    public void showNewEmployeeScreen(){
        showScene("new-employee-screen.fxml", "New Employee");
    }

    public void showExpenseCategorySearch(){
        showScene("expense-category-search.fxml", "Expense Category Search");
    }

    public void showNewExpenseCategoryScreen(){
        showScene("new-expense-category-screen.fxml", "New Expense Category");
    }

    public void showExpenseSearch(){
        showScene("expense-search.fxml", "Expense Search");
    }

    public void showNewExpenseScreen(){
        showScene("new-expense-screen.fxml", "New Expense");
    }

    public void showTravelLogSearch(){
        showScene("travel-log-search.fxml", "Travel Log Search");
    }

    public void showNewTravelLogScreen(){
        showScene("new-travel-log-screen.fxml", "New Travel Log");
    }

    public void showReimbursementSearch(){
        showScene("reimbursement-search.fxml", "Reimbursement Search");
    }

    public void showNewReimbursementScreen(){
        showScene("new-reimbursement-screen.fxml", "New Reimbursement");
    }

}
