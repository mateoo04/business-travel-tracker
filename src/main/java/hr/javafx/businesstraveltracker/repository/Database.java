package hr.javafx.businesstraveltracker.repository;

import hr.javafx.businesstraveltracker.exception.RepositoryAccessException;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Database {

    private static Boolean activeConnectionWithDatabase = false;

    private Database(){}

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

    public static Boolean isActiveConnectionWithDatabase() {
        return activeConnectionWithDatabase;
    }

    public static void setActiveConnectionWithDatabase(Boolean activeConnectionWithDatabase) {
        Database.activeConnectionWithDatabase = activeConnectionWithDatabase;
    }
}
