package hr.javafx.businesstraveltracker.util;

import hr.javafx.businesstraveltracker.BusinessTravelTrackerApplication;
import hr.javafx.businesstraveltracker.controller.*;
import hr.javafx.businesstraveltracker.exception.SceneManagerException;
import hr.javafx.businesstraveltracker.model.*;
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
            throw new SceneManagerException(e);
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

    public void showEditEmployeeScreen(Employee employee){
        FXMLLoader fxmlLoader = new FXMLLoader(BusinessTravelTrackerApplication.class.
                getResource("edit-employee-screen.fxml"));
        Scene scene = null;
        try{
            scene = new Scene(fxmlLoader.load(), 800, 600);

            EditEmployeeScreenController controller = fxmlLoader.getController();
            controller.initData(employee);
        }catch (IOException e){
            throw new SceneManagerException(e);
        }

        BusinessTravelTrackerApplication.getPrimaryStage().setScene(scene);
        BusinessTravelTrackerApplication.getPrimaryStage().setTitle("Edit Employee");
        BusinessTravelTrackerApplication.getPrimaryStage().show();
    }

    public void showExpenseCategorySearch(){
        showScene("expense-category-search.fxml", "Expense Category Search");
    }

    public void showNewExpenseCategoryScreen(){
        showScene("new-expense-category-screen.fxml", "New Expense Category");
    }

    public void showEditExpenseCategoryScreen(ExpenseCategory expenseCategory){
        FXMLLoader fxmlLoader = new FXMLLoader(BusinessTravelTrackerApplication.class.
                getResource("edit-expense-category-screen.fxml"));
        Scene scene = null;
        try{
            scene = new Scene(fxmlLoader.load(), 800, 600);

            EditExpenseCategoryController controller = fxmlLoader.getController();
            controller.initData(expenseCategory);
        }catch (IOException e){
            throw new SceneManagerException(e);
        }

        BusinessTravelTrackerApplication.getPrimaryStage().setScene(scene);
        BusinessTravelTrackerApplication.getPrimaryStage().setTitle("Edit Expense Category");
        BusinessTravelTrackerApplication.getPrimaryStage().show();
    }

    public void showExpenseSearch(){
        showScene("expense-search.fxml", "Expense Search");
    }

    public void showNewExpenseScreen(){
        showScene("new-expense-screen.fxml", "New Expense");
    }

    public void showEditExpenseScreen(Expense expense){
        FXMLLoader fxmlLoader = new FXMLLoader(BusinessTravelTrackerApplication.class.
                getResource("edit-expense-screen.fxml"));
        Scene scene = null;
        try{
            scene = new Scene(fxmlLoader.load(), 800, 600);

            EditExpenseScreenController controller = fxmlLoader.getController();
            controller.initData(expense);
        }catch (IOException e){
            throw new SceneManagerException(e);
        }

        BusinessTravelTrackerApplication.getPrimaryStage().setScene(scene);
        BusinessTravelTrackerApplication.getPrimaryStage().setTitle("Edit Expense");
        BusinessTravelTrackerApplication.getPrimaryStage().show();
    }

    public void showTravelLogSearch(){
        showScene("travel-log-search.fxml", "Travel Log Search");
    }

    public void showNewTravelLogScreen(){
        showScene("new-travel-log-screen.fxml", "New Travel Log");
    }

    public void showEditTravelLogScreen(TravelLog travelLog){
        FXMLLoader fxmlLoader = new FXMLLoader(BusinessTravelTrackerApplication.class.
                getResource("edit-travel-log-screen.fxml"));
        Scene scene = null;
        try{
            scene = new Scene(fxmlLoader.load(), 800, 600);

            EditTravelLogController controller = fxmlLoader.getController();
            controller.initData(travelLog);
        }catch (IOException e){
            throw new SceneManagerException(e);
        }

        BusinessTravelTrackerApplication.getPrimaryStage().setScene(scene);
        BusinessTravelTrackerApplication.getPrimaryStage().setTitle("Edit Travel Log");
        BusinessTravelTrackerApplication.getPrimaryStage().show();
    }

    public void showReimbursementSearch(){
        showScene("reimbursement-search.fxml", "Reimbursement Search");
    }

    public void showNewReimbursementScreen(){
        showScene("new-reimbursement-screen.fxml", "New Reimbursement");
    }

    public void showEditReimbursementScreen(Reimbursement reimbursement){
        FXMLLoader fxmlLoader = new FXMLLoader(BusinessTravelTrackerApplication.class.
                getResource("edit-reimbursement-screen.fxml"));
        Scene scene = null;
        try{
            scene = new Scene(fxmlLoader.load(), 800, 600);

            EditReimbursementScreenController controller = fxmlLoader.getController();
            controller.initData(reimbursement);
        }catch (IOException e){
            throw new SceneManagerException(e);
        }

        BusinessTravelTrackerApplication.getPrimaryStage().setScene(scene);
        BusinessTravelTrackerApplication.getPrimaryStage().setTitle("Edit Reimbursement");
        BusinessTravelTrackerApplication.getPrimaryStage().show();
    }

    public void showChangeLogScreen(){
        showScene("change-log-screen.fxml","Change Log");
    }

}
