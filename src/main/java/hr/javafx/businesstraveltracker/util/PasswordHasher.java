package hr.javafx.businesstraveltracker.util;

import com.password4j.Password;

public class PasswordHasher {
    public static String hashPassword(String password) {
        return Password.hash(password)
                .addRandomSalt()
                .withArgon2()
                .getResult();
    }

    public static Boolean checkPassword(String password, String hashedPassword) {
        return Password.check(password, hashedPassword).withArgon2();
    }
}
