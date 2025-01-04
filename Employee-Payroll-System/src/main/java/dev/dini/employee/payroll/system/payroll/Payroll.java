package dev.dini.employee.payroll.system.payroll;

import dev.dini.employee.payroll.system.employees.Employee;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "payroll")
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-generated ID for each payroll record
    private Integer payrollId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "employeeId", nullable = false)
    @NotNull(message = "Employee must be associated with the payroll record.")  // Validation to ensure non-null employee
    private Employee employee;

    @Column(nullable = false)
    @Min(value = 0, message = "Basic salary must be a positive value.")
    private Double basicSalary ;

    @Column(nullable = false)
    @Min(value = 0, message = "Bonus must be a positive value.")
    private Double bonus ;

    @Column(nullable = false)
    @Min(value = 0, message = "Deductions must be a positive value.")
    private Double deductions ;

    @Column(nullable = false)
    private Double netSalary ;

    @Column(nullable = false)
    private LocalDate payDate;

    private double grossSalary;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PayrollStatus status;


    public Payroll(Employee employee, Double basicSalary, Double bonus, Double deductions, LocalDate payDate, PayrollStatus status) {
        this.employee = employee;
        this.basicSalary = basicSalary;
        this.bonus = bonus;
        this.deductions = deductions;
        this.payDate = payDate;
        this.netSalary = calculateNetSalary();
        this.status = status;
    }

    public Payroll(Integer payrollId) {
        this.payrollId = payrollId;
    }


    // Method to calculate net salary
    public Double calculateNetSalary() {
        return basicSalary +bonus - deductions;
    }


    // Update bonus and recalculate net salary
    public void updateBonus(Double bonus) {
        this.bonus = bonus;
        this.netSalary = calculateNetSalary();
    }

    // Update deductions and recalculate net salary
    public void updateDeductions(Double deductions) {
        this.deductions = deductions;
        this.netSalary = calculateNetSalary();
    }

    // Update basic salary and recalculate net salary
    public void updateBasicSalary(Double basicSalary) {
        this.basicSalary = basicSalary;
        this.netSalary = calculateNetSalary();
    }

    // Auditing fields (optional)
    @Column(updatable = false)
    private LocalDate createdAt = LocalDate.now();

    private LocalDate updatedAt;

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDate.now();
    }

    public void setNetPay(Double netPay) {
        this.netSalary = netPay;
    }

    public void setGrossPay(double grossPay) {
        this.grossSalary = grossPay;
    }

    public Employee getEmployee() {
        return employee;
    }

    public Double getBasicSalary() {
        return basicSalary;
    }

    public Double getBonus() {
        return bonus;
    }

    public Double getDeductions() {
        return deductions;
    }

    public Double getNetSalary() {
        return netSalary;
    }

    public LocalDate getPayDate() {
        return payDate;
    }

    public void setPayrollId(Integer payrollId) {
        this.payrollId = payrollId;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Integer getPayrollId() {
        return payrollId;
    }

    public void setBasicSalary(Double basicSalary) {
        this.basicSalary = basicSalary;
    }

    public void setBonus(Double bonus) {
        this.bonus = bonus;
    }

    public void setStatus(PayrollStatus status) {
        this.status = status;
    }
}
