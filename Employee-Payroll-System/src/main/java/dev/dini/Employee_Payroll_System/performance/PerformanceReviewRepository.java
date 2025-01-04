package dev.dini.Employee_Payroll_System.performance;

import dev.dini.Employee_Payroll_System.employees.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PerformanceReviewRepository extends JpaRepository<PerformanceReview, Integer> {
    List<PerformanceReview> findByEmployeeEmployeeId(Integer employeeId);
    Optional<PerformanceReview> findByEmployeeAndReviewPeriodStartAndReviewPeriodEnd(
            Employee employee, LocalDate startDate, LocalDate endDate);
}

