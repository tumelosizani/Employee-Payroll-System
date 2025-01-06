package dev.dini.leavemanagementservice.leave;



import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "leave_requests")
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer leaveId;

    @NotNull(message = "Employee ID must not be null.")
    @JoinColumn(name = "employee_id")
    private Integer employeeId;

    private LeaveStatus status;

    private LocalDate startDate;
    private LocalDate endDate;

    private String reason;

    private Integer daysTaken;  // Days taken for leave



}