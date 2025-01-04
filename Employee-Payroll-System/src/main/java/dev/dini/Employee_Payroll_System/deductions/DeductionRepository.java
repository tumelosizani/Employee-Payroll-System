package dev.dini.Employee_Payroll_System.deductions;

import dev.dini.Employee_Payroll_System.employees.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DeductionRepository extends JpaRepository<Deduction, Integer> {
    List<Deduction> findByPayrollEmployeeAndStartDateBetween(Employee employee, LocalDate startDate, LocalDate endDate);

}
