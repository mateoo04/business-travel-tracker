package hr.javafx.businesstraveltracker.repository;

import hr.javafx.businesstraveltracker.exception.RepositoryAccessException;
import hr.javafx.businesstraveltracker.model.Entity;
import production.threads.DatabaseOperationThread;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CrudRepository<T extends Entity> {
    List<T> findAll();

    Optional<T> findById(Long id);

    void save(T entity);

    void update(T entity);

    void deleteById(Long id);

    default void deleteFromTable(String tableName, Long id) {
        DatabaseOperationThread thread = new DatabaseOperationThread(() -> {
            try (Connection connection = Database.connectToDatabase()) {
                String sql = "DELETE FROM " + tableName + " where id = ?";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setInt(1, id.intValue());
                stmt.executeUpdate();
            } catch (IOException | SQLException e) {
                throw new RepositoryAccessException(e);
            }
        });
        thread.start();
    }
}
