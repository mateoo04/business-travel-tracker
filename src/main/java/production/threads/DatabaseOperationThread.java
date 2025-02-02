package production.threads;

import hr.javafx.businesstraveltracker.repository.Database;

public class DatabaseOperationThread extends Thread {

    private Runnable operation;

    public DatabaseOperationThread(Runnable operation) {
        this.operation = operation;
    }

    @Override
    public void run() {
        synchronized (Database.class){
            try{
                while(Database.isActiveConnectionWithDatabase()){
                    Database.class.wait();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
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
