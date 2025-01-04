package dev.dini.Employee_Payroll_System.taxes;

import dev.dini.Employee_Payroll_System.employees.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface TaxRepository extends JpaRepository<Tax, Integer> {
    List<Tax> findByEmployeeAndTaxRateBetweenAndEffectiveDateBetween(
            Employee employee, Double minTaxRate, Double maxTaxRate, Instant startDate, Instant endDate);
    List<Tax> findByEmployee(Employee employee);
    Optional<Tax> findTopByEmployeeOrderByEffectiveDateDesc(Employee employee);
    List<Tax> findByEmployeeAndEffectiveDateBetween(Employee employee, Instant startDate, Instant endDate);
    List<Tax> findByEffectiveDateBetween(Instant startDate, Instant endDate);
}
