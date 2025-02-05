package hr.javafx.businesstraveltracker.repository;

import hr.javafx.businesstraveltracker.enums.Department;
import hr.javafx.businesstraveltracker.exception.InvalidEnumValueException;
import hr.javafx.businesstraveltracker.exception.RepositoryAccessException;
import hr.javafx.businesstraveltracker.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import production.threads.DatabaseOperationThread;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Klasa koja upravlja zapisima zaposlenika.
 */
public class EmployeeRepository implements CrudRepository<Employee> {

    private static Logger log = LoggerFactory.getLogger(EmployeeRepository.class);

    /**
     * Pronalazi i vraća sve zaposlenike u bazi podataka.
     * @return sve spremljene zaposlenike
     */
    @Override
    public List<Employee> findAll() {
        final List<Employee> employees = new ArrayList<>();

        DatabaseOperationThread thread = new DatabaseOperationThread(()->{
            try(Connection connection = Database.connectToDatabase()) {
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("select id, first_name, last_name, role, department_id, email from employee");

                while(rs.next())
                    employees.add(extractEmployeeFromResultSet(rs));
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
     * Pronalazi zaposlenika s odgovarajućim ID-jem
     * @param id ID zapisa
     * @return Optional koji sadržava zaposlenika s odgovarajućim ID-jem ako je pronađen
     * @throws RepositoryAccessException
     */
    @Override
    public Optional<Employee> findById(Long id) throws RepositoryAccessException {
        AtomicReference<Employee> employee = new AtomicReference<>();

        DatabaseOperationThread thread = new DatabaseOperationThread(()->{
            try(Connection connection = Database.connectToDatabase()) {
                PreparedStatement stmt = connection.prepareStatement("select id, first_name, last_name, department_id, role, email from employee where id = ?");
                stmt.setLong(1, id);
                ResultSet rs = stmt.executeQuery();
                if(rs.next()) employee.set(extractEmployeeFromResultSet(rs));
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
     * Sprema zaposlenika u bazu podataka.
     * @param entity
     */
    @Override
    public void save(Employee entity) {
        DatabaseOperationThread thread = new DatabaseOperationThread(()->{
            try(Connection connection = Database.connectToDatabase()){
                PreparedStatement stmt = connection.prepareStatement
                        ("INSERT INTO employee(first_name, last_name, role, department_id, email) VALUES(?, ?, ?, ?, ?)");
                stmt.setString(1, entity.getFirstName());
                stmt.setString(2, entity.getLastName());
                stmt.setString(3, entity.getRole());
                stmt.setLong(4, entity.getDepartment().getId());
                stmt.setString(5, entity.getEmail());

                stmt.executeUpdate();

            } catch (SQLException | IOException e) {
                throw new RepositoryAccessException(e);
            }
        });
        thread.start();
    }

    /**
     * Ažurira zaposlenika u bazi podataka.
     * @param entity
     */
    @Override
    public void update(Employee entity) {
        DatabaseOperationThread thread = new DatabaseOperationThread(()->{
            try(Connection connection = Database.connectToDatabase()){
                PreparedStatement stmt = connection.prepareStatement
                        ("update employee set first_name = ?, last_name = ?," +
                                "role = ?, department_id = ?, email = ? where id = ?");
                stmt.setString(1, entity.getFirstName());
                stmt.setString(2, entity.getLastName());
                stmt.setString(3, entity.getRole());
                stmt.setLong(4, entity.getDepartment().getId());
                stmt.setString(5, entity.getEmail());
                stmt.setLong(6, entity.getId());
                stmt.executeUpdate();
            }catch (SQLException | IOException e){
                throw new RepositoryAccessException(e);
            }
        });
        thread.start();
    }

    /**
     * Briše zaposlenika sa zadanim ID-jem
     * @param id ID zapisa zaposlenika
     */
    @Override
    public void deleteById(Long id) {
        deleteFromTable("employee",id);
    }

    /**
     * Extracta zaposlenika iz ResultSeta
     * @param rs
     * @return zaposlenika
     * @throws SQLException
     */
    private Employee extractEmployeeFromResultSet(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        String role = rs.getString("role");
        Long departmentId = rs.getLong("department_id");
        String email = rs.getString("email");

        Department department = null;
        try {
            department = Department.getById(departmentId);
        } catch (InvalidEnumValueException e) {
            log.error(e.getMessage());
        }

        return new Employee.Builder(firstName, lastName, role, department)
                .withEmail(email)
                .withId(id)
                .build();
    }
}
