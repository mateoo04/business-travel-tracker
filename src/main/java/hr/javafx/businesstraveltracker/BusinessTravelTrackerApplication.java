package hr.javafx.businesstraveltracker;

import hr.javafx.businesstraveltracker.repository.EmployeeRepository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class BusinessTravelTrackerApplication extends Application {

    private static Logger log = LoggerFactory.getLogger(EmployeeRepository.class);

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            log.error(thread.getName(), throwable);
        });

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