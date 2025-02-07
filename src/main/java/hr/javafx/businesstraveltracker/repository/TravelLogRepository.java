package hr.javafx.businesstraveltracker.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import hr.javafx.businesstraveltracker.model.TravelLog;
import hr.javafx.businesstraveltracker.util.LocalDateAdapter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Klasa koja upravlja repozitorijem zabilješki putovanja.
 */
public class TravelLogRepository implements CrudRepository<TravelLog> {

    private final HttpRequestHandler httpRequestHandler = new HttpRequestHandler();

    private static final String URI_PATH = "/travel-log";

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    /**
     * Pronalazi i vraća sve zabilješke troškova iz baze podataka.
     * @return sve zabilješke troškova iz baze podataka
     */
    @Override
    public List<TravelLog> findAll() {
        String response = httpRequestHandler.makeHttpRequest(URI_PATH, "GET");
        List<TravelLog> travelLogs = new ArrayList<>();
        JsonArray jsonArray = JsonParser.parseString(response).getAsJsonArray();
        jsonArray.forEach(item -> travelLogs.add(gson.fromJson(item, TravelLog.class)));

        return travelLogs;
    }

    /**
     * Pronalazi zabilješku putovanja sa odgovarajućim ID-jem
     * @param id ID zapisa
     * @return Optional koji sadržava zabiješku putovanja sa zadanim ID-jem ako je pronađen
     */
    @Override
    public Optional<TravelLog> findById(Long id) {
        String response = httpRequestHandler.makeHttpRequest(URI_PATH+"/"+id, "GET");

        if(response.isEmpty()) return Optional.empty();

        return Optional.of(gson.fromJson(response, TravelLog.class));
    }

    /**
     * Sprema zabilješku putovanja u bazu podataka
     * @param entity
     */
    @Override
    public void save(TravelLog entity) {
        httpRequestHandler.makeHttpRequest(URI_PATH, "POST", gson.toJson(entity));
    }

    /**
     * Ažurira zabilješku putovanja u bazi podataka.
     * @param entity
     */
    @Override
    public void update(TravelLog entity) {
        httpRequestHandler.makeHttpRequest(URI_PATH, "PUT", gson.toJson(entity));
    }

    /**
     * Briše zabilješku putovanja iz baze podataka.
     * @param id ID zapisa
     */
    @Override
    public void deleteById(Long id) {
        httpRequestHandler.makeHttpRequest(URI_PATH + "/" +id,"DELETE");
    }
}
