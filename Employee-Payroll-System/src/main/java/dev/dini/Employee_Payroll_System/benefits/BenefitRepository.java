package dev.dini.Employee_Payroll_System.benefits;


import dev.dini.Employee_Payroll_System.employees.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BenefitRepository extends JpaRepository<Benefit, Integer> {
    List<Benefit> findByEmployee(Employee employee);
    List<Benefit> findByEnrollmentDateBetween(LocalDate startDate, LocalDate endDate);
    List<Benefit> findByEmployeeEmployeeIdAndEnrollmentDateBetween(Integer employeeId, LocalDate startDate, LocalDate endDate);
    List<Benefit> findByEmployeeEmployeeId(Integer employeeId);
}
