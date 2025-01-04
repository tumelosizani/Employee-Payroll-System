package dev.dini.Employee_Payroll_System.employees;

import dev.dini.Employee_Payroll_System.benefits.Benefit;
import dev.dini.Employee_Payroll_System.benefits.BenefitEnrollment;
import dev.dini.Employee_Payroll_System.leave.LeaveRequest;
import dev.dini.Employee_Payroll_System.payroll.PayrollDetails;
import dev.dini.Employee_Payroll_System.payroll.SalaryDetails;
import dev.dini.Employee_Payroll_System.performance.PerformanceReview;
import dev.dini.Employee_Payroll_System.reports.PayrollReport;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface EmployeeService {

    // Basic employee management methods
    List<Employee> getAllEmployees();
    Employee getEmployeeById(Integer id);
    Employee createEmployee(Employee employee);
    Employee updateEmployee(Integer id, Employee employee);
    boolean deleteEmployee(Integer id);

    // Payroll-related features
    PayrollDetails generatePayroll(Integer employeeId, LocalDate startDate, LocalDate endDate);
    SalaryDetails getSalaryDetails(Integer employeeId);
    boolean processPayroll(Integer employeeId, LocalDate startDate, LocalDate endDate);
    boolean processPayrollWithTransaction(Integer employeeId, LocalDate startDate, LocalDate endDate); // Optional: transactional payroll processing

    // Leave management features
    LeaveRequest applyForLeave(Integer employeeId, LeaveRequest leaveRequest);
    boolean approveLeave(Integer leaveRequestId);
    void rejectLeave(Integer leaveRequestId);
    List<LeaveRequest> getEmployeeLeaveRequests(Integer employeeId);

    // Performance review features
    PerformanceReview createPerformanceReview(Integer employeeId, PerformanceReview review);
    PerformanceReview updatePerformanceReview(Integer reviewId, PerformanceReview review);
    List<PerformanceReview> getPerformanceReviews(Integer employeeId);

    // Benefits-related features
    BenefitEnrollment enrollInBenefit(Integer employeeId, Benefit benefit);
    boolean updateBenefitEnrollment(Integer employeeId, Benefit benefit);
    List<Benefit> getEmployeeBenefits(Integer employeeId);

    // Payroll report generation
    PayrollReport generatePayrollReport(LocalDate startDate, LocalDate endDate);

    // Audit logs
    void logAudit(String actionType, Integer employeeId, String details, Long userId, LocalDate timestamp); // Optional: Adding timestamp for audit logs

    // Security & authorization-related
    boolean verifyEmployeeSalary(Integer employeeId, BigDecimal requestedSalary);
}
