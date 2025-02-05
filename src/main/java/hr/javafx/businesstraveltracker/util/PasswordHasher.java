package hr.javafx.businesstraveltracker.util;

import com.password4j.Password;

/**
 * Klasa zadužena za hashiranje i provjeru valjanosti zaporke.
 */
public class PasswordHasher {

    private PasswordHasher() {}

    /**
     * Hashira String zaporke.
     * @param password zaporka
     * @return String vrijednost hashirane zaporke
     */
    public static String hashPassword(String password) {
        return Password.hash(password)
                .addRandomSalt()
                .withArgon2()
                .getResult();
    }

    /**
     * Provjerava je li unesena zaporka jednaka hashiranoj
     * @param password unesena zaporka
     * @param hashedPassword hashirana zaporka
     * @return boolean koji predstavlja je li unesena zaporka jednaka hashiranoj
     */
    public static boolean checkPassword(String password, String hashedPassword) {
        return Password.check(password, hashedPassword).withArgon2();
    }
}
