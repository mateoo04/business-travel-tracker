package hr.javafx.businesstraveltracker.repository;

import hr.javafx.businesstraveltracker.enums.ReimbursementStatus;
import hr.javafx.businesstraveltracker.exception.InvalidEnumValueException;
import hr.javafx.businesstraveltracker.exception.RepositoryAccessException;
import hr.javafx.businesstraveltracker.model.Expense;
import hr.javafx.businesstraveltracker.model.Reimbursement;
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

public class ReimbursementRepository implements CrudRepository<Reimbursement> {

    private static Logger log = LoggerFactory.getLogger(ReimbursementRepository.class);

    @Override
    public List<Reimbursement> findAll() {
        List<Reimbursement> reimbursements = new ArrayList<>();

        DatabaseOperationThread thread = new DatabaseOperationThread(()->{
            try(Connection connection = Database.connectToDatabase()){
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery
                        ("select reimbursement.id as reimbursement_id, expense_id, approval_date, reimbursement.status as reimbursement_status, " +
                                "travel_log_id, expense_category_id, amount as expense_amount, " +
                                "expense.description as expense_description, date as expense_date, employee_id, destination, " +
                                "travel_log.start_date as travel_log_start_date, travel_log.end_date as travel_log_end_date, " +
                                "trip_status, expense_category.name as expense_category_name, expense_category.description " +
                                "as expense_category_description, first_name as employee_first_name, last_name as employee_last_name," +
                                "role as employee_role, email as employee_email, department_id as employee_department_id " +
                                "from reimbursement " +
                                "join expense on reimbursement.expense_id = expense.id " +
                                "join travel_log on expense.travel_log_id = travel_log.id " +
                                "join expense_category on expense.expense_category_id = expense_category.id " +
                                "join employee on travel_log.employee_id = employee.id");

                while(rs.next()) reimbursements.add(extractReimbursementFromResultSet(rs));
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

        return reimbursements;
    }

    @Override
    public Optional<Reimbursement> findById(Long id) {
        AtomicReference<Reimbursement> reimbursementAtomicReference = new AtomicReference<>();

        DatabaseOperationThread thread = new DatabaseOperationThread(()->{
            try(Connection connection = Database.connectToDatabase()){
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery
                        ("select reimbursement.id as reimbursement_id, expense_id, approval_date, reimbursement.status as reimbursement_status, " +
                                "travel_log_id, expense_category_id, amount as expense_amount, " +
                                "expense.description as expense_description, date as expense_date, employee_id, destination, " +
                                "travel_log.start_date as travel_log_start_date, travel_log.end_date as travel_log_end_date, " +
                                "trip_status, expense_category.name as expense_category_name, expense_category.description " +
                                "as expense_category_description, first_name as employee_first_name, last_name as employee_last_name," +
                                "role as employee_role, email as employee_email, department_id as employee_department_id " +
                                "from reimbursement " +
                                "join expense on reimbursement.expense_id = expense.id " +
                                "join travel_log on expense.travel_log_id = travel_log.id " +
                                "join expense_category on expense.expense_category_id = expense_category.id " +
                                "join employee on travel_log.employee_id = employee.id " +
                                "where reimbursement.id = " + id);

                if(rs.next()) reimbursementAtomicReference.set(extractReimbursementFromResultSet(rs));
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

        return Optional.ofNullable(reimbursementAtomicReference.get());
    }

    @Override
    public void save(Reimbursement entity) {
        DatabaseOperationThread thread = new DatabaseOperationThread(()->{
            try(Connection connection = Database.connectToDatabase()){
                PreparedStatement stmt = connection.prepareStatement
                        ("insert into reimbursement(expense_id, approval_date, status) values(?,?,?)");
                stmt.setInt(1, entity.getExpense().getId().intValue());
                stmt.setDate(2, Date.valueOf(entity.getApprovalDate()));
                stmt.setObject(3, entity.getStatus().getStatus(), java.sql.Types.OTHER);
                stmt.executeUpdate();
            }catch (SQLException | IOException e){
                throw new RepositoryAccessException(e);
            }
        });
        thread.start();
    }

    @Override
    public void update(Reimbursement entity) {
        DatabaseOperationThread thread = new DatabaseOperationThread(()->{
            try(Connection connection = Database.connectToDatabase()){
                PreparedStatement stmt = connection.prepareStatement
                        ("update reimbursement set expense_id = ?, status = ? where id = ?");
                stmt.setInt(1, entity.getExpense().getId().intValue());
                stmt.setObject(2, entity.getStatus().getStatus(), java.sql.Types.OTHER);
                stmt.setInt(3, entity.getId().intValue());
                stmt.executeUpdate();
            }catch (SQLException | IOException e) {
                throw new RepositoryAccessException(e);
            }
        });
        thread.start();
    }

    @Override
    public void deleteById(Long id) {
        deleteFromTable("reimbursement",id);
    }

    private Reimbursement extractReimbursementFromResultSet(ResultSet rs) throws SQLException {
        Long reimbursementId = rs.getLong("reimbursement_id");
        ReimbursementStatus status;
        try {
            status = ReimbursementStatus.getByStatus(rs.getString("reimbursement_status"));
        } catch (InvalidEnumValueException e) {
            log.error(e.getMessage());
            status = ReimbursementStatus.UNAPPROVED;
        }
        LocalDate approvalDate = rs.getDate("approval_date").toLocalDate();

        Expense expense = ExpenseRepository.extractExpenseFromResultSet(rs);

        return new Reimbursement(reimbursementId, expense, status, approvalDate);
    }
}
