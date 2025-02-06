package production.threads;

import hr.javafx.businesstraveltracker.repository.Database;

/**
 * Klasa koja brine da se istovremeno ne odvija više radnji nad bazom podataka.
 */
public class DatabaseOperationThread extends Thread {

    private Runnable operation;

    /**
     * Konstruktor.
     * @param operation radnja
     */
    public DatabaseOperationThread(Runnable operation) {
        this.operation = operation;
    }

    /**
     * Pokretanje niti.
     */
    @Override
    public void run() {
        synchronized (Database.class){
            while (Boolean.TRUE.equals(Database.isActiveConnectionWithDatabase())) {
                try {
                    Database.class.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }

            Database.setActiveConnectionWithDatabase(true);

            try {
                operation.run();
            }finally {
                Database.setActiveConnectionWithDatabase(false);
                Database.class.notifyAll();
            }
        }
    }
}
