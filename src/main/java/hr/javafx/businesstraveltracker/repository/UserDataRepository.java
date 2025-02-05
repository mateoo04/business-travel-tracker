package hr.javafx.businesstraveltracker.repository;

import hr.javafx.businesstraveltracker.enums.UserPrivileges;
import hr.javafx.businesstraveltracker.exception.RepositoryAccessException;
import hr.javafx.businesstraveltracker.model.User;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Klasa repozitorija sa korisničkim podacima.
 */
public class UserDataRepository {

    private static final String USER_DATA_FILE_PATH = "dat/user_data.dat";

    private static final Object lock = new Object();

    /**
     * Pronalazi i vraća sve spremljene korisnike.
     * @return sve spremljene korisnike
     */
    public List<User> findAllUsers(){
        synchronized (lock){
            List<User> users = new ArrayList<>();

            try (Stream<String> stream = Files.lines(Path.of(USER_DATA_FILE_PATH))) {
                List<String> fileRows = stream.toList();

                for (String row : fileRows) {
                    String[] userData = row.split(";");
                    String username = userData[0];
                    String hashedPassword = userData[1];
                    String privileges = userData[2];

                    users.add(new User(username, hashedPassword, UserPrivileges.valueOf(privileges)));
                }
            } catch (IOException e) {
                throw new RepositoryAccessException(e);
            }

            return users;
        }
    }

    /**
     * Sprema novog korisnika u repozitorij.
     * @param user novi korisnik
     */
    public void save(User user){
        synchronized (lock){
            try (PrintWriter writer = new PrintWriter(new FileWriter(USER_DATA_FILE_PATH, true))) {
                writer.println(user.username() + ";" + user.hashedPassword() + ";" + user.privileges());
            } catch (IOException e) {
                throw new RepositoryAccessException(e);
            }
        }
    }

    public void save(List<User> users){
        synchronized (lock){
            try(PrintWriter writer = new PrintWriter(new FileWriter(USER_DATA_FILE_PATH))){
                for(User user : users){
                    writer.println(user.username() + ";" + user.hashedPassword() + ";" + user.privileges());
                }
            }catch (IOException e){
                throw new RepositoryAccessException(e);
            }
        }
    }
}
