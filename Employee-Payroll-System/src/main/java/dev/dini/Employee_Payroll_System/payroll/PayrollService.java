package dev.dini.Employee_Payroll_System.payroll;



import dev.dini.Employee_Payroll_System.employees.Employee;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface PayrollService {

    // Basic CRUD Operations
    List<Payroll> getAllPayrolls();
    Payroll getPayrollById(Integer payrollId);
    Payroll createPayroll(Payroll payroll);
    Payroll updatePayroll(Integer payrollId, Payroll payroll);
    void deletePayroll(Integer payrollId);

    // Payroll Retrieval by Criteria
    List<Payroll> getPayrollsByDateRange(LocalDate startDate, LocalDate endDate);
    List<Payroll> getPayrollHistoryForEmployee(Integer employeeId, LocalDate startDate, LocalDate endDate);

    // Payroll Calculation
    Payroll calculatePayroll(Employee employee, LocalDate startDate, LocalDate endDate);

    // Additional Features
    void processMonthlyPayroll(LocalDate payrollDate); // Processes payroll for all employees for a given month.
    Payroll generatePayrollReport(Integer payrollId); // Generates a detailed payroll report.
    Map<Integer, Double> getTotalPaymentsByEmployee(LocalDate startDate, LocalDate endDate); // Retrieves total payments by employee in a date range.
    double calculateTotalPayrollCost(LocalDate startDate, LocalDate endDate); // Calculates the total payroll cost for a date range.
    Payroll previewPayroll(Employee employee, LocalDate startDate, LocalDate endDate); // Previews payroll calculation before saving.

    double calculateBonus(Employee employee, LocalDate startDate, LocalDate endDate);

    // Tax and Benefits
    double calculateTax(Employee employee, LocalDate startDate, LocalDate endDate); // Calculates tax for an employee in a date range.
    Map<String, Double> calculateDeductions(Employee employee, LocalDate startDate, LocalDate endDate); // Calculates all deductions (tax, benefits, etc.) for an employee.
    Map<String, Double> calculateAllowances(Employee employee, LocalDate startDate, LocalDate endDate); // Calculates allowances for an employee.

    // Error Handling and Auditing
    void handlePayrollErrors(Integer payrollId); // Resolves errors for a specific payroll entry.
    List<String> auditPayrollRecords(LocalDate startDate, LocalDate endDate); // Audits payroll records for discrepancies.

    // Employee-Specific Features
    List<Payroll> getPendingPayrollsForEmployee(Integer employeeId); // Retrieves all payrolls that are pending for an employee.
    List<Employee> getEmployeesWithOverduePayrolls(LocalDate payrollDate); // Lists employees whose payroll is overdue.
}
