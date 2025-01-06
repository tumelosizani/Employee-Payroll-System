package dev.dini.payrollservice.payroll;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payroll")
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-generated ID for each payroll record
    private Integer payrollId;

    @NotNull(message = "Employee ID must not be null.")  // Employee ID from Employee Service
    @Column(nullable = false, name = "employee_id")
    private Integer employeeId;

    @Column(nullable = false)
    @Min(value = 0, message = "Basic salary must be a positive value.")
    private Double basicSalary;

    @Column(nullable = false)
    @Min(value = 0, message = "Bonus must be a positive value.")
    private Double bonus;

    @Column(nullable = false)
    @Min(value = 0, message = "Deductions must be a positive value.")
    private Double deductions;

    @Column(nullable = false)
    private Double netSalary;

    @Column(nullable = false)
    private LocalDate payDate;

    private double grossSalary;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PayrollStatus status;

    // Constructor for setting necessary fields
    public Payroll(Integer employeeId, Double basicSalary, Double bonus, Double deductions, LocalDate payDate, PayrollStatus status) {
        this.employeeId = employeeId;
        this.basicSalary = basicSalary;
        this.bonus = bonus;
        this.deductions = deductions;
        this.payDate = payDate;
        this.netSalary = calculateNetSalary();
        this.status = status;
    }

    // Method to calculate net salary
    public Double calculateNetSalary() {
        return basicSalary + bonus - deductions;
    }

    // Auditing fields (optional)
    @Column(updatable = false)
    private LocalDate createdAt = LocalDate.now();

    private LocalDate updatedAt;

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDate.now();
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public void setNetPay(Double netSalary) {
        this.netSalary = netSalary;
    }
}
