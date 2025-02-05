package hr.javafx.businesstraveltracker.model;

import hr.javafx.businesstraveltracker.enums.UserPrivileges;

import java.io.Serializable;

/**
 * Zapis koji predstavlja korisnika.
 * @param username korisničko ime
 * @param hashedPassword hashirana zaporka
 * @param privileges privilegije korisnika
 */
public record User(String username, String hashedPassword, UserPrivileges privileges) implements Serializable {
}
