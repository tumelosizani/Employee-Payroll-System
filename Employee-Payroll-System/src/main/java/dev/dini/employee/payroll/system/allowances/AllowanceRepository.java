package dev.dini.employee.payroll.system.allowances;


import dev.dini.employee.payroll.system.employees.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AllowanceRepository extends JpaRepository<Allowance, Integer> {

    // Find allowances for an employee between the given start and end dates
    List<Allowance> findByEmployeeAndStartDateBetween(Employee employee, LocalDate startDate, LocalDate endDate);
}
