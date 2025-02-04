package hr.javafx.businesstraveltracker.model;

import hr.javafx.businesstraveltracker.enums.TripStatus;

import java.time.LocalDate;

public class TravelLog extends Entity {
    private Employee employee;
    private String destination;
    private LocalDate startDate;
    private LocalDate endDate;
    private TripStatus status;

    public TravelLog(Long id, Employee employee, String destination, LocalDate startDate, LocalDate endDate, TripStatus status) {
        super(id);
        this.employee = employee;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }
    public TravelLog(Employee employee, String destination, LocalDate startDate, LocalDate endDate, TripStatus status) {
        super();
        this.employee = employee;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }
    public TravelLog(TravelLog other) {
        super(other.getId());
        this.employee = other.getEmployee();
        this.destination = other.getDestination();
        this.startDate = other.getStartDate();
        this.endDate = other.getEndDate();
        this.status = other.getStatus();
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public TripStatus getStatus() {
        return status;
    }

    public void setStatus(TripStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Employee: " + this.employee.getFirstName() + " " + this.employee.getLastName() +
                "\nDestination: " + this.destination + "\nStart Date: " + this.startDate + "\nEnd Date: " +
                this.endDate + "\nStatus: " + this.status.getName();
    }
}
