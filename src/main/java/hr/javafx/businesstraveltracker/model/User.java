package hr.javafx.businesstraveltracker.model;

import hr.javafx.businesstraveltracker.enums.UserPrivileges;

import java.io.Serializable;

public record User(String username, String hashedPassword, UserPrivileges privileges) implements Serializable {
}
