module hr.javafx.businesstraveltracker.businesstraveltracker {
    requires javafx.controls;
    requires javafx.fxml;


    opens hr.javafx.businesstraveltracker to javafx.fxml;
    exports hr.javafx.businesstraveltracker;
}