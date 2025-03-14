module hr.javafx.businesstraveltracker.businesstraveltracker {
    requires javafx.fxml;
    requires password4j;
    requires org.slf4j;
    requires org.jfree.chart.fx;
    requires org.jfree.jfreechart;
    requires javafx.controls;
    requires java.desktop;
    requires java.net.http;
    requires com.google.gson;
    requires java.sql;

    exports hr.javafx.businesstraveltracker.controller;
    exports hr.javafx.businesstraveltracker;
    exports hr.javafx.businesstraveltracker.model;
    exports hr.javafx.businesstraveltracker.enums;

    opens hr.javafx.businesstraveltracker to javafx.fxml;
    opens hr.javafx.businesstraveltracker.model to javafx.fxml, com.google.gson;
    opens hr.javafx.businesstraveltracker.controller to javafx.fxml;
    opens hr.javafx.businesstraveltracker.enums to javafx.fxml;

}