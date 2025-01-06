package dev.dini.employee.management.service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Data
public class EmployeeRequestDTO {

    @NotBlank(message = "First name must not be blank")
    @NotNull
    @Size(min = 2, max = 50)
    private String firstName;

    @NotBlank(message = "Last name must not be blank")
    @NotNull
    @Size(min = 2, max = 50)
    private String lastName;

    @Email(message = "Email should be valid")
    private String email;

    private String phone;

    @NotNull(message = "Position must not be blank")
    @NotBlank
    private String position;

    private String address;

    private LocalDate dateOfBirth;
    private LocalDate dateOfEmployment;
}

