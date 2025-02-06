package hr.javafx.businesstraveltracker.util;

import javafx.scene.control.Alert;

/**
 * Klasa koja omogućava stvaranje Alerta koji se prikazuje kada korisnik pokuša izvršiti radnju koja je izvan opsega
 * njegovih privilegija/dozvola.
 */
public class UnauthorisedAlert {
    private UnauthorisedAlert(){}

    /**
     * Prikazuje Alert.
     */
    public static void show(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Unauthorised");
        alert.setHeaderText("Unauthorised action");
        alert.setContentText("You are not authorized to perform this action.");
        alert.showAndWait();
    }
}
