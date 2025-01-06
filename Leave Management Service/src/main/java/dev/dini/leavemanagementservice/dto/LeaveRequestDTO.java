package dev.dini.leavemanagementservice.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LeaveRequestDTO {
    private Integer employeeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
}

