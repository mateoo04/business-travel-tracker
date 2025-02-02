module hr.javafx.businesstraveltracker.businesstraveltracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires password4j;
    requires java.sql;
    requires java.desktop;

    exports hr.javafx.businesstraveltracker.controller;
    opens hr.javafx.businesstraveltracker to javafx.fxml;
    exports hr.javafx.businesstraveltracker;
}