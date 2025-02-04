package hr.javafx.businesstraveltracker.repository;

import hr.javafx.businesstraveltracker.exception.RepositoryAccessException;
import hr.javafx.businesstraveltracker.model.ChangeLog;
import hr.javafx.businesstraveltracker.model.Entity;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ChangeLogRepository {

    private static final String LOG_FILE_PATH = "logs/change_log.dat";

    public void log(ChangeLog<Entity> changeLog){
        List<ChangeLog<Entity>> changeLogs = findAll();
        changeLogs.add(changeLog);

        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(LOG_FILE_PATH))){
            oos.writeObject(changeLogs);
        } catch (IOException e) {
            throw new RepositoryAccessException(e);
        }
    }

    public List<ChangeLog<Entity>> findAll(){
        File file = new File(LOG_FILE_PATH);
        if(!file.exists() || file.length() == 0){
            return new ArrayList<>();
        }
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(LOG_FILE_PATH))){
            return (List<ChangeLog<Entity>>) ois.readObject();
        }catch (IOException | ClassNotFoundException e){
            throw new RepositoryAccessException(e);
        }
    }
}
