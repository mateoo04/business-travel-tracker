package production.threads;

import hr.javafx.businesstraveltracker.BusinessTravelTrackerApplication;
import hr.javafx.businesstraveltracker.exception.RepositoryAccessException;
import hr.javafx.businesstraveltracker.repository.Database;
import hr.javafx.businesstraveltracker.util.SceneManager;
import javafx.application.Platform;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TotalExpensesThread extends Thread {
    /**
     * Pokretanje niti.
     */
    @Override
    public void run() {
        synchronized (Database.class) {
            while (Boolean.TRUE.equals(Database.isActiveConnectionWithDatabase())) {
                try {
                    Database.class.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }

            Database.setActiveConnectionWithDatabase(true);

            try (Connection connection = Database.connectToDatabase();
                 PreparedStatement stmt = connection.prepareStatement("select sum(amount) from expense");
                 ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    String title = SceneManager.APP_TITLE +
                            " (Total expenses: " + rs.getBigDecimal("sum") + "€)";
                    Platform.runLater(() -> BusinessTravelTrackerApplication.getPrimaryStage().setTitle(title));
                }

            } catch (SQLException | IOException e) {
                throw new RepositoryAccessException(e);
            } finally {
                Database.setActiveConnectionWithDatabase(false);
                Database.class.notifyAll();
            }
        }
    }
}
