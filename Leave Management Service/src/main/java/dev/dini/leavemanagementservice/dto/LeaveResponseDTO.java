package dev.dini.leavemanagementservice.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LeaveResponseDTO {
    private Integer leaveId;
    private Integer employeeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private String reason;
}

