package hr.javafx.businesstraveltracker.model;

import hr.javafx.businesstraveltracker.enums.UserPrivileges;

public record User(String username, String hashedPassword, UserPrivileges privileges) {
}
