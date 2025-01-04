package dev.dini.employee.payroll.system.payroll;



import dev.dini.employee.payroll.system.employees.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PayrollRepository extends JpaRepository<Payroll, Integer> {
    List<Payroll> findByEmployee(Employee employee);
    List<Payroll> findByPayDateBetween(LocalDate startDate, LocalDate endDate);
    List<Payroll> findByEmployeeEmployeeIdAndPayDateBetween(Integer employeeId, LocalDate startDate, LocalDate endDate);
    Payroll findByEmployeeEmployeeId(Integer employeeId);
    List<Payroll> findByEmployeeEmployeeIdAndStatus(Integer employee_employeeId, PayrollStatus status);
    List<Payroll> findByPayDateBeforeAndStatus(LocalDate payDate, PayrollStatus status);
}
