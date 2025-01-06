package dev.dini.payrollservice.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PayrollRequestDTO {

    private Integer employeeId;
    private Double basicSalary;
    private Double bonus;
    private Double deductions;
    private LocalDate payDate;
}

/// For Sending Data from Client to Server
