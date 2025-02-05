package hr.javafx.businesstraveltracker.repository;

import hr.javafx.businesstraveltracker.enums.UserPrivileges;
import hr.javafx.businesstraveltracker.exception.RepositoryAccessException;
import hr.javafx.businesstraveltracker.model.User;
import hr.javafx.businesstraveltracker.util.DataValidation;

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
                if(fileRows.isEmpty()) return users;

                for (String row : fileRows) {
                    String[] userData = row.split(";");
                    if(row.chars().filter(ch -> ch == ';').count() != 4 || userData.length != 5 ||
                            !DataValidation.isValidNumber(userData[0]) || !DataValidation.isValidNumber(userData[4]))
                        continue;

                    Long userId = Long.valueOf(userData[0]);
                    String username = userData[1];
                    String hashedPassword = userData[2];
                    String privileges = userData[3];
                    long employeeId = Long.parseLong(userData[4]);

                    User.Builder builder = new User.Builder(username, UserPrivileges.valueOf(privileges))
                            .withId(userId).withHashedPassword(hashedPassword);
                    if(employeeId >= 0)  builder.withEmployeeId(employeeId);

                    users.add(builder.build());
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
                writer.println(user.getId() + ";" + user.getUsername() + ";" + user.getHashedPassword() + ";" + user.getPrivileges() +
                        ";" + (user.getEmployeeId() == null ? -1 : user.getEmployeeId()));
            } catch (IOException e) {
                throw new RepositoryAccessException(e);
            }
        }
    }

    public void save(List<User> users){
        synchronized (lock){
            try(PrintWriter writer = new PrintWriter(new FileWriter(USER_DATA_FILE_PATH))){
                for(User user : users){
                    writer.println(user.getId() + ";" + user.getUsername() + ";" + user.getHashedPassword() + ";" + user.getPrivileges() +
                            ";" + (user.getEmployeeId() == null ? -1 : user.getEmployeeId()));
                }
            }catch (IOException e){
                throw new RepositoryAccessException(e);
            }
        }
    }

    public void delete(User user){
        List<User> userList = findAllUsers();
        save(userList.stream().filter(item -> !item.getId().equals(user.getId())).toList());
    }

    public void update(User user){
        List<User> userList = findAllUsers();
        userList = userList.stream().map(item -> {
            if(user.getId().equals(item.getId())){
                return user;
            }
            return item;
        }).toList();

        save(userList);
    }
}
