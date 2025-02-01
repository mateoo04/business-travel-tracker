package hr.javafx.businesstraveltracker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BusinessTravelTrackerApplication extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(BusinessTravelTrackerApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Business Travel Expenses Tracker - Log in");
        stage.setScene(scene);
        stage.show();
    }

    public static Stage getPrimaryStage() {return primaryStage;}

    public static void main(String[] args) {
        launch();
    }
}