package dev.dini.Employee_Payroll_System.payroll;

public class TaxCalculator {

    // Example method to calculate deductions based on the salary
    public static double calculateDeductions(double salary) {
        double taxRate = 0.20;  // Example: 20% tax rate
        double tax = salary * taxRate;
        double otherDeductions = 50;  // Example of other deductions (e.g., insurance)
        return tax + otherDeductions;
    }

    // Example method to calculate net salary after deductions
    public static double calculateNetSalary(double basicSalary, double bonus, double deductions) {
        return (basicSalary + bonus) - deductions;
    }
}
