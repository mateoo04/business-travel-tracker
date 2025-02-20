package hr.javafx.businesstraveltracker.repository;

import hr.javafx.businesstraveltracker.exception.NoDataFoundException;
import hr.javafx.businesstraveltracker.exception.RepositoryAccessException;
import hr.javafx.businesstraveltracker.model.ChangeLog;
import hr.javafx.businesstraveltracker.model.Entity;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa koja upravlja zapisima promjena.
 */
public class ChangeLogRepository {

    private static final String LOG_FILE_PATH = "logs/change_log.dat";

    private static final Object lock = new Object();

    /**
     * Dodaje novi zapis promjene u repozitorij.
     * @param changeLog zapis promjene
     */
    public void log(ChangeLog<Entity> changeLog){
        synchronized (lock){
            List<ChangeLog<Entity>> changeLogs = null;
            try {
                changeLogs = findAll();
            } catch (NoDataFoundException e) {
                changeLogs = new ArrayList<>();
            }
            changeLogs.add(changeLog);

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(LOG_FILE_PATH))) {
                oos.writeObject(changeLogs);
            } catch (IOException e) {
                throw new RepositoryAccessException(e);
            }
        }
    }

    /**
     * Iščitava sve zapise o promjenama iz repozitorija
     * @return sve pronađene zapise promjena
     */
    public List<ChangeLog<Entity>> findAll() throws NoDataFoundException {
        synchronized (lock){
            File file = new File(LOG_FILE_PATH);
            if (!file.exists() || file.length() == 0) {
                throw new NoDataFoundException("No change logs found");
            }
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(LOG_FILE_PATH))) {
                return (List<ChangeLog<Entity>>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RepositoryAccessException(e);
            }
        }
    }
}
