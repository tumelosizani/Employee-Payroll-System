package dev.dini.employee.payroll.system.payroll;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalaryDetails {

    private Integer employeeId;
    private String employeeName;
    private Double basicSalary;
    private Double bonus;
    private Double deductions;
    private Double netSalary;

    // Constructor to initialize SalaryDetails
    public SalaryDetails(Double basicSalary, Double bonus, Double deductions) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.basicSalary = basicSalary;
        this.bonus = bonus;
        this.deductions = deductions;
        this.netSalary = calculateNetSalary();
    }

    // Method to calculate net salary
    private Double calculateNetSalary() {
        return basicSalary + bonus - deductions;
    }

    // Getters and setters
    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
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

    // Optionally, override toString(), equals(), and hashCode() methods
    @Override
    public String toString() {
        return "SalaryDetails{" +
                "employeeId=" + employeeId +
                ", employeeName='" + employeeName + '\'' +
                ", basicSalary=" + basicSalary +
                ", bonus=" + bonus +
                ", deductions=" + deductions +
                ", netSalary=" + netSalary +
                '}';
    }
}
