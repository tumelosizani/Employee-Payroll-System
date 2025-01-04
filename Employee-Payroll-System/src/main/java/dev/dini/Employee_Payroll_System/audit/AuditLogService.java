package dev.dini.Employee_Payroll_System.audit;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AuditLogService {

    void logAudit(String actionType, Integer employeeId, String details, Long userId, LocalDate timestamp);

    List<AuditLog> getAuditLogsByEmployeeAndDateRange(Integer employeeId, LocalDate startDate, LocalDate endDate);

    List<AuditLog> getAuditLogsByDateRange(LocalDate startDate, LocalDate endDate);
}
