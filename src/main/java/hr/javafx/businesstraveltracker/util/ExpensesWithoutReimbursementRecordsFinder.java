package hr.javafx.businesstraveltracker.util;

import hr.javafx.businesstraveltracker.model.Expense;
import hr.javafx.businesstraveltracker.repository.ExpenseRepository;
import hr.javafx.businesstraveltracker.repository.ReimbursementRepository;

import java.util.List;

/**
 * Klasa za pronalazak Expense objekata za koje ne postoji dedicirani Reimbursement objekt,
 * odnosno pronalazi troškove za koje nadoknada troškova nije odobrena niti je u procesu odobravanja.
 */
public class ExpensesWithoutReimbursementRecordsFinder {

    private static final ReimbursementRepository reimbursementRepository = new ReimbursementRepository();
    private static final ExpenseRepository expenseRepository = new ExpenseRepository();

    private ExpensesWithoutReimbursementRecordsFinder() {}

    /**
     * Pronalazi sve objekte klase Expense koji nemaju dedicirani Reimbursement objekt
     * @return listu objekata klase Expense koji nemaju dedicirani Reimbursement objekt
     */
    public static List<Expense> find(){
        List<Long> idsOfExpensesWithReimbursements = reimbursementRepository.findAll().stream()
                .map(reimbursement -> reimbursement.getExpense().getId())
                .toList();

        return   expenseRepository.findAll().stream()
                .filter(expense -> !idsOfExpensesWithReimbursements.contains(expense.getId()))
                .toList();
    }
}
