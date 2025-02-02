package hr.javafx.businesstraveltracker.repository;

import hr.javafx.businesstraveltracker.enums.Department;
import hr.javafx.businesstraveltracker.enums.TripStatus;
import hr.javafx.businesstraveltracker.exception.InvalidEnumValueException;
import hr.javafx.businesstraveltracker.exception.RepositoryAccessException;
import hr.javafx.businesstraveltracker.model.Employee;
import hr.javafx.businesstraveltracker.model.TravelLog;
import production.threads.DatabaseOperationThread;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class TravelLogRepository implements CrudRepository<TravelLog> {
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

    @Override
    public void save(TravelLog entity) {
        DatabaseOperationThread thread = new DatabaseOperationThread(()->{
            try(Connection connection = Database.connectToDatabase()){
                PreparedStatement stmt = connection.prepareStatement
                        ("insert into travel_log(employee_id, destination, start_date, end_date, trip_status) values(?,?,?,?,?)");

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
            System.out.println("Logger needed here");
        }
        String email = rs.getString("email");

        TripStatus tripStatus = null;
        try{
            tripStatus = TripStatus.getByName(statusString);
        }catch (InvalidEnumValueException e){
            System.out.println("Logger needed here");
        }

        return new TravelLog(id, new Employee.Builder(firstName, lastName, role, department)
                .withId(employeeId)
                .withEmail(email)
                .build(),
                destination, startDate, endDate, tripStatus);
    }
}
