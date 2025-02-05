package hr.javafx.businesstraveltracker.repository;

import hr.javafx.businesstraveltracker.exception.RepositoryAccessException;
import hr.javafx.businesstraveltracker.model.ExpenseCategory;
import production.threads.DatabaseOperationThread;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Upravlja repozitorijem kategorija troškova.
 */
public class ExpenseCategoryRepository implements CrudRepository<ExpenseCategory> {
    /**
     * Pronalazi i vraća sve spremljene kategorije troškova
     * @return spremljene kategorije troškova
     */
    @Override
    public List<ExpenseCategory> findAll() {
        final List<ExpenseCategory> employees = new ArrayList<>();

        DatabaseOperationThread thread = new DatabaseOperationThread(()->{
            try(Connection connection = Database.connectToDatabase()) {
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("select id, name, description from expense_category");

                while(rs.next())
                    employees.add(extractExpenseCategoryFromResultSet(rs));
            } catch (SQLException | IOException e) {
                throw new RepositoryAccessException(e);
            }
        });
        thread.start();

        try{
            thread.join();
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
            throw new RepositoryAccessException(e);
        }

        return employees;
    }

    /**
     * Pronalazi kategoriju troškova sa zadanim ID-jem
     * @param id ID zapisa
     * @return Optional koji sadržava kategoriju troškova sa zadanim ID-jem ako je pronađena
     * @throws RepositoryAccessException
     */
    @Override
    public Optional<ExpenseCategory> findById(Long id) throws RepositoryAccessException {
        AtomicReference<ExpenseCategory> employee = new AtomicReference<>();

        DatabaseOperationThread thread = new DatabaseOperationThread(()->{
            try(Connection connection = Database.connectToDatabase()) {
                PreparedStatement stmt = connection.prepareStatement("select id, name, description from expense_category where id = ?");
                stmt.setLong(1, id);
                ResultSet rs = stmt.executeQuery();
                if(rs.next()) employee.set(extractExpenseCategoryFromResultSet(rs));
            } catch (SQLException | IOException e) {
                throw new RepositoryAccessException(e);
            }
        });
        thread.start();

        try{
            thread.join();
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
            throw new RepositoryAccessException(e);
        }

        return Optional.ofNullable(employee.get());
    }

    /**
     * Sprema kategoriju troškova
     * @param entity
     */
    @Override
    public void save(ExpenseCategory entity){
        DatabaseOperationThread thread = new DatabaseOperationThread(()->{
            try(Connection connection = Database.connectToDatabase()){
                PreparedStatement stmt = connection.prepareStatement
                        ("INSERT INTO expense_category(name, description) VALUES(?, ?)");
                stmt.setString(1, entity.getName());
                stmt.setString(2, entity.getDescription());

                stmt.executeUpdate();

            } catch (SQLException | IOException e) {
                throw new RepositoryAccessException(e);
            }
        });
        thread.start();
    }

    /**
     * Ažurira podatke o kategoriji troškova u bazi podataka
     * @param entity
     */
    @Override
    public void update(ExpenseCategory entity) {
        DatabaseOperationThread thread = new DatabaseOperationThread(()->{
            try(Connection connection = Database.connectToDatabase()){
                PreparedStatement stmt = connection.prepareStatement
                        ("update expense_category set name = ?, description = ? where id = ?");
                stmt.setString(1, entity.getName());
                stmt.setString(2,entity.getDescription());
                stmt.setInt(3, entity.getId().intValue());
                stmt.executeUpdate();
            }catch (SQLException | IOException e) {
                throw new RepositoryAccessException(e);
            }
        });
        thread.start();
    }

    /**
     * Briše zapis kategorije troškova sa zadanim ID-jem
     * @param id ID zapisa
     */
    @Override
    public void deleteById(Long id) {
        deleteFromTable("expense_category",id);
    }

    /**
     * Extracta zapis kategorije troškova iz ResultSeta
     * @param rs
     * @return kategoriju troškova
     * @throws SQLException
     */
    private ExpenseCategory extractExpenseCategoryFromResultSet(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String name = rs.getString("name");
        String description = rs.getString("description");

        return new ExpenseCategory(id, name,description);
    }
}
