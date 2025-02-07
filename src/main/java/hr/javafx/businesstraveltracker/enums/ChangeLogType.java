package hr.javafx.businesstraveltracker.enums;

/**
 * Predstavlja tip zabilješke promjene.
 */
public enum ChangeLogType {
    NEW("New"),
    MODIFICATION("Modification"),
    DELETE("Delete");

    private final String name;

    ChangeLogType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
