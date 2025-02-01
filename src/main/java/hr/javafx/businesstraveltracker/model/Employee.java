package hr.javafx.businesstraveltracker.model;

import hr.javafx.businesstraveltracker.enums.Department;

public class Employee extends Entity {

    private String firstName;
    private String lastName;
    private String role;
    private Department department;
    private String email;

    public static class Builder{
        private Long id;
        private String firstName;
        private String lastName;
        private String role;
        private Department department;
        private String email;

        public Builder(Long id,String firstName, String lastName, String role, Department department){
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.role = role;
            this.department = department;
        }

        public Builder withEmail(String email){
            this.email = email;
            return this;
        }

        public Employee build(){
            return new Employee(this.id, this.firstName, this.lastName, this.role, this.department, this.email);
        }
    }

    private Employee(Long id, String firstName, String lastName, String role, Department department, String email) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.department = department;
        this.email = email;
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
}
