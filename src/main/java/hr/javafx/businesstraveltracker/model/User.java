package hr.javafx.businesstraveltracker.model;

import hr.javafx.businesstraveltracker.enums.UserPrivileges;
import hr.javafx.businesstraveltracker.repository.UserDataRepository;
import hr.javafx.businesstraveltracker.util.PasswordHasher;

/**
 * Klasa koja predstavlja korisnika.
 */
public class User extends Entity{
    private String username;
    private String hashedPassword;
    private UserPrivileges privileges;
    private Long employeeId;

    private User(Long id, String username, String hashedPassword, UserPrivileges privileges, Long employeeId) {
        super(id);
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.privileges = privileges;
        this.employeeId = employeeId;
    }

    public User(User other){
        super(other.getId());
        this.username = other.getUsername();
        this.hashedPassword = other.getHashedPassword();
        this.privileges = other.getPrivileges();
        this.employeeId = other.getEmployeeId();
    }

    public static class Builder{
        private Long id;
        private final String username;
        private String hashedPassword;
        private final UserPrivileges privileges;
        private Long employeeId;

        public Builder(String username, UserPrivileges privileges){
            this.username = username;
            this.privileges = privileges;
        }

        public Builder withId(Long id){
            this.id = id;
            return this;
        }

        public Builder withPassword(String password){
            this.hashedPassword = PasswordHasher.hashPassword(password);
            return this;
        }

        public Builder withHashedPassword(String hashedPassword){
            this.hashedPassword = hashedPassword;
            return this;
        }

        public Builder withEmployeeId(Long employeeId){
            this.employeeId = employeeId;
            return this;
        }

        private static Long getNextUserId(){
            UserDataRepository userDataRepository = new UserDataRepository();
            return userDataRepository.findAllUsers().stream().map(User::getId).max(Long::compareTo).orElse(0L) + 1;
        }

        public User build(){
            return new User(this.id == null ? getNextUserId() : this.id, this.username, this.hashedPassword, this.privileges, this.employeeId);
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public void setPassword(String password){
        this.hashedPassword = PasswordHasher.hashPassword(password);
    }

    public UserPrivileges getPrivileges() {
        return privileges;
    }

    public void setPrivileges(UserPrivileges privileges) {
        this.privileges = privileges;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public String toString() {
        return "Username: " + username +
                "\nPrivileges: " + privileges +
                "\nEmployeeId: " + employeeId;
    }
}
