package hr.javafx.businesstraveltracker;

import hr.javafx.businesstraveltracker.util.SceneManager;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Glavna klasa aplikacije za praćenje troškova putovanja
 */
public class BusinessTravelTrackerApplication extends Application {

    private static final Logger log = LoggerFactory.getLogger(BusinessTravelTrackerApplication.class);

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> log.error(thread.getName(), throwable));

        setPrimaryStage(stage);

        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("plane-icon.png")));
        primaryStage.setTitle(SceneManager.APP_TITLE);

        SceneManager.getInstance().showLogInScene();
    }

    /**
     * Vraća primarni Stage objekt
     * @return primarni Stage
     */
    public static Stage getPrimaryStage() {return primaryStage;}

    /**
     * Postavlja primarni stage objekt
     * @param nextPrimaryStage
     */
    private static void setPrimaryStage(Stage nextPrimaryStage) { primaryStage = nextPrimaryStage;}

    public static void main(String[] args) {
        launch();
    }
}