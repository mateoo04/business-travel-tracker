package hr.javafx.businesstraveltracker.repository;

import hr.javafx.businesstraveltracker.enums.Department;
import hr.javafx.businesstraveltracker.enums.TripStatus;
import hr.javafx.businesstraveltracker.exception.InvalidEnumValueException;
import hr.javafx.businesstraveltracker.exception.RepositoryAccessException;
import hr.javafx.businesstraveltracker.model.Employee;
import hr.javafx.businesstraveltracker.model.TravelLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import production.threads.DatabaseOperationThread;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Klasa koja upravlja repozitorijem zabilješki putovanja.
 */
public class TravelLogRepository implements CrudRepository<TravelLog> {

    private static Logger log = LoggerFactory.getLogger(TravelLogRepository.class);

    /**
     * Pronalazi i vraća sve zabilješke troškova iz baze podataka.
     * @return sve zabilješke troškova iz baze podataka
     */
    @Override
    public List<TravelLog> findAll() {
        List<TravelLog> travelLogs = new ArrayList<>();

        DatabaseOperationThread thread = new DatabaseOperationThread(()->{
            try(Connection connection = Database.connectToDatabase()){
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("select travel_log.id, employee_id, destination, start_date, " +
                        "end_date, trip_status, first_name, last_name, role, department_id, email from travel_log " +
                        "join employee on employee.id = travel_log.employee_id");

                while(resultSet.next()) travelLogs.add(extractTravelLogFromResultSet(resultSet));
            }catch (SQLException | IOException e) {
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

        return travelLogs;
    }

    /**
     * Pronalazi zabilješku putovanja sa odgovarajućim ID-jem
     * @param id ID zapisa
     * @return Optional koji sadržava zabiješku putovanja sa zadanim ID-jem ako je pronađen
     */
    @Override
    public Optional<TravelLog> findById(Long id) {
        AtomicReference<TravelLog> travelLogRef = new AtomicReference<>();

        DatabaseOperationThread thread = new DatabaseOperationThread(()->{
            try(Connection connection = Database.connectToDatabase()){
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("select travel_log.id, employee_id, destination, start_date, " +
                        "end_date, trip_status, first_name, last_name, role, department_id, email from travel_log " +
                        "join employee on employee.id = travel_log.employee_id where travel_log.id = " + id);

                if(resultSet.next()) travelLogRef.set(extractTravelLogFromResultSet(resultSet));
            }catch (SQLException | IOException e) {
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

        return Optional.ofNullable(travelLogRef.get());
    }

    /**
     * Sprema zabilješku putovanja u bazu podataka
     * @param entity
     */
    @Override
    public void save(TravelLog entity) {
        DatabaseOperationThread thread = new DatabaseOperationThread(()->{
            try(Connection connection = Database.connectToDatabase()){
                PreparedStatement stmt = connection.prepareStatement
                        ("insert into travel_log(employee_id, destination, start_date, end_date, trip_status) " +
                                "values(?,?,?,?,?)");

                stmt.setInt(1, entity.getEmployee().getId().intValue());
                stmt.setString(2, entity.getDestination());
                stmt.setDate(3, Date.valueOf(entity.getStartDate()));
                stmt.setDate(4, Date.valueOf(entity.getEndDate()));
                stmt.setObject(5, entity.getStatus().getName(), java.sql.Types.OTHER);

                stmt.executeUpdate();
            } catch (SQLException | IOException e) {
                throw new RepositoryAccessException(e);
            }
        });
        thread.start();
    }

    /**
     * Ažurira zabilješku putovanja u bazi podataka.
     * @param entity
     */
    @Override
    public void update(TravelLog entity) {
        DatabaseOperationThread thread = new DatabaseOperationThread(()->{
            try(Connection connection = Database.connectToDatabase()){
                PreparedStatement stmt = connection.prepareStatement
                        ("update travel_log set employee_id = ?, destination = ?," +
                                "start_date = ?, end_date = ?, trip_status = ? where id = ?");
                stmt.setInt(1, entity.getEmployee().getId().intValue());
                stmt.setString(2, entity.getDestination());
                stmt.setDate(3, Date.valueOf(entity.getStartDate()));
                stmt.setDate(4, Date.valueOf(entity.getEndDate()));
                stmt.setObject(5, entity.getStatus().getName(), java.sql.Types.OTHER);
                stmt.setInt(6, entity.getId().intValue());
                stmt.executeUpdate();
            }catch (SQLException | IOException e) {
                throw new RepositoryAccessException(e);
            }
        });
        thread.start();
    }

    /**
     * Briše zabilješku putovanja iz baze podataka.
     * @param id ID zapisa
     */
    @Override
    public void deleteById(Long id) {
        deleteFromTable("travel_log",id);
    }

    /**
     * Sprema listu zabilješki putovanja u bazu podataka.
     * @param entities lista zabilješki putovanja
     */
    public void save(List<TravelLog> entities) {

        DatabaseOperationThread thread = new DatabaseOperationThread(()->{
            Connection connection = null;
            try{
                connection = Database.connectToDatabase();

                PreparedStatement stmt = connection.prepareStatement
                        ("insert into travel_log(employee_id, destination, start_date, end_date, trip_status) values(?,?,?,?,?)");

                connection.setAutoCommit(false);

                stmt.setString(2, entities.getFirst().getDestination());
                stmt.setDate(3, Date.valueOf(entities.getFirst().getStartDate()));
                stmt.setDate(4, Date.valueOf(entities.getFirst().getEndDate()));
                stmt.setString(5, entities.getFirst().getStatus().getName());
                for(TravelLog travelLog : entities){
                    stmt.setString(1, travelLog.getDestination());
                    stmt.addBatch();
                }

                stmt.executeBatch();
                connection.commit();

            } catch (SQLException | IOException e) {
                throw new RepositoryAccessException(e);
            }finally{
                try{
                    if(connection != null) {
                        connection.setAutoCommit(true);
                        connection.close();
                    }
                }catch (SQLException e){
                    Thread.currentThread().interrupt();
                }
            }
        });
        thread.start();
    }

    /**
     * Extracta zabilješku putovanja iz ResultSeta
     * @param rs
     * @return zabilješku putovanja
     * @throws SQLException
     */
    private TravelLog extractTravelLogFromResultSet(ResultSet rs) throws SQLException{
        Long id = rs.getLong("id");
        String destination = rs.getString("destination");
        LocalDate startDate = rs.getDate("start_date").toLocalDate();
        LocalDate endDate = rs.getDate("end_date").toLocalDate();
        String statusString = rs.getString("trip_status");

        Long employeeId = rs.getLong("employee_id");
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        String role = rs.getString("role");
        Department department = null;
        try {
            department = Department.getById((long) rs.getInt("department_id"));
        } catch (InvalidEnumValueException e) {
            log.error(e.getMessage());
        }
        String email = rs.getString("email");

        TripStatus tripStatus = null;
        try{
            tripStatus = TripStatus.getByName(statusString);
        }catch (InvalidEnumValueException e){
            log.error(e.getMessage());
        }

        return new TravelLog(id, new Employee.Builder(firstName, lastName, role, department)
                .withId(employeeId)
                .withEmail(email)
                .build(),
                destination, startDate, endDate, tripStatus);
    }
}
