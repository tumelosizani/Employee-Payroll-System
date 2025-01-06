package dev.dini.employee.management.service.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Data
public class EmployeeResponseDTO {

    private Integer employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String position;
    private String address;
    private LocalDate dateOfBirth;
    private LocalDate dateOfEmployment;
}
