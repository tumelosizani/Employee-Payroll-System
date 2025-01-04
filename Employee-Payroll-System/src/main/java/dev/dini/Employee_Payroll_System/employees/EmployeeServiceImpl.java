package dev.dini.Employee_Payroll_System.employees;



import dev.dini.Employee_Payroll_System.audit.AuditLog;
import dev.dini.Employee_Payroll_System.audit.AuditLogRepository;
import dev.dini.Employee_Payroll_System.benefits.Benefit;
import dev.dini.Employee_Payroll_System.benefits.BenefitEnrollment;
import dev.dini.Employee_Payroll_System.benefits.BenefitEnrollmentRepository;
import dev.dini.Employee_Payroll_System.benefits.BenefitRepository;
import dev.dini.Employee_Payroll_System.exception.EmployeeNotFoundException;
import dev.dini.Employee_Payroll_System.exception.InsufficientLeaveBalanceException;
import dev.dini.Employee_Payroll_System.leave.LeaveRequest;
import dev.dini.Employee_Payroll_System.leave.LeaveRequestRepository;
import dev.dini.Employee_Payroll_System.leave.LeaveStatus;
import dev.dini.Employee_Payroll_System.payroll.*;
import dev.dini.Employee_Payroll_System.performance.PerformanceReview;
import dev.dini.Employee_Payroll_System.performance.PerformanceReviewRepository;
import dev.dini.Employee_Payroll_System.reports.PayrollReport;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Validated
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PayrollRepository payrollRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final PerformanceReviewRepository performanceReviewRepository;
    private final BenefitEnrollmentRepository benefitEnrollmentRepository;
    private final BenefitRepository benefitRepository;
    private final AuditLogRepository auditLogRepository;
    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    private final PayrollService payrollService;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, PayrollRepository payrollRepository, LeaveRequestRepository leaveRequestRepository, PerformanceReviewRepository performanceReviewRepository, BenefitEnrollmentRepository benefitEnrollmentRepository, BenefitRepository benefitRepository, AuditLogRepository auditLogRepository, PayrollService payrollService) {
        this.employeeRepository = employeeRepository;
        this.payrollRepository = payrollRepository;
        this.leaveRequestRepository = leaveRequestRepository;
        this.performanceReviewRepository = performanceReviewRepository;
        this.benefitEnrollmentRepository = benefitEnrollmentRepository;
        this.benefitRepository = benefitRepository;
        this.auditLogRepository = auditLogRepository;
        this.payrollService = payrollService;
    }

    @Override
    public List<Employee> getAllEmployees() {
        logger.info("Fetching all employees");
        return employeeRepository.findAll();
    }

    @Override
    public Employee getEmployeeById(Integer employeeId) {
        logger.info("Fetching employee with id: {}", employeeId);
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> {logger.error("Employee not found with id: {}", employeeId);
                    return new EmployeeNotFoundException("Employee not found with id: " + employeeId);
                });
    }

    @Override
    public Employee createEmployee(@Valid Employee employee) {
        logger.info("Creating employee with details: {}", employee);
        validateEmployee(employee);
        return employeeRepository.save(employee);
    }

    @Transactional
    @Override
    public Employee updateEmployee(Integer employeeId, @Valid Employee employee) {
        logger.info("Updating employee with id: {}", employeeId);
        if (!employeeRepository.existsById(employeeId)) {
            throw new EmployeeNotFoundException("Employee not found with id: " + employeeId);
        }
        employee.setEmployeeId(employeeId);
        return employeeRepository.save(employee);
    }

    @Override
    public boolean deleteEmployee(Integer employeeId) {
        logger.info("Deleting employee with id: {}", employeeId);
        if (!employeeRepository.existsById(employeeId)) {
            throw new EmployeeNotFoundException("Employee record not found for ID " + employeeId);
        }
        employeeRepository.deleteById(employeeId);
        return true;
    }

    @Override
    public PayrollDetails generatePayroll(Integer employeeId, LocalDate startDate, LocalDate endDate) {
        // Fetch the employee from the repository, throwing exception if not found
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + employeeId));

        // Ensure the start date is before the end date
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        // Calculate payroll based on employee's details and the provided date range
        Payroll payroll = payrollService.calculatePayroll(employee, startDate, endDate);

        // Return the generated payroll details
        return new PayrollDetails(payroll);
    }


    @Override
    public SalaryDetails getSalaryDetails(Integer employeeId) {
        Payroll payroll = payrollRepository.findByEmployeeEmployeeId(employeeId);
        return new SalaryDetails(payroll.getBasicSalary(),payroll.getBonus(),payroll.getDeductions());
    }

    @Override
    public boolean processPayroll(Integer employeeId, LocalDate startDate, LocalDate endDate) {
        PayrollDetails payrollDetails = generatePayroll(employeeId, startDate, endDate);
        payrollRepository.save(payrollDetails.getPayroll()); // Assuming PayrollDetails holds the Payroll entity
        return true;
    }

    @Transactional
    @Override
    public boolean processPayrollWithTransaction(Integer employeeId, LocalDate startDate, LocalDate endDate) {
        try {
            return processPayroll(employeeId, startDate, endDate);  // Call the processPayroll method
        } catch (EmployeeNotFoundException e) {
            logger.error("Employee not found with id: {}", employeeId, e);
            return false;
        } catch (InsufficientLeaveBalanceException exception) {
            logger.error("Insufficient leave balance for employee with id: {}", employeeId, exception);
            return false;
        } catch (Exception exception) {
            logger.error("An error occurred while processing payroll for employee with id: {}", employeeId, exception);
            return false;
        }
    }


    public LeaveRequest applyForLeave(Integer employeeId, LeaveRequest leaveRequest) {
        // Fetch the employee
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with ID: " + employeeId));

        // Calculate the number of leave days
        Integer leaveDays = calculateLeaveDays(leaveRequest.getStartDate(), leaveRequest.getEndDate());

        // Check if the employee has sufficient leave balance before applying
        if (employee.getLeaveBalance() < leaveDays) {
            throw new InsufficientLeaveBalanceException("Employee does not have sufficient leave balance.");
        }

        // Process the leave request
        leaveRequest.setEmployee(employee);
        leaveRequest.setDaysTaken(leaveDays);
        leaveRequest.setStatus(LeaveStatus.PENDING); // Assuming you start with a 'PENDING' status
        leaveRequestRepository.save(leaveRequest);

        // Deduct the leave balance after applying the leave
        employee.setLeaveBalance(employee.getLeaveBalance() - leaveDays);
        employeeRepository.save(employee); // Ensure the leave balance is updated in the database

        return leaveRequest;
    }

    private Integer calculateLeaveDays(LocalDate startDate, LocalDate endDate) {
        // Example calculation for leave days based on start and end date
        return (int) java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1; // Adding 1 for inclusive days
    }





    @Override
    public boolean approveLeave(Integer leaveRequestId) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveRequestId).orElseThrow(() -> new IllegalArgumentException("Leave Request not found"));
        leaveRequest.setStatus(LeaveStatus.APPROVED);
        leaveRequestRepository.save(leaveRequest);
        return true;
    }


    @Override
    public void rejectLeave(Integer leaveRequestId) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveRequestId).orElseThrow(() -> new IllegalArgumentException("Leave Request not found"));
        leaveRequest.setStatus(LeaveStatus.REJECTED);
        leaveRequestRepository.save(leaveRequest);
    }


    @Override
    public List<LeaveRequest> getEmployeeLeaveRequests(Integer employeeId) {
        return leaveRequestRepository.findByEmployeeEmployeeId(employeeId);
    }


    @Override
    public PerformanceReview createPerformanceReview(Integer employeeId, PerformanceReview review) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        review.setEmployee(employee);
        performanceReviewRepository.save(review);
        return review;
    }


    @Override
    public PerformanceReview updatePerformanceReview(Integer reviewId, PerformanceReview review) {
        // Retrieve the existing review from the database
        PerformanceReview existingReview = performanceReviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

        // Update the existing review's fields with the new data
        existingReview.setReviewDate(review.getReviewDate());
        existingReview.setPerformanceRating(review.getPerformanceRating());
        existingReview.setReviewerComments(review.getReviewerComments());
        existingReview.setEmployeeComments(review.getEmployeeComments());
        existingReview.setDevelopmentGoals(review.getDevelopmentGoals());

        // Save the updated review back to the repository
        performanceReviewRepository.save(existingReview);

        // Return the updated review
        return existingReview;
    }



    @Override
    public List<PerformanceReview> getPerformanceReviews(Integer employeeId) {
        return performanceReviewRepository.findByEmployeeEmployeeId(employeeId);
    }


    @Override
    public BenefitEnrollment enrollInBenefit(Integer employeeId, Benefit benefit) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        BenefitEnrollment enrollment = new BenefitEnrollment(employee, benefit, LocalDate.now(), "Active", LocalDate.now(), LocalDate.now().plusYears(1));
        benefitEnrollmentRepository.save(enrollment);
        return enrollment;
    }


    @Override
    public boolean updateBenefitEnrollment(Integer employeeId, Benefit benefit) {
        BenefitEnrollment enrollment = benefitEnrollmentRepository.findByEmployeeEmployeeIdAndBenefitBenefitId(employeeId, benefit.getBenefitId());
        enrollment.setBenefit(benefit);
        benefitEnrollmentRepository.save(enrollment);
        return true;
    }


    @Override
    public List<Benefit> getEmployeeBenefits(Integer employeeId) {
        return benefitRepository.findByEmployeeEmployeeId(employeeId);
    }


    @Override
    public PayrollReport generatePayrollReport(LocalDate startDate, LocalDate endDate) {
        List<Payroll> payrolls = payrollRepository.findByPayDateBetween(startDate, endDate);
        return new PayrollReport(payrolls);
    }


    @Override
    public void logAudit(String actionType, Integer employeeId, String details, Long userId, LocalDate timestamp) {
        // Example of how to fill in the missing fields for the AuditLog
        String tableName = "employees"; // Specify the table name, here we're assuming it's the "employees" table
        // Assuming employeeId is the record you're auditing
        String oldValue = ""; // You can pass the old value based on your logic
        // In this case, passing the details as new value

        // Converting LocalDate to LocalDateTime for action timestamp
        LocalDateTime actionTimestamp = timestamp.atStartOfDay();

        // Retrieve the employee object by employeeId
        Employee employee = employeeRepository.findById(employeeId).orElse(null);

        // Create the audit log record
        AuditLog log;
        if (employee != null) {
            log = new AuditLog(actionType, tableName, employeeId, oldValue, details, actionTimestamp, userId.intValue(), employee);
        } else {
            log = new AuditLog(actionType, tableName, employeeId, oldValue, details, actionTimestamp, userId.intValue());
        }

        // Save the log record in the repository
        auditLogRepository.save(log);
    }

    @Override
    public boolean verifyEmployeeSalary(Integer employeeId, BigDecimal requestedSalary) {
        Payroll payroll = payrollRepository.findByEmployeeEmployeeId(employeeId);

        // Check if payroll is not null and requested salary is not null
        if (payroll == null) {
            throw new IllegalArgumentException("Payroll information not found for employee with ID " + employeeId);
        }

        if (requestedSalary == null) {
            throw new IllegalArgumentException("Requested salary cannot be null");
        }

        // Use compareTo for better precision handling with BigDecimal
        return payroll.getBasicSalary().equals(requestedSalary);
    }



    private void validateEmployee(Employee employee) {
        if (employee.getSalary() == null || employee.getSalary() <= 0) {
            throw new IllegalArgumentException("Salary must be greater than zero");
        }
        if (employee.getFirstName() == null || employee.getFirstName().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (employee.getLastName() == null || employee.getLastName().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (employee.getPosition() == null || employee.getPosition().isEmpty()) {
            throw new IllegalArgumentException("Position is required");
        }
        if (employee.getEmail() == null || !employee.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email address");
        }

        // Validate dateOfBirth
        if (employee.getDateOfBirth() != null && employee.getDateOfBirth().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date of birth cannot be in the future");
        }

        // Validate dateOfEmployment
        if (employee.getDateOfEmployment() != null && employee.getDateOfEmployment().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date of employment cannot be in the future");
        }
    }


    public List<Employee> getEmployeesByPosition(String position) {
        logger.info("Fetching employees with position: {}", position);
        return employeeRepository.findByPosition(position);
    }

    public List<Employee> getEmployeesByFirstName(String firstName) {
        logger.info("Fetching employees with first name: {}", firstName);
        return employeeRepository.findByFirstName(firstName);
    }

    public List<Employee> getEmployeesByLastName(String lastName) {
        logger.info("Fetching employees with last name: {}", lastName);
        return employeeRepository.findByLastName(lastName);
    }

    public Optional<Employee> getEmployeeByEmail(String email) {
        logger.info("Fetching employee with email: {}", email);
        return employeeRepository.findByEmail(email);
    }

    public List<Employee> getEmployeesByFullName(String firstName, String lastName) {
        logger.info("Fetching employees with full name: {} {}", firstName, lastName);
        return employeeRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    public List<Employee> getEmployeesByPositionAndEmail(String position, String emailDomain) {
        logger.info("Fetching employees with position: {} and email domain: {}", position, emailDomain);
        return employeeRepository.findByPositionAndEmailLike(position, "%" + emailDomain);
    }


}
