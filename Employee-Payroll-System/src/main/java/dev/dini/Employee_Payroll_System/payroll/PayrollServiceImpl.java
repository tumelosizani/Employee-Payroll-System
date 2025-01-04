package dev.dini.Employee_Payroll_System.payroll;


import dev.dini.Employee_Payroll_System.allowances.Allowance;
import dev.dini.Employee_Payroll_System.allowances.AllowanceRepository;
import dev.dini.Employee_Payroll_System.deductions.Deduction;
import dev.dini.Employee_Payroll_System.deductions.DeductionRepository;
import dev.dini.Employee_Payroll_System.employees.Employee;
import dev.dini.Employee_Payroll_System.employees.EmployeeRepository;
import dev.dini.Employee_Payroll_System.exception.InvalidPayrollDataException;
import dev.dini.Employee_Payroll_System.exception.PayrollNotFoundException;
import dev.dini.Employee_Payroll_System.exception.PayrollProcessingException;
import dev.dini.Employee_Payroll_System.performance.PerformanceReview;
import dev.dini.Employee_Payroll_System.performance.PerformanceReviewRepository;
import dev.dini.Employee_Payroll_System.taxes.Tax;
import dev.dini.Employee_Payroll_System.taxes.TaxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PayrollServiceImpl implements PayrollService {

    private static final Logger logger = LoggerFactory.getLogger(PayrollServiceImpl.class);

    private final PayrollRepository payrollRepository;
    private final EmployeeRepository employeeRepository;
    private final TaxRepository taxRepository;
    private final DeductionRepository deductionRepository;
    private final AllowanceRepository allowanceRepository;
    private final PerformanceReviewRepository performanceReviewRepository;

    public PayrollServiceImpl(PayrollRepository payrollRepository, EmployeeRepository employeeRepository, TaxRepository taxRepository, DeductionRepository deductionRepository, AllowanceRepository allowanceRepository, PerformanceReviewRepository performanceReviewRepository) {
        this.payrollRepository = payrollRepository;
        this.employeeRepository = employeeRepository;
        this.taxRepository = taxRepository;
        this.deductionRepository = deductionRepository;
        this.allowanceRepository = allowanceRepository;
        this.performanceReviewRepository = performanceReviewRepository;
    }

    @Override
    public List<Payroll> getAllPayrolls() {
        logger.info("Fetching all payroll records.");
        return payrollRepository.findAll();  // Fetch all payroll records
    }

    @Override
    public Payroll getPayrollById(Integer payrollId) {
        logger.info("Fetching payroll record with ID: {}", payrollId);
        Optional<Payroll> payroll = payrollRepository.findById(payrollId);
        if (payroll.isPresent()) {
            return payroll.get();
        } else {
            logger.warn("Payroll with ID {} not found.", payrollId);
            throw new PayrollNotFoundException("Payroll record not found for ID " + payrollId);  // Throw custom exception
        }
    }

    @Override
    public Payroll createPayroll(Payroll payroll) {
        if (payroll.getEmployee() != null) {
            logger.info("Creating payroll for employee: {} {}", payroll.getEmployee().getFirstName(), payroll.getEmployee().getLastName());
            logger.debug("Payroll details: {}", payroll);  // Add more detailed information in debug logs
        } else {
            logger.error("Employee information is missing for payroll creation. Payroll details: {}", payroll);
        }

        return payrollRepository.save(payroll);  // Save the payroll record to the database
    }


    @Override
    public Payroll updatePayroll(Integer payrollId, Payroll payroll) {
        if (payrollRepository.existsById(payrollId)) {
            payroll.setPayrollId(payrollId);  // Set the existing payroll ID
            logger.info("Updating payroll record with ID: {}", payrollId);
            logger.debug("Updated payroll details: {}", payroll);  // Log updated payroll details in debug level
            return payrollRepository.save(payroll);  // Update payroll record
        }
        logger.warn("Payroll with ID {} not found for update.", payrollId);
        throw new PayrollNotFoundException("Payroll record not found for ID " + payrollId);  // Throw custom exception
    }


    @Override
    public void deletePayroll(Integer payrollId) {
        if (payrollRepository.existsById(payrollId)) {
            logger.info("Deleting payroll record with ID: {}", payrollId);
            payrollRepository.deleteById(payrollId);  // Delete the payroll record
        } else {
            logger.warn("Payroll with ID {} not found for deletion.", payrollId);
            throw new PayrollNotFoundException("Payroll record not found for ID " + payrollId);  // Throw custom exception
        }
    }


    @Override
    public List<Payroll> getPayrollsByDateRange(LocalDate startDate, LocalDate endDate) {
        logger.info("Fetching payroll records for date range: {} to {}", startDate, endDate);
        return payrollRepository.findByPayDateBetween(startDate, endDate);
    }

    @Override
    public List<Payroll> getPayrollHistoryForEmployee(Integer employeeId, LocalDate startDate, LocalDate endDate) {
        logger.info("Fetching payroll history for employee with ID: {} between {} and {}", employeeId, startDate, endDate);
        return payrollRepository.findByEmployeeEmployeeIdAndPayDateBetween(employeeId, startDate, endDate);
    }

    @Override
    public Payroll calculatePayroll(Employee employee, LocalDate startDate, LocalDate endDate) {
        logger.info("Calculating payroll for employee: {} {} from {} to {}",
                employee.getFirstName(), employee.getLastName(), startDate, endDate);

        // Retrieve employee's salary and other details
        double salary = employee.getBaseSalary();
        double allowances = calculateAllowances(employee, startDate, endDate).values().stream().mapToDouble(Double::doubleValue).sum();
        double deductions = calculateDeductions(employee, startDate, endDate).values().stream().mapToDouble(Double::doubleValue).sum();
        double tax = calculateTax(employee, startDate, endDate);

        double netPay = salary + allowances - deductions - tax;

        // Create and return the payroll using the constructor with parameters
        Payroll payroll = new Payroll(employee, salary, allowances, deductions + tax, endDate, PayrollStatus.PENDING);
        payroll.setGrossPay(salary + allowances);
        payroll.setNetPay(netPay);

        logger.debug("Calculated payroll details: {}", payroll);
        return payroll;
    }



    @Override
    public void processMonthlyPayroll(LocalDate payrollDate) {
        logger.info("Processing monthly payroll for date: {}", payrollDate);
        List<Employee> employees = employeeRepository.findAll();

        for (Employee employee : employees) {
            try {
                Payroll payroll = calculatePayroll(employee, payrollDate.minusMonths(1).withDayOfMonth(1), payrollDate);
                createPayroll(payroll);
            } catch (Exception exception) {
                logger.error("Failed to process payroll for employee: {}", employee.getEmployeeId(), exception);
            }
        }

        logger.info("Completed processing monthly payroll.");
    }


    @Override
    public Payroll generatePayrollReport(Integer payrollId) {
        logger.info("Generating payroll report for payroll ID: {}", payrollId);

        // Fetch the payroll record
        Optional<Payroll> payrollOptional = payrollRepository.findById(payrollId);
        if (payrollOptional.isEmpty()) {
            logger.warn("Payroll with ID {} not found for report generation.", payrollId);
            throw new PayrollNotFoundException("Payroll record not found for ID " + payrollId);
        }

        Payroll payroll = payrollOptional.get();

        // Log the payroll details for debugging
        logger.debug("Payroll details: {}", payroll);

        // (Optional) Add logic here if the report requires any processing or formatting

        // Return the payroll record directly (or a formatted report object if necessary)
        return payroll;
    }


    @Override
    public Map<Integer, Double> getTotalPaymentsByEmployee(LocalDate startDate, LocalDate endDate) {
        logger.info("Calculating total payments for employees between {} and {}", startDate, endDate);

        // Fetch payroll records within the date range
        List<Payroll> payrolls = payrollRepository.findByPayDateBetween(startDate, endDate);

        // Check if any payrolls exist in the range
        if (payrolls.isEmpty()) {
            logger.warn("No payroll records found between {} and {}", startDate, endDate);
            return Map.of(); // Return an empty map if no records exist
        }

        // Group by employee ID and sum the net salary
        Map<Integer, Double> totalPaymentsByEmployee = payrolls.stream()
                .filter(payroll -> payroll.getEmployee() != null) // Ensure payroll has an associated employee
                .collect(Collectors.groupingBy(
                        payroll -> payroll.getEmployee().getEmployeeId(),
                        Collectors.summingDouble(Payroll::getNetSalary)
                ));

        logger.info("Total payments calculated for {} employees.", totalPaymentsByEmployee.size());
        return totalPaymentsByEmployee;
    }


    @Override
    public double calculateTotalPayrollCost(LocalDate startDate, LocalDate endDate) {
        logger.info("Calculating total payroll cost between {} and {}", startDate, endDate);

        // Fetch payroll records within the date range
        List<Payroll> payrolls = payrollRepository.findByPayDateBetween(startDate, endDate);

        // Check if there are payroll records
        if (payrolls.isEmpty()) {
            logger.warn("No payroll records found between {} and {}", startDate, endDate);
            return 0.0; // Return 0 if no records exist
        }

        // Calculate total payroll cost
        double totalCost = payrolls.stream()
                .mapToDouble(Payroll::getNetSalary) // Extract net salary from each payroll
                .sum();

        logger.info("Total payroll cost calculated: {}", totalCost);
        return totalCost;
    }


    @Override
    public Payroll previewPayroll(Employee employee, LocalDate startDate, LocalDate endDate) {
        logger.info("Previewing payroll for employee: {} {} between {} and {}",
                employee.getFirstName(), employee.getLastName(), startDate, endDate);

        // Example logic to calculate payroll preview (can be customized)
        double basicSalary = employee.getBaseSalary(); // Assuming the employee has a base salary
        double bonus = calculateBonus(employee, startDate, endDate); // Implement logic for bonus calculation

        // Calculate total deductions (sum of all deductions)
        double deductions = calculateDeductions(employee, startDate, endDate).values().stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        // Calculate net salary
        double netSalary = basicSalary + bonus - deductions;

        // Create a preview payroll record for the employee
        Payroll previewPayroll = new Payroll(employee, basicSalary, bonus, deductions, endDate, PayrollStatus.PENDING);
        previewPayroll.setNetPay(netSalary); // Update net salary
        previewPayroll.setGrossPay(basicSalary + bonus); // Optionally, set gross pay

        // Log the preview payroll details
        logger.debug("Preview payroll details: {}", previewPayroll);

        return previewPayroll;
    }

    @Override
    public double calculateBonus(Employee employee, LocalDate startDate, LocalDate endDate) {
        logger.info("Calculating bonus for employee: {} {} between {} and {}",
                employee.getFirstName(), employee.getLastName(), startDate, endDate);

        // Example: Base bonus calculation - assuming bonus is a percentage of the base salary
        double baseSalary = employee.getBaseSalary();  // Employee's base salary
        double performanceBonusPercentage = getPerformanceBonusPercentage(employee, startDate, endDate); // Assuming a method to get the performance-based bonus percentage

        // Calculate bonus based on the performance review or other criteria
        double bonus = baseSalary * performanceBonusPercentage;

        // Log the calculated bonus
        logger.debug("Calculated bonus for employee {}: {}", employee.getEmployeeId(), bonus);

        return bonus;
    }

    private double getPerformanceBonusPercentage(Employee employee, LocalDate startDate, LocalDate endDate) {
        // Fetch the performance review for the employee within the specified period
        Optional<PerformanceReview> performanceReviewOpt = performanceReviewRepository
                .findByEmployeeAndReviewPeriodStartAndReviewPeriodEnd(employee, startDate, endDate);

        if (performanceReviewOpt.isEmpty()) {
            logger.warn("No performance review found for employee {} during the period {} to {}",
                    employee.getEmployeeId(), startDate, endDate);
            return 0.05; // Default bonus percentage if no performance review exists
        }

        PerformanceReview performanceReview = performanceReviewOpt.get();

        // Log the retrieved review details
        logger.debug("Retrieved performance review: {}", performanceReview);

        // Use an Enum for performance ratings to avoid magic strings and improve maintainability
        switch (performanceReview.getPerformanceRating()) {
            case EXCELLENT:
                return 0.15;  // 15% bonus for excellent performance
            case GOOD:
                return 0.10;  // 10% bonus for good performance
            case AVERAGE:
                return 0.05;  // 5% bonus for average performance
            default:
                return 0.03;  // Default bonus for poor performance
        }
    }





    @Override
    public double calculateTax(Employee employee, LocalDate startDate, LocalDate endDate) {
        // Convert LocalDate to Instant for querying with tax records
        Instant startInstant = startDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Instant endInstant = endDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();

        // Fetch tax records for the employee within the date range and tax rate range
        List<Tax> taxes = taxRepository.findByEmployeeAndTaxRateBetweenAndEffectiveDateBetween(
                employee, 0.0, Double.MAX_VALUE, startInstant, endInstant);  // Adjust as needed for your tax rate logic

        if (taxes.isEmpty()) {
            logger.warn("No tax records found for employee ID: {} between {} and {}", employee.getEmployeeId(), startDate, endDate);
            return 0.0;  // If no tax records are found, return 0.0
        }

        // Calculate total tax
        double totalTax = 0.0;
        for (Tax tax : taxes) {
            double taxAmount = calculateTaxAmount(employee, tax);
            totalTax += taxAmount;  // Accumulate total tax over the period
        }

        // Return the calculated total tax
        return totalTax;
    }


    // Helper method to calculate tax for a specific tax record
    private double calculateTaxAmount(Employee employee, Tax tax) {
        // Example tax calculation based on total salary, tax rate, and tax deductions
        double salary = employee.getBaseSalary();  // Assuming you're using the base salary
        double taxRate = tax.getTaxRate();  // Get the applicable tax rate

        // Apply tax deduction (if the deduction should reduce the salary first)
        double taxableSalary = salary - tax.getTaxDeduction();

        // Ensure the taxable salary is not negative
        taxableSalary = Math.max(taxableSalary, 0.0);

        // Calculate tax amount using the tax rate (percentage of the taxable salary)
        double taxAmount = taxableSalary * (taxRate / 100);

        // Round the tax amount to two decimal places for precision
        taxAmount = Math.round(taxAmount * 100.0) / 100.0;

        // Ensure tax amount is not negative (in case deductions are larger than the taxable salary)
        return Math.max(taxAmount, 0.0);
    }


    // Calculate deductions for an employee
    @Override
    public Map<String, Double> calculateDeductions(Employee employee, LocalDate startDate, LocalDate endDate) {
        // Fetch deductions for the employee between the specified dates
        List<Deduction> deductions = deductionRepository.findByPayrollEmployeeAndStartDateBetween(employee, startDate, endDate);

        if (deductions.isEmpty()) {
            logger.warn("No deductions found for employee ID: {} between {} and {}", employee.getEmployeeId(), startDate, endDate);
            return Map.of();  // If no deductions are found, return an empty map
        }

        // Initialize a map to store deduction type and amount
        Map<String, Double> deductionMap = new HashMap<>();

        // Iterate through the deductions and calculate total for each deduction type
        for (Deduction deduction : deductions) {
            double totalDeduction = 0.0;

            // If the deduction is a fixed amount, add it directly
            if (deduction.getPercentage() == null) {
                totalDeduction = deduction.getAmount();
            } else {
                // If the deduction is a percentage of salary, calculate it
                double salary = employee.getBaseSalary();  // Get employee's base salary
                totalDeduction = (salary * deduction.getPercentage()) / 100;
            }

            // Add the deduction to the map with the deduction type as the key
            deductionMap.put(deduction.getDeductionType(), totalDeduction);
        }

        // Return the map with deduction types and their corresponding amounts
        return deductionMap;
    }

    // Calculate allowances for an employee
    @Override
    public Map<String, Double> calculateAllowances(Employee employee, LocalDate startDate, LocalDate endDate) {
        // Fetch allowances for the employee between the specified dates
        List<Allowance> allowances = allowanceRepository.findByEmployeeAndStartDateBetween(employee, startDate, endDate);

        if (allowances.isEmpty()) {
            logger.warn("No allowances found for employee ID: {} between {} and {}", employee.getEmployeeId(), startDate, endDate);
            return Map.of();  // If no allowances are found, return an empty map
        }

        // Initialize a map to store allowance type and amount
        Map<String, Double> allowanceMap = new HashMap<>();

        // Iterate through the allowances and calculate total for each allowance type
        for (Allowance allowance : allowances) {
            double totalAllowance = 0.0;

            // If the allowance is a fixed amount, add it directly
            if (allowance.getPercentage() == null) {
                totalAllowance = allowance.getAmount();
            } else {
                // If the allowance is percentage-based, calculate it based on the employee's salary
                double salary = employee.getBaseSalary();  // Get employee's base salary
                totalAllowance = (salary * allowance.getPercentage()) / 100;
            }

            // Add the allowance to the map with the allowance type as the key
            allowanceMap.put(allowance.getAllowanceType(), totalAllowance);
        }

        // Return the map with allowance types and their corresponding amounts
        return allowanceMap;
    }

    @Override
    public void handlePayrollErrors(Integer payrollId) {
        try {
            // Attempt to find the payroll by ID
            Optional<Payroll> payrollOptional = payrollRepository.findById(payrollId);
            if (payrollOptional.isEmpty()) {
                // Payroll record not found
                logger.error("Payroll with ID {} not found.", payrollId);
                throw new PayrollNotFoundException("Payroll record not found for ID " + payrollId);
            }

            Payroll payroll = payrollOptional.get();

            // Check if employee data is valid
            Employee employee = payroll.getEmployee();
            if (employee == null || employee.getEmployeeId() == null) {
                // Invalid employee data
                logger.error("Invalid employee data for payroll ID {}.", payrollId);
                throw new InvalidPayrollDataException("Employee data is missing or invalid for payroll ID " + payrollId);
            }

            // Check if payroll calculations are valid (you could implement specific checks)
            if (payroll.getBasicSalary() == null || payroll.getBonus() == null || payroll.getDeductions() == null) {
                // Invalid payroll data
                logger.error("Invalid payroll data for payroll ID {}. Missing salary, bonus, or deductions.", payrollId);
                throw new InvalidPayrollDataException("Payroll data is missing essential fields (salary, bonus, deductions) for payroll ID " + payrollId);
            }

            // Optionally, you can check for database integrity errors or other specific error conditions

            logger.info("Payroll with ID {} is valid and ready for processing.", payrollId);

        } catch (PayrollNotFoundException | InvalidPayrollDataException e) {
            // Handle known exceptions
            logger.error("Error processing payroll ID {}: {}", payrollId, e.getMessage());
            // Optionally, notify administrators or take further actions like alerting the system
        } catch (Exception e) {
            // Handle unexpected exceptions
            logger.error("Unexpected error occurred while processing payroll ID {}: {}", payrollId, e.getMessage());
            // Send notifications or alerts if necessary
            throw new PayrollProcessingException("Unexpected error occurred while processing payroll ID " + payrollId, e);
        }
    }

    @Override
    public List<String> auditPayrollRecords(LocalDate startDate, LocalDate endDate) {
        // List to hold audit findings
        List<String> auditReport = new ArrayList<>();

        // Fetch payroll records within the specified date range
        List<Payroll> payrollRecords = payrollRepository.findByPayDateBetween(startDate, endDate);

        // Check if there are no records for the given date range
        if (payrollRecords.isEmpty()) {
            auditReport.add("No payroll records found for the given date range: " + startDate + " to " + endDate);
            return auditReport;  // Early return if no records exist
        }

        // Iterate over the payroll records to check for discrepancies
        for (Payroll payroll : payrollRecords) {
            // Example checks:
            // 1. Check if the employee associated with the payroll exists
            if (payroll.getEmployee() == null) {
                auditReport.add("Payroll record with ID " + payroll.getPayrollId() + " is missing employee data.");
            }

            // 2. Check if the payroll values are valid (e.g., salary should not be negative)
            if (payroll.getBasicSalary() == null || payroll.getBasicSalary() < 0) {
                auditReport.add("Payroll record with ID " + payroll.getPayrollId() + " has an invalid basic salary: " + payroll.getBasicSalary());
            }

            if (payroll.getBonus() == null || payroll.getBonus() < 0) {
                auditReport.add("Payroll record with ID " + payroll.getPayrollId() + " has an invalid bonus: " + payroll.getBonus());
            }

            if (payroll.getDeductions() == null || payroll.getDeductions() < 0) {
                auditReport.add("Payroll record with ID " + payroll.getPayrollId() + " has invalid deductions: " + payroll.getDeductions());
            }

            if (payroll.getNetSalary() == null || payroll.getNetSalary() < 0) {
                auditReport.add("Payroll record with ID " + payroll.getPayrollId() + " has invalid net salary: " + payroll.getNetSalary());
            }

            // 3. Check if any payroll changes occurred (you could use a history table or versioning system if available)
            // Example: If payroll information has been modified, track changes (you might need a specific versioning strategy).
            // In case of discrepancies:
            // Example logic:
            if (payroll.getBasicSalary() != payroll.getNetSalary() + payroll.getDeductions() - payroll.getBonus()) {
                auditReport.add("Payroll record with ID " + payroll.getPayrollId() + " has mismatched salary calculations.");
            }

            // You can also log payroll changes or discrepancies from previous audits (if any history or logs are kept).
        }

        // Return the generated audit report
        return auditReport;
    }

    @Override
    public List<Payroll> getPendingPayrollsForEmployee(Integer employeeId) {
        logger.info("Fetching pending payroll records for employee with ID: {}", employeeId);

        // Fetch all payroll records for the employee that are in the "PENDING" status
        List<Payroll> pendingPayrolls = payrollRepository.findByEmployeeEmployeeIdAndStatus(employeeId, PayrollStatus.PENDING);

        // Log the result of the query for debugging purposes
        logger.debug("Found {} pending payroll records for employee with ID: {}", pendingPayrolls.size(), employeeId);

        // Return the list of pending payroll records
        return pendingPayrolls;
    }

    @Override
    public List<Employee> getEmployeesWithOverduePayrolls(LocalDate payrollDate) {
        logger.info("Fetching employees with overdue payrolls as of {}", payrollDate);

        // Fetch payrolls that are overdue (payDate before payrollDate and status is "PENDING")
        List<Payroll> overduePayrolls = payrollRepository.findByPayDateBeforeAndStatus(payrollDate, PayrollStatus.PENDING);

        // Extract the list of unique employees from the overdue payrolls
        List<Employee> overdueEmployees = overduePayrolls.stream()
                .map(Payroll::getEmployee)  // Extract the Employee from each Payroll
                .distinct()  // Ensure no duplicates
                .collect(Collectors.toList());

        // Log the result of the query for debugging purposes
        logger.debug("Found {} employees with overdue payrolls", overdueEmployees.size());

        // Return the list of employees with overdue payrolls
        return overdueEmployees;
    }
}