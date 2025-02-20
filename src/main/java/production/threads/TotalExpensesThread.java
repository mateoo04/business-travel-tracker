package production.threads;

import hr.javafx.businesstraveltracker.BusinessTravelTrackerApplication;
import hr.javafx.businesstraveltracker.repository.ExpenseRepository;
import hr.javafx.businesstraveltracker.util.SceneManager;
import javafx.application.Platform;

import java.math.BigDecimal;

/**
 * Thread koji dohvaća ukupan iznos troškova i zapisuje rezultat u naslov prozora.
 */
public class TotalExpensesThread extends Thread {

    private final ExpenseRepository expenseRepository = new ExpenseRepository();

    /**
     * Pokretanje niti.
     */
    @Override
    public void run() {
        BigDecimal totalExpenses = expenseRepository.getTotalExpensesAmount();

        String title = SceneManager.APP_TITLE +
                " (Total expenses: " + totalExpenses + "€)";
        Platform.runLater(() -> BusinessTravelTrackerApplication.getPrimaryStage().setTitle(title));
    }
}
