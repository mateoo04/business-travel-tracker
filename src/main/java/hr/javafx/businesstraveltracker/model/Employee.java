package hr.javafx.businesstraveltracker.model;

import hr.javafx.businesstraveltracker.enums.Department;

/**
 * Predstavlja zaposlenika
 */
public class Employee extends Entity {

    private String firstName;
    private String lastName;
    private String role;
    private Department department;
    private String email;

    /**
     * Builder klasa koje omogućava lakše stvaranje objekara klase Employee.
     */
    public static class Builder{
        private Long id;
        private String firstName;
        private String lastName;
        private String role;
        private Department department;
        private String email;

        public Builder(String firstName, String lastName, String role, Department department){
            this.firstName = firstName;
            this.lastName = lastName;
            this.role = role;
            this.department = department;
        }

        public Builder withId(Long id){
            this.id = id;
            return this;
        }

        public Builder withEmail(String email){
            this.email = email;
            return this;
        }

        public Employee build(){
            return new Employee(this.id, this.firstName, this.lastName, this.role, this.department, this.email);
        }
    }

    /**
     * Privatni konstruktor
     * @param id ID
     * @param firstName ime
     * @param lastName prezime
     * @param role pozicija unutar tvrtke
     * @param department odjel
     * @param email email
     */
    private Employee(Long id, String firstName, String lastName, String role, Department department, String email) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.department = department;
        this.email = email;
    }

    /**
     * Kopirni konstruktor
     * @param other objekt koji će biti kopiran
     */
    public Employee(Employee other) {
        super(other.getId());
        this.firstName = other.getFirstName();
        this.lastName = other.getLastName();
        this.role = other.getRole();
        this.department = other.getDepartment();
        this.email = other.getEmail();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Name: " + this.firstName + " " + this.lastName
                + "\nRole: " + this.role + "\nDepartment: " + this.department.getName()
                + (email.isEmpty() ? " " : "\nEmail: " + this.email);
    }
}
