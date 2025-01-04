package dev.dini.employee.payroll.system.deductions;

import dev.dini.employee.payroll.system.employees.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DeductionRepository extends JpaRepository<Deduction, Integer> {
    List<Deduction> findByPayrollEmployeeAndStartDateBetween(Employee employee, LocalDate startDate, LocalDate endDate);

}
