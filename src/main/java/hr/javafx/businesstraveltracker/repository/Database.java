package hr.javafx.businesstraveltracker.repository;


import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Klasa koja omogućava spajanje na bazu podataka.
 */
public class Database {

    private static Boolean activeConnectionWithDatabase = false;

    private Database(){}

    /**
     * Metoda za spajanje na bazu podataka.
     * @return vezu s bazom podataka
     * @throws IOException
     * @throws SQLException
     */
    public static synchronized Connection connectToDatabase() throws IOException, SQLException {
        Properties props = new Properties();

        try(FileReader reader = new FileReader("database.properties")){
            props.load(reader);
        }

        return DriverManager.getConnection(
                props.getProperty("databaseUrl"),
                props.getProperty("username"),
                props.getProperty("password"));
    }

    /**
     * Provjerava je li trenutno aktivna neka veza s bazom podataka.
     * @return boolen koji predstavlja je li trenutno aktivna neka veza s bazom podataka
     */
    public static Boolean isActiveConnectionWithDatabase() {
        return activeConnectionWithDatabase;
    }

    /**
     * Postavlja vrijednost aktivnosti veze s bazom podataka
     * @param activeConnectionWithDatabase
     */
    public static void setActiveConnectionWithDatabase(Boolean activeConnectionWithDatabase) {
        Database.activeConnectionWithDatabase = activeConnectionWithDatabase;
    }
}
