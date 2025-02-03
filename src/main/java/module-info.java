module hr.javafx.businesstraveltracker.businesstraveltracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires password4j;
    requires java.sql;
    requires java.desktop;
    requires org.slf4j;

    exports hr.javafx.businesstraveltracker.controller;
    exports hr.javafx.businesstraveltracker;
    exports hr.javafx.businesstraveltracker.model;
    exports hr.javafx.businesstraveltracker.enums;

    opens hr.javafx.businesstraveltracker to javafx.fxml;
    opens hr.javafx.businesstraveltracker.model to javafx.fxml;
    opens hr.javafx.businesstraveltracker.controller to javafx.fxml;
    opens hr.javafx.businesstraveltracker.enums to javafx.fxml;
}