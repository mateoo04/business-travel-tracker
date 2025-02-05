package hr.javafx.businesstraveltracker.enums;

public enum StatsChartType {

    SPENDING_BY_EMPLOYEES("Spending by employees"),
    SPENDING_BY_TRAVEL_LOGS("Spending by travel logs"),
    SPENDING_BY_MONTH("Spending by month of the year");

    private String name;

    StatsChartType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
