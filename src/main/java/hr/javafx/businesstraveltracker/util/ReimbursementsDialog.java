package hr.javafx.businesstraveltracker.util;

import hr.javafx.businesstraveltracker.enums.ReimbursementStatus;
import hr.javafx.businesstraveltracker.repository.ReimbursementRepository;
import javafx.application.Platform;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

import java.util.Optional;

public class ReimbursementsDialog {

    private ReimbursementsDialog(){}

    private static final ReimbursementRepository reimbursementRepository = new ReimbursementRepository();

    private static boolean isDialogOpen = false;

    public static void show(){
        int unapprovedReimbursementsCount = reimbursementRepository.findAll().stream()
                .filter(reimbursement ->
                        reimbursement.getStatus().equals(ReimbursementStatus.UNAPPROVED))
                .toList().size();

        if(unapprovedReimbursementsCount > 0 && !isDialogOpen){
            isDialogOpen = true;

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Reimbursements");
            if(unapprovedReimbursementsCount > 1) {
                dialog.setHeaderText("There are " + unapprovedReimbursementsCount
                        + " unapproved reimbursements!");
            }else dialog.setHeaderText("There is one unapproved reimbursement!");

            dialog.setContentText("Click on 'Open Reimbursements' button to manage reimbursements.");

            ButtonType openReimbursementsButtonType = new ButtonType("Open Reimbursements", ButtonBar.ButtonData.YES);
            ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(openReimbursementsButtonType, okButtonType);

            Platform.runLater(()->{
                Optional<ButtonType> result = dialog.showAndWait();
                result.ifPresent(response -> {
                    if (response.getButtonData() == ButtonBar.ButtonData.YES) {
                        SceneManager.getInstance().showReimbursementSearch();
                    }
                    isDialogOpen = false;
                });
            });
        }
    }
}
