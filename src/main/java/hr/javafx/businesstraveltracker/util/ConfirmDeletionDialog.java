package hr.javafx.businesstraveltracker.util;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

import java.util.Optional;

/**
 * Klasa dijaloga kojim se potvrđuje brisanje podataka iz baze podataka.
 */
public class ConfirmDeletionDialog {
    private ConfirmDeletionDialog(){}

    /**
     * Prikazuje dijalog kojim se potvrđuje brisanje podataka iz baze podataka
     * @param item podatak za brisanje
     * @param dialog dijalog
     * @param onConfirmation radnja koja se izvršava na potvrdu brisanja
     * @param <T> klasa entiteta koji će bit obrisan iz baze podataka
     */
    public static <T> void show(T item, Dialog<ButtonType> dialog, Runnable onConfirmation){
        dialog.setContentText(item.toString());

        ButtonType confirmButtonType = new ButtonType("Delete", ButtonBar.ButtonData.YES);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, cancelButtonType);

        Optional<ButtonType> result = dialog.showAndWait();
        result.ifPresent(response ->{
            if (response == confirmButtonType){
                onConfirmation.run();
            }
        });
    }
}
