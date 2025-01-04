package dev.dini.employee.payroll.system.employees;


import dev.dini.employee.payroll.system.benefits.Benefit;
import dev.dini.employee.payroll.system.benefits.BenefitEnrollment;
import dev.dini.employee.payroll.system.exception.EmployeeNotFoundException;
import dev.dini.employee.payroll.system.leave.LeaveRequest;
import dev.dini.employee.payroll.system.payroll.PayrollDetails;
import dev.dini.employee.payroll.system.payroll.SalaryDetails;
import dev.dini.employee.payroll.system.performance.PerformanceReview;
import dev.dini.employee.payroll.system.reports.PayrollReport;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Get all employees
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        if (employees.isEmpty()) {
            return ResponseEntity.noContent().build();  // Returns 204 if no employees are found
        }
        return ResponseEntity.ok(employees);  // Returns 200 with the list of employees
    }

    // Get employee by ID
    @GetMapping("/{employeeId}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Integer employeeId) {
        try {
            Employee employee = employeeService.getEmployeeById(employeeId);
            return ResponseEntity.ok(employee);  // Returns 200 with the employee object
        } catch (EmployeeNotFoundException e) {
            return ResponseEntity.notFound().build();  // Returns 404 if employee is not found
        }
    }

    // Create new employee
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody @Valid Employee employee) {
        try {
            Employee createdEmployee = employeeService.createEmployee(employee);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);  // Returns 201 Created
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();  // Returns 400 Bad Request if validation fails
        }
    }

    // Update employee
    @PutMapping("/{employeeId}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Integer employeeId, @RequestBody @Valid Employee employee) {
        try {
            Employee updatedEmployee = employeeService.updateEmployee(employeeId, employee);
            return ResponseEntity.ok(updatedEmployee);  // Returns 200 with the updated employee
        } catch (EmployeeNotFoundException exception) {
            return ResponseEntity.notFound().build();  // Returns 404 if employee is not found
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();  // Returns 400 if validation fails
        }
    }

    // Delete employee
    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Integer employeeId) {
        try {
            boolean isDeleted = employeeService.deleteEmployee(employeeId);
            if (!isDeleted) {
                return ResponseEntity.notFound().build();  // Returns 404 if employee is not found
            }
            return ResponseEntity.noContent().build();  // Returns 204 No Content for successful deletion
        } catch (EmployeeNotFoundException exception) {
            return ResponseEntity.notFound().build();  // Returns 404 if employee is not found
        }
    }

    // Generate payroll for an employee
    @PostMapping("/{employeeId}/payroll")
    public ResponseEntity<PayrollDetails> generatePayroll(
            @PathVariable Integer employeeId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        PayrollDetails payrollDetails = employeeService.generatePayroll(employeeId, startDate, endDate);
        return ResponseEntity.ok(payrollDetails);
    }

    // Get salary details of an employee
    @GetMapping("/{employeeId}/salary")
    public ResponseEntity<SalaryDetails> getSalaryDetails(@PathVariable Integer employeeId) {
        SalaryDetails salaryDetails = employeeService.getSalaryDetails(employeeId);
        return ResponseEntity.ok(salaryDetails);
    }

    // Apply for leave
    @PostMapping("/{employeeId}/leave")
    public ResponseEntity<LeaveRequest> applyForLeave(
            @PathVariable Integer employeeId,
            @RequestBody LeaveRequest leaveRequest) {
        LeaveRequest createdLeaveRequest = employeeService.applyForLeave(employeeId, leaveRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLeaveRequest);
    }

    // Reject leave
    @PatchMapping("/{employeeId}/leave/{leaveRequestId}/reject")
    public ResponseEntity<Void> rejectLeave(
            @PathVariable Integer employeeId,
            @PathVariable Integer leaveRequestId) {
        employeeService.rejectLeave(leaveRequestId);
        return ResponseEntity.ok().build();
    }

    // Get all leave requests for an employee
    @GetMapping("/{employeeId}/leaves")
    public ResponseEntity<List<LeaveRequest>> getEmployeeLeaveRequests(@PathVariable Integer employeeId) {
        List<LeaveRequest> leaveRequests = employeeService.getEmployeeLeaveRequests(employeeId);
        return ResponseEntity.ok(leaveRequests);
    }

    // Create a performance review
    @PostMapping("/{employeeId}/performance")
    public ResponseEntity<PerformanceReview> createPerformanceReview(
            @PathVariable Integer employeeId,
            @RequestBody PerformanceReview performanceReview) {
        PerformanceReview createdReview = employeeService.createPerformanceReview(employeeId, performanceReview);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
    }

    // Update performance review
    @PutMapping("/performance/{reviewId}")
    public ResponseEntity<PerformanceReview> updatePerformanceReview(
            @PathVariable Integer reviewId,
            @RequestBody PerformanceReview performanceReview) {
        PerformanceReview updatedReview = employeeService.updatePerformanceReview(reviewId, performanceReview);
        return ResponseEntity.ok(updatedReview);
    }

    // Get all performance reviews for an employee
    @GetMapping("/{employeeId}/performance")
    public ResponseEntity<List<PerformanceReview>> getPerformanceReviews(@PathVariable Integer employeeId) {
        List<PerformanceReview> reviews = employeeService.getPerformanceReviews(employeeId);
        return ResponseEntity.ok(reviews);
    }

    // Enroll employee in a benefit
    @PostMapping("/{employeeId}/benefits")
    public ResponseEntity<BenefitEnrollment> enrollInBenefit(
            @PathVariable Integer employeeId,
            @RequestBody Benefit benefit) {
        BenefitEnrollment benefitEnrollment = employeeService.enrollInBenefit(employeeId, benefit);
        return ResponseEntity.status(HttpStatus.CREATED).body(benefitEnrollment);
    }

    // Update benefit enrollment for an employee
    @PutMapping("/{employeeId}/benefits")
    public ResponseEntity<Void> updateBenefitEnrollment(
            @PathVariable Integer employeeId,
            @RequestBody Benefit benefit) {
        boolean updated = employeeService.updateBenefitEnrollment(employeeId, benefit);
        return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    // Get all benefits of an employee
    @GetMapping("/{employeeId}/benefits")
    public ResponseEntity<List<Benefit>> getEmployeeBenefits(@PathVariable Integer employeeId) {
        List<Benefit> benefits = employeeService.getEmployeeBenefits(employeeId);
        return ResponseEntity.ok(benefits);
    }

    // Generate payroll report
    @GetMapping("/payroll-report")
    public ResponseEntity<PayrollReport> generatePayrollReport(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        PayrollReport report = employeeService.generatePayrollReport(startDate, endDate);
        return ResponseEntity.ok(report);
    }

    // Verify employee salary
    @PostMapping("/{employeeId}/verify-salary")
    public ResponseEntity<Boolean> verifySalary(
            @PathVariable Integer employeeId,
            @RequestParam BigDecimal requestedSalary) {
        boolean isSalaryVerified = employeeService.verifyEmployeeSalary(employeeId, requestedSalary);
        return ResponseEntity.ok(isSalaryVerified);
    }
}

    // Additional endpoints for other functionalities (e.g., payroll, leave, benefits) can be added similarly

