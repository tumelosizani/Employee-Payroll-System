package dev.dini.employee.payroll.system.audit;

import java.time.LocalDate;
import java.util.List;

public interface AuditLogService {

    void logAudit(String actionType, Integer employeeId, String details, Long userId, LocalDate timestamp);

    List<AuditLog> getAuditLogsByEmployeeAndDateRange(Integer employeeId, LocalDate startDate, LocalDate endDate);

    List<AuditLog> getAuditLogsByDateRange(LocalDate startDate, LocalDate endDate);
}
