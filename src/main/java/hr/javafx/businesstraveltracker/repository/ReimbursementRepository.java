package hr.javafx.businesstraveltracker.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import hr.javafx.businesstraveltracker.model.Reimbursement;
import hr.javafx.businesstraveltracker.util.LocalDateAdapter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Klasa koja upravalja repozitorijem nadoknade troškova.
 */
public class ReimbursementRepository implements CrudRepository<Reimbursement> {

    private final HttpRequestHandler httpRequestHandler = new HttpRequestHandler();

    private static final String URI_PATH = "/reimbursement";

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    /**
     * Pronalazi i vraća sve zabilješke nadoknadi troskova koje su spremljene u bazu podataka.
     * @return sve zabilješke nadoknadi troskova koje su spremljene u bazu podataka
     */
    @Override
    public List<Reimbursement> findAll() {
        List<Reimbursement> reimbursements = new ArrayList<>();
        String response = httpRequestHandler.makeHttpRequest(URI_PATH, "GET");
        JsonArray jsonArray = JsonParser.parseString(response).getAsJsonArray();
        jsonArray.forEach(item -> reimbursements.add(gson.fromJson(item, Reimbursement.class)));

        return reimbursements;
    }

    /**
     * Pronalazi zabilješku nadoknade troškova sa zadanim ID-jem
     * @param id ID zapisa
     * @return Optional koji sadržava nadoknadu troška sa zadanim ID-jem ako je pronađen
     */
    @Override
    public Optional<Reimbursement> findById(Long id) {
        String response = httpRequestHandler.makeHttpRequest(URI_PATH + "/"+id, "GET");

        if(response.isEmpty()) return Optional.empty();

        return Optional.of(gson.fromJson(response, Reimbursement.class));
    }

    /**
     * Sprema zabilješku nadoknade troškova u bazu podataka.
     * @param entity objekt koji će biti spremljen.
     */
    @Override
    public void save(Reimbursement entity) {
        httpRequestHandler.makeHttpRequest(URI_PATH, "POST", gson.toJson(entity));
    }

    /**
     * Ažurira zabilješku nadoknade troškova u bazi podataka.
     * @param entity objekt koji će biti ažuriran
     */
    @Override
    public void update(Reimbursement entity) {
        httpRequestHandler.makeHttpRequest(URI_PATH, "PUT", gson.toJson(entity));
    }

    /**
     * Briše zabilješku sa odgovrajućim ID-jem iz baze podataka.
     * @param id ID zabilješke
     */
    @Override
    public void deleteById(Long id) {
        httpRequestHandler.makeHttpRequest(URI_PATH + "/"+id, "DELETE");
    }
}
