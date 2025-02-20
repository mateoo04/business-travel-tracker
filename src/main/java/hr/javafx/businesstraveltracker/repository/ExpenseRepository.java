package hr.javafx.businesstraveltracker.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import hr.javafx.businesstraveltracker.model.Expense;
import hr.javafx.businesstraveltracker.util.DataValidation;
import hr.javafx.businesstraveltracker.util.LocalDateAdapter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Klasa koja upravlja repozitorijem troškova.
 */
public class ExpenseRepository implements CrudRepository<Expense> {

    private final HttpRequestHandler httpRequestHandler = new HttpRequestHandler();

    private static final String URI_PATH = "/expense";

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    /**
     * Pronalazi i vraća sve troškove u bazi podataka.
     * @return sve troškove u bazi podataka
     */
    @Override
    public List<Expense> findAll() {
        List<Expense> expenses = new ArrayList<>();
        String response = httpRequestHandler.makeHttpRequest(URI_PATH, "GET");
        JsonArray jsonArray = JsonParser.parseString(response).getAsJsonArray();
        jsonArray.forEach(item -> expenses.add(gson.fromJson(item, Expense.class)));

        return expenses;
    }

    /**
     * Pronalazi zapis troška prema ID-u.
     * @param id ID zapisa
     * @return Optional koji sadržava zapis troška sa zadanim ID-jem ako je pronađen
     */
    @Override
    public Optional<Expense> findById(Long id) {
        String response = httpRequestHandler.makeHttpRequest(URI_PATH + "/"+id, "GET");

        if(response.isEmpty()) return Optional.empty();

        return Optional.of(gson.fromJson(response, Expense.class));
    }

    /**
     * Sprema zabilješku troška u bazu podataka.
     * @param expense objekt koji će biti spremljen
     */
    @Override
    public void save(Expense expense) {
        httpRequestHandler.makeHttpRequest(URI_PATH, "POST", gson.toJson(expense));
    }

    /**
     * Ažurira zapis troška u bazi podataka.
     * @param entity objekt koji će biti ažuriran
     */
    @Override
    public void update(Expense entity) {
        httpRequestHandler.makeHttpRequest(URI_PATH, "PUT", gson.toJson(entity));
    }

    /**
     * Briše zabilješku sa zadanim ID-jem iz baze podataka
     * @param id ID zabilješke troška
     */
    @Override
    public void deleteById(Long id) {
        httpRequestHandler.makeHttpRequest(URI_PATH + "/"+id, "DELETE");
    }

    /**
     * Dohvaća ukupne troškove.
     * @return ukupni iznos troškova.
     */
    public BigDecimal getTotalExpensesAmount(){
        String response = httpRequestHandler.makeHttpRequest(URI_PATH + "/total-amount", "GET");
        if(DataValidation.isValidDecimalNumber(response)){
            return new BigDecimal(response);
        }
        else return BigDecimal.ZERO;
    }
}
