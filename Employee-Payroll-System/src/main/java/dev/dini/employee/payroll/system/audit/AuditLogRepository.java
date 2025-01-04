package dev.dini.employee.payroll.system.audit;


import dev.dini.employee.payroll.system.employees.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Integer> {
    List<AuditLog> findByEmployee(Employee employee);

    List<AuditLog> findByEmployeeEmployeeId(Integer employeeId);


    List<AuditLog> findByActionTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);

    // If you want to find by Employee ID and action date range, here's how you'd do it
    List<AuditLog> findByEmployeeEmployeeIdAndActionTimestampBetween(Integer employeeId, LocalDateTime startDate, LocalDateTime endDate);

}
