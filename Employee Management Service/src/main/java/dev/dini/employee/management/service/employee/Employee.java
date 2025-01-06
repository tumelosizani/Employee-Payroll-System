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

    @NotNull(message = "Position must not be blank")
    @NotBlank
    private String position;

    private String address;

    private LocalDate dateOfBirth;
    private LocalDate dateOfEmployment;

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
}
