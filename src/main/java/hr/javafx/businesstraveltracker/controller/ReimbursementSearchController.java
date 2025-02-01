package hr.javafx.businesstraveltracker.controller;

import hr.javafx.businesstraveltracker.enums.ReimbursementStatus;
import hr.javafx.businesstraveltracker.model.Expense;
import hr.javafx.businesstraveltracker.model.Reimbursement;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.Date;

public class ReimbursementSearchController {

    @FXML
    public ChoiceBox<Expense> expenseChoiceBox;

    @FXML
    public ChoiceBox<ReimbursementStatus> reimbursementStatusChoiceBox;

    @FXML
    public DatePicker startDatePicker;

    @FXML
    public DatePicker endDatePicker;

    @FXML
    public TableView<Reimbursement> reimbursementTableView;

    @FXML
    public TableColumn<Reimbursement, Long> idColumn;

    @FXML
    public TableColumn<Reimbursement, String> expenseColumn;

    @FXML
    public TableColumn<Reimbursement, String> statusColumn;

    @FXML
    public TableColumn<Reimbursement, Date> approvalDateColumn;

    public void initialize() {}

    public void filterReimbursements() {}
}
