package dev.dini.payrollservice.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {
    private Integer employeeId;
    private String firstName;
    private String lastName;
    private String department;
    private String position;
}
