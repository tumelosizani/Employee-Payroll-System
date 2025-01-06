package dev.dini.leavemanagementservice.leave;

import dev.dini.leavemanagementservice.dto.LeaveRequestDTO;
import dev.dini.leavemanagementservice.dto.LeaveResponseDTO;

import java.util.List;

public interface LeaveRequestService {
    List<LeaveResponseDTO> getLeaveRequestsByEmployee(Integer employeeId);



    LeaveResponseDTO updateLeaveRequest(Integer leaveId, LeaveRequestDTO leaveRequestDTO);

    void deleteLeaveRequest(Integer leaveId);

    LeaveResponseDTO createLeaveRequest(LeaveRequestDTO leaveRequestDTO);
}
