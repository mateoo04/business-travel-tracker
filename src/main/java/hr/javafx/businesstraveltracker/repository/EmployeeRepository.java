package hr.javafx.businesstraveltracker.repository;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import hr.javafx.businesstraveltracker.model.Employee;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Klasa koja upravlja zapisima zaposlenika.
 */
public class EmployeeRepository implements CrudRepository<Employee> {

    private final HttpRequestHandler httpRequestHandler = new HttpRequestHandler();

    private static final String URI_PATH = "/employee";

    private static final Gson gson = new Gson();

    /**
     * Pronalazi i vraća sve zaposlenike u bazi podataka.
     *
     * @return sve spremljene zaposlenike
     */
    @Override
    public List<Employee> findAll() {
        List<Employee> employees = new ArrayList<>();
        String response = httpRequestHandler.makeHttpRequest(URI_PATH, "GET");
        JsonArray jsonArray = JsonParser.parseString(response).getAsJsonArray();
        jsonArray.forEach(item -> employees.add(gson.fromJson(gson.toJson(item), Employee.class)));

        return employees;
    }

    /**
     * Pronalazi zaposlenika s odgovarajućim ID-jem
     *
     * @param id ID zapisa
     * @return Optional koji sadržava zaposlenika s odgovarajućim ID-jem ako je pronađen
     */
    @Override
    public Optional<Employee> findById(Long id) {
        String response = httpRequestHandler.makeHttpRequest(URI_PATH + "/" + id, "GET");

        if(response.isEmpty()) return Optional.empty();

        return Optional.of(gson.fromJson(response, Employee.class));
    }

    /**
     * Sprema zaposlenika u bazu podataka.
     *
     * @param entity
     */
    @Override
    public void save(Employee entity) {
        httpRequestHandler.makeHttpRequest(URI_PATH, "POST", gson.toJson(entity));

    }

    /**
     * Ažurira zaposlenika u bazi podataka.
     *
     * @param entity
     */
    @Override
    public void update(Employee entity) {
        httpRequestHandler.makeHttpRequest(URI_PATH, "PUT", gson.toJson(entity));
    }

    /**
     * Briše zaposlenika sa zadanim ID-jem
     *
     * @param id ID zapisa zaposlenika
     */
    @Override
    public void deleteById(Long id) {
        httpRequestHandler.makeHttpRequest(URI_PATH + "/" + id, "DELETE");
    }
}
