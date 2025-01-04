package dev.dini.Employee_Payroll_System.audit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    @Autowired
    public AuditLogServiceImpl(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    public void logAudit(String actionType, Integer employeeId, String details, Long userId, LocalDate timestamp) {
        // Convert LocalDate to LocalDateTime for the timestamp
        LocalDateTime actionTimestamp = timestamp.atStartOfDay();

        // Create a new AuditLog object
        AuditLog log = new AuditLog(actionType, "employees", employeeId, "", details, actionTimestamp, userId.intValue());

        // Save the log record to the repository
        auditLogRepository.save(log);
    }

    @Override
    public List<AuditLog> getAuditLogsByEmployeeAndDateRange(Integer employeeId, LocalDate startDate, LocalDate endDate) {
        // Convert LocalDate to LocalDateTime for the query
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        // Query repository for audit logs by employee and date range
        return auditLogRepository.findByEmployeeEmployeeIdAndActionTimestampBetween(employeeId, startDateTime, endDateTime);
    }

    @Override
    public List<AuditLog> getAuditLogsByDateRange(LocalDate startDate, LocalDate endDate) {
        // Convert LocalDate to LocalDateTime for the query
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        // Query repository for all audit logs in the given date range
        return auditLogRepository.findByActionTimestampBetween(startDateTime, endDateTime);
    }
}
