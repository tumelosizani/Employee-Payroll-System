package dev.dini.employee.management.service.employee;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer employeeId;

    @NotBlank(message = "First name must not be blank")
    @NotNull
    @Size(min = 2, max = 50)
    private String firstName;

    @NotBlank(message = "Last name must not be blank")
    @NotNull
    @Size(min = 2, max = 50)
    private String lastName;

    @Email
    private String email;

    private String phone;

    @NotNull(message = "Salary must not be null")
    @Positive(message = "Salary must be positive")
    private Double salary;

    private String basicSalary;

    private String salaryType;

    private String department;

    @NotNull
    @NotBlank(message = "Position must not be blank")
    private String position;

    private String address;

    private LocalDate dateOfBirth;
    private LocalDate dateOfEmployment;

    private String bankAccount;

    private Integer leaveBalance = 0; // Default value for leaveBalance


    // Default constructor (required by JPA)
    public Employee() {}

    // Constructor with all fields
    public Employee(Integer employeeId, String firstName, String lastName, String email, String position) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.position = position;
    }



    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Double getSalary() {
        return salary;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public LocalDate getDateOfEmployment() {
        return dateOfEmployment;
    }

    public void setLeaveBalance(Integer leaveBalance) {
        this.leaveBalance = leaveBalance;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }



    public int getLeaveBalance() {
        return leaveBalance;  // Return the actual leaveBalance value
    }

    // Optionally, override toString(), equals(), and hashCode() methods if needed
    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", position='" + position + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Employee employee = (Employee) obj;
        return employeeId.equals(employee.employeeId);
    }

    @Override
    public int hashCode() {
        return employeeId != null ? employeeId.hashCode() : 0;
    }

    public void setBasicSalary(double basicSalary) {

    }
}

