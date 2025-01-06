package dev.dini.payrollservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PayrollResponseDTO {
    private Integer payrollId;        // ID of the payroll
    private Integer employeeId;       // ID of the associated employee
    private Double basicSalary;       // Basic salary
    private Double bonus;             // Bonus amount
    private Double deductions;        // Deductions
    private Double netSalary;         // Net salary (after calculations)
    private LocalDate payDate;        // Date of payment
    private String status;            // Payroll status (e.g., PENDING, COMPLETED)
}

/// This class For Returning Processed Data from Server to Client