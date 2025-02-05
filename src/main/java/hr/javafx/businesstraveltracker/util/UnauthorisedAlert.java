package hr.javafx.businesstraveltracker.util;

import javafx.scene.control.Alert;

public class UnauthorisedAlert {
    private UnauthorisedAlert(){}

    public static void show(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Unauthorised");
        alert.setHeaderText("Unauthorised action");
        alert.setContentText("You are not authorized to perform this action.");
        alert.showAndWait();
    }
}
