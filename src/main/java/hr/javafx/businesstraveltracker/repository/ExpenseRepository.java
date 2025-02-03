package hr.javafx.businesstraveltracker.repository;

import hr.javafx.businesstraveltracker.controller.LogInController;
import hr.javafx.businesstraveltracker.enums.Department;
import hr.javafx.businesstraveltracker.enums.TripStatus;
import hr.javafx.businesstraveltracker.exception.InvalidEnumValueException;
import hr.javafx.businesstraveltracker.exception.RepositoryAccessException;
import hr.javafx.businesstraveltracker.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import production.threads.DatabaseOperationThread;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class ExpenseRepository implements CrudRepository<Expense> {

    private static Logger log = LoggerFactory.getLogger(LogInController.class);

    @Override
    public List<Expense> findAll() {
        List<Expense> expenses = new ArrayList<>();

        DatabaseOperationThread thread = new DatabaseOperationThread(()->{
            try(Connection connection = Database.connectToDatabase()){
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("select expense.id as expense_id, travel_log_id, expense_category_id, amount as expense_amount, " +
                        "expense.description as expense_description, date as expense_date, employee_id, destination, " +
                        "travel_log.start_date as travel_log_start_date, travel_log.end_date as travel_log_end_date, " +
                        "trip_status, expense_category.name as expense_category_name, expense_category.description " +
                        "as expense_category_description, first_name as employee_first_name, last_name as employee_last_name," +
                        "role as employee_role, email as employee_email, department_id as employee_department_id " +
                        "from expense " +
                        "join travel_log on expense.travel_log_id = travel_log.id " +
                        "join expense_category on expense.expense_category_id = expense_category.id " +
                        "join employee on travel_log.employee_id = employee.id");
                while(rs.next()) expenses.add(extractExpenseFromResultSet(rs));
            }catch (SQLException | IOException e){
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

        return expenses;
    }

    @Override
    public Optional<Expense> findById(Long id) {
        AtomicReference<Expense> expenseAtomicReference  = new AtomicReference<>();

        DatabaseOperationThread thread = new DatabaseOperationThread(()->{
            try(Connection connection = Database.connectToDatabase()){
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("select expense.id as expense_id, travel_log_id, expense_category_id, amount as expense_amount, " +
                        "expense.description as expense_description, date as expense_date, employee_id, destination, " +
                        "travel_log.start_date as travel_log_start_date, travel_log.end_date as travel_log_end_date, " +
                        "trip_status, expense_category.name as expense_category_name, expense_category.description " +
                        "as expense_category_description, first_name as employee_first_name, last_name as employee_last_name," +
                        "role as employee_role, email as employee_email, department_id as employee_department_id from expense join travel_log " +
                        "on expense.travel_log_id = travel_log.id join expense_category " +
                        "on expense.expense_category_id = expense_category.id join employee " +
                        "on travel_log.employee_id = employee.id where expense.id = " + id);
                if(rs.next()) expenseAtomicReference.set(extractExpenseFromResultSet(rs));
            }catch (SQLException | IOException e){
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

        return Optional.ofNullable(expenseAtomicReference.get());
    }

    @Override
    public void save(Expense expense) {
        DatabaseOperationThread thread = new DatabaseOperationThread(()->{
            try(Connection connection = Database.connectToDatabase()){
                PreparedStatement stmt = connection.prepareStatement
                        ("insert into expense(travel_log_id, expense_category_id, amount, " +
                                "description, date) values (?,?,?,?,?)");
                stmt.setInt(1, expense.getTravelLog().getId().intValue());
                stmt.setInt(2, expense.getCategory().getId().intValue());
                stmt.setBigDecimal(3, expense.getAmount());
                stmt.setString(4, expense.getDescription());
                stmt.setDate(5, Date.valueOf(expense.getDate()));

                stmt.executeUpdate();
            }catch (SQLException | IOException e){
                throw new RepositoryAccessException(e);
            }
        });
        thread.start();
    }

    @Override
    public void update(Expense entity) {
        DatabaseOperationThread thread = new DatabaseOperationThread(()->{
           try(Connection connection = Database.connectToDatabase()){
               PreparedStatement stmt = connection.prepareStatement
                       ("update expense set travel_log_id = ?, expense_category_id = ?," +
                               "amount = ?, description = ?, date = ? where id = ?");
               stmt.setInt(1, entity.getTravelLog().getId().intValue());
               stmt.setInt(2, entity.getCategory().getId().intValue());
               stmt.setBigDecimal(3, entity.getAmount());
               stmt.setString(4, entity.getDescription());
               stmt.setDate(5, Date.valueOf(entity.getDate()));
               stmt.setInt(6, entity.getId().intValue());
               stmt.executeUpdate();
           }catch (SQLException | IOException e){
               throw new RepositoryAccessException(e);
           }
        });
        thread.start();
    }

    @Override
    public void deleteById(Long id) {
        deleteFromTable("expense",id);
    }

    public static Expense extractExpenseFromResultSet(ResultSet rs) throws SQLException {
        Long expenseId = rs.getLong("expense_id");
        BigDecimal amount = rs.getBigDecimal("expense_amount");
        String description = rs.getString("expense_description");
        LocalDate expenseDate = rs.getDate("expense_date").toLocalDate();

        Long travelLogId = rs.getLong("travel_log_id");
        String destination = rs.getString("destination");
        LocalDate travelLogStartDate = rs.getDate("travel_log_start_date").toLocalDate();
        LocalDate travelLogEndDate = rs.getDate("travel_log_end_date").toLocalDate();
        TripStatus tripStatus = null;
        try {
            tripStatus = TripStatus.getByName(rs.getString("trip_status"));
        } catch (InvalidEnumValueException e) {
            tripStatus = TripStatus.PLANNED;
        }

        Long employeeId = rs.getLong("employee_id");
        String employeeFirstName = rs.getString("employee_first_name");
        String employeeLastName = rs.getString("employee_last_name");
        String employeeRole = rs.getString("employee_role");
        String employeeEmail = rs.getString("employee_email");
        Department employeeDepartment = null;
        try {
            employeeDepartment = Department.getById(rs.getLong("employee_department_id"));
        } catch (InvalidEnumValueException e){
            log.error(e.getMessage());
        }

        Long expenseCategoryId = rs.getLong("expense_category_id");
        String expenseCategoryName = rs.getString("expense_category_name");
        String expenseCategoryDescription = rs.getString("expense_category_description");

        ExpenseCategory expenseCategory =
                new ExpenseCategory(expenseCategoryId, expenseCategoryName, expenseCategoryDescription);

        TravelLog travelLog = new TravelLog(
                travelLogId,
                new Employee.Builder(employeeFirstName, employeeLastName, employeeRole, employeeDepartment)
                        .withId(employeeId)
                        .withEmail(employeeEmail)
                        .build(),
                destination, travelLogStartDate, travelLogEndDate, tripStatus);

        return new Expense(expenseId, travelLog, expenseCategory, amount, description, expenseDate);
    }
}
