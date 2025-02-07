package hr.javafx.businesstraveltracker.repository;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import hr.javafx.businesstraveltracker.model.ExpenseCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Upravlja repozitorijem kategorija troškova.
 */
public class ExpenseCategoryRepository implements CrudRepository<ExpenseCategory> {
    
    private final HttpRequestHandler httpRequestHandler = new HttpRequestHandler();

    private static final String URI_PATH = "/expense-category";

    private static final Gson gson = new Gson();
    
    /**
     * Pronalazi i vraća sve spremljene kategorije troškova
     * @return spremljene kategorije troškova
     */
    @Override
    public List<ExpenseCategory> findAll() {
        String response = httpRequestHandler.makeHttpRequest(URI_PATH, "GET");
        List<ExpenseCategory> expenseCategories = new ArrayList<>();
        JsonArray jsonArray = JsonParser.parseString(response).getAsJsonArray();
        jsonArray.forEach(item -> expenseCategories.add(gson.fromJson(item, ExpenseCategory.class)));

        return expenseCategories;
    }

    /**
     * Pronalazi kategoriju troškova sa zadanim ID-jem
     * @param id ID zapisa
     * @return Optional koji sadržava kategoriju troškova sa zadanim ID-jem ako je pronađena
     */
    @Override
    public Optional<ExpenseCategory> findById(Long id) {
        String response = httpRequestHandler.makeHttpRequest(URI_PATH + "/" + id, "GET");

        if(response.isEmpty()) return Optional.empty();

        return Optional.of(gson.fromJson(response, ExpenseCategory.class));
    }

    /**
     * Sprema kategoriju troškova
     * @param entity
     */
    @Override
    public void save(ExpenseCategory entity){
        httpRequestHandler.makeHttpRequest(URI_PATH,"POST", gson.toJson(entity));
    }

    /**
     * Ažurira podatke o kategoriji troškova u bazi podataka
     * @param entity
     */
    @Override
    public void update(ExpenseCategory entity) {
        httpRequestHandler.makeHttpRequest(URI_PATH,"PUT",gson.toJson(entity));
    }

    /**
     * Briše zapis kategorije troškova sa zadanim ID-jem
     * @param id ID zapisa
     */
    @Override
    public void deleteById(Long id) {
        httpRequestHandler.makeHttpRequest(URI_PATH + "/" +id,"DELETE");
    }
}
