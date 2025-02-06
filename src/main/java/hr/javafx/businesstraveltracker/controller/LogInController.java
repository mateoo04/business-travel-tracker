package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.enums.UserPrivileges;
import hr.javafx.businesstraveltracker.model.User;
import hr.javafx.businesstraveltracker.repository.UserDataRepository;
import hr.javafx.businesstraveltracker.util.PasswordHasher;
import hr.javafx.businesstraveltracker.util.ReimbursementsDialog;
import hr.javafx.businesstraveltracker.util.SceneManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import production.threads.TotalExpensesThread;

import java.util.List;

import static javafx.animation.Animation.INDEFINITE;

/**
 * Kontroler za prijavu.
 */
public class LogInController {

    @FXML
    public TextField usernameTextField;

    @FXML
    public PasswordField passwordTextField;

    private final UserDataRepository userDataRepository = new UserDataRepository();

    private static final SceneManager sceneManager = SceneManager.getInstance();
    
    private static final String ADMIN = "admin";

    private static Timeline totalExpensesTimeline = null;

    private static Timeline reimbursementNotifTimeline = null;

    private static User currentUser;
    /**
     * Inicijalizira ekran za prijavu.
     */
    public void initialize() {
        usernameTextField.setOnKeyPressed(this::handleKeyPressed);
        passwordTextField.setOnKeyPressed(this::handleKeyPressed);

        cancelTimelines();
    }

    /**
     * Olakšava prijavu upotrebom tipkovnike.
     * @param keyEvent događaj tipkovnice
     */
    private void handleKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ENTER) {
            if(usernameTextField.isFocused())
                passwordTextField.requestFocus();
            else if(passwordTextField.isFocused())
                logIn();
        }
    }

    /**
     * Provodi prijavu pri čemu provjerava unesene podatke.
     */
    public void logIn(){
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        
        if(username.equals(ADMIN) && password.equals(ADMIN)){
            setCurrentUser(new User.Builder(ADMIN, UserPrivileges.HIGH).build());
            setApp();
        }else {
            List<User> users = userDataRepository.findAllUsers();

            boolean userFound = false;
            for (User user : users) {
                if (username.equals(user.getUsername())) {
                    userFound = true;
                    if (PasswordHasher.checkPassword(password, user.getHashedPassword())) {
                        setCurrentUser(user);
                        setApp();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Password Mismatch");
                        alert.setHeaderText("Incorrect Password Entered!");
                        alert.showAndWait();
                    }
                    break;
                }
            }
            if (!userFound) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("No such user");
                alert.setHeaderText("User not found!");
                alert.showAndWait();
            }
        }
    }

    private static void setApp(){
        sceneManager.showEmployeeSearch();
        if(getCurrentUser().getPrivileges().equals(UserPrivileges.HIGH) && reimbursementNotifTimeline == null) {
            reimbursementNotifTimeline = new Timeline(
                    new KeyFrame(Duration.ZERO, _ -> ReimbursementsDialog.show()),
                    new KeyFrame(Duration.seconds(10), _ -> {
                if(getCurrentUser().getPrivileges().equals(UserPrivileges.HIGH)) {
                    ReimbursementsDialog.show();
                }else{
                    reimbursementNotifTimeline.stop();
                }
            }));
            reimbursementNotifTimeline.setCycleCount(INDEFINITE);
            reimbursementNotifTimeline.play();
        }

        if(totalExpensesTimeline == null){
            totalExpensesTimeline = new Timeline(new KeyFrame(Duration.seconds(2), _ -> {
                TotalExpensesThread thread = new TotalExpensesThread();
                thread.start();
            }));
            totalExpensesTimeline.setCycleCount(INDEFINITE);
            totalExpensesTimeline.play();
        }
    }

    private static void cancelTimelines(){
        if(totalExpensesTimeline != null) {
            totalExpensesTimeline.stop();
            totalExpensesTimeline = null;
        }
        if(reimbursementNotifTimeline != null) {
            reimbursementNotifTimeline.stop();
            reimbursementNotifTimeline = null;
        }
    }

    /**
     * Postavlja trenutnog korisnika
     * @param user korisnik
     */
    private static void setCurrentUser(User user){
        currentUser = user;
    }

    /**
     * Vraća trenutnog korisnika
     * @return trenutni korisnik
     */
    public static User getCurrentUser() {
        return currentUser;
    }
}
