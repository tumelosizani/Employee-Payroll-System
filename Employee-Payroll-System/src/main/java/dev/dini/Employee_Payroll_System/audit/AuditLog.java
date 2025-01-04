package dev.dini.Employee_Payroll_System.audit;


import dev.dini.Employee_Payroll_System.employees.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "audit_log")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer auditId;

    @Column(name = "action_type", nullable = false)
    private String actionType;

    @Column(name = "table_name", nullable = false)
    private String tableName;

    @Column(name = "record_id", nullable = false)
    private Integer recordId;

    @Column(name = "old_value", columnDefinition = "TEXT")
    private String oldValue;

    @Column(name = "new_value", columnDefinition = "TEXT")
    private String newValue;

    @Column(name = "action_timestamp", nullable = false)
    private LocalDateTime actionTimestamp;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @ManyToOne
    @JoinColumn(name = "employee_id")  // Foreign key to Employee
    private Employee employee;


    // Constructor without Employee parameter
    public AuditLog(String actionType, String tableName, Integer recordId, String oldValue, String newValue, LocalDateTime actionTimestamp, Integer userId) {
        this.actionType = actionType;
        this.tableName = tableName;
        this.recordId = recordId;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.actionTimestamp = actionTimestamp;
        this.userId = userId;
    }

    // Constructor with Employee parameter
    public AuditLog(String actionType, String tableName, Integer recordId, String oldValue, String newValue, LocalDateTime actionTimestamp, Integer userId, Employee employee) {
        this(actionType, tableName, recordId, oldValue, newValue, actionTimestamp, userId);
        this.employee = employee;
    }

}