package dev.dini.Employee_Payroll_System.payroll;


import dev.dini.Employee_Payroll_System.employees.Employee;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PayrollDetails {

    private Integer payrollId;
    private Employee employee;
    private Double basicSalary;
    private Double bonus;
    private Double deductions;
    private Double netSalary;
    private LocalDate payDate;

    // Constructor to initialize PayrollDetails
    public PayrollDetails(Integer payrollId, Employee employee, Double basicSalary, Double bonus, Double deductions, LocalDate payDate) {
        this.payrollId = payrollId;
        this.employee = employee;
        this.basicSalary = basicSalary;
        this.bonus = bonus;
        this.deductions = deductions;
        this.payDate = payDate;
        this.netSalary = calculateNetSalary();
    }

    public PayrollDetails(Payroll payroll) {
        this.payrollId = payroll.getPayrollId();
        this.employee = payroll.getEmployee();
        this.basicSalary = payroll.getBasicSalary();
        this.bonus = payroll.getBonus();
        this.deductions = payroll.getDeductions();
        this.payDate = payroll.getPayDate();
        this.netSalary = calculateNetSalary();
    }

    // Method to calculate net salary
    private Double calculateNetSalary() {
        return basicSalary + bonus - deductions;
    }

    // Getters and setters
    public Integer getPayrollId() {
        return payrollId;
    }

    public void setPayrollId(Integer payrollId) {
        this.payrollId = payrollId;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Double getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(Double basicSalary) {
        this.basicSalary = basicSalary;
    }

    public Double getBonus() {
        return bonus;
    }

    public void setBonus(Double bonus) {
        this.bonus = bonus;
    }

    public Double getDeductions() {
        return deductions;
    }

    public void setDeductions(Double deductions) {
        this.deductions = deductions;
    }

    public Double getNetSalary() {
        return netSalary;
    }

    public void setNetSalary(Double netSalary) {
        this.netSalary = netSalary;
    }

    public LocalDate getPayDate() {
        return payDate;
    }

    public void setPayDate(LocalDate payDate) {
        this.payDate = payDate;
    }

    // Optionally, override toString(), equals(), and hashCode() methods
    @Override
    public String toString() {
        return "PayrollDetails{" +
                "payrollId=" + payrollId +
                ", employee=" + employee +
                ", basicSalary=" + basicSalary +
                ", bonus=" + bonus +
                ", deductions=" + deductions +
                ", netSalary=" + netSalary +
                ", payDate=" + payDate +
                '}';
    }

    public Payroll getPayroll() {
        Payroll payroll = new Payroll(
                employee,
                basicSalary,
                bonus,
                deductions,
                payDate,
                PayrollStatus.PENDING // Adjust as needed
        );
        return payroll;
    }

}