package dev.dini.leavemanagementservice.leave;

import dev.dini.leavemanagementservice.dto.LeaveRequestDTO;
import dev.dini.leavemanagementservice.dto.LeaveResponseDTO;
import dev.dini.leavemanagementservice.employee.EmployeeServiceClient;
import dev.dini.leavemanagementservice.exception.LeaveRequestNotFoundException;
import dev.dini.leavemanagementservice.mapper.LeaveRequestMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveRequestServiceImpl implements LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeServiceClient employeeServiceClient; // REST client to call EmployeeService
    private final LeaveRequestMapper leaveRequestMapper;
    private static final Logger logger = LoggerFactory.getLogger(LeaveRequestServiceImpl.class);

    @Override
    public List<LeaveResponseDTO> getLeaveRequestsByEmployee(Integer employeeId) {
        List<LeaveRequest> leaveRequests = leaveRequestRepository.findByEmployeeId(employeeId);

        if (leaveRequests.isEmpty()) {
            logger.warn("No leave requests found for employee with ID: {}", employeeId);
        }

        // Map List of Entities to List of Response DTOs
        return leaveRequests.stream()
                .map(leaveRequestMapper::toResponseDTO)
                .toList();
    }


    @Override
    public LeaveResponseDTO createLeaveRequest(LeaveRequestDTO leaveRequestDTO) {
        Integer employeeId = leaveRequestDTO.getEmployeeId(); // Extract employeeId from DTO

        // Validate employee existence using the EmployeeService
        try {
            boolean employeeExists = employeeServiceClient.checkEmployeeExists(employeeId);
            if (!employeeExists) {
                logger.warn("Employee with ID {} does not exist.", employeeId);
                throw new LeaveRequestNotFoundException("Employee not found with ID: " + employeeId);
            }
        } catch (Exception ex) {
            logger.error("Failed to validate employee existence for ID: {}", employeeId, ex);
            throw new RuntimeException("Unable to validate employee. Please try again later.");
        }

        // Map DTO to Entity
        LeaveRequest leaveRequest = leaveRequestMapper.toEntity(leaveRequestDTO);
        leaveRequest.setEmployeeId(employeeId); // Set employeeId explicitly if not already in the entity
        leaveRequest.setStatus(LeaveStatus.PENDING);

        logger.info("Creating leave request for employee with ID: {}", employeeId);
        LeaveRequest savedLeaveRequest = leaveRequestRepository.save(leaveRequest);

        // Map Entity to Response DTO
        return leaveRequestMapper.toResponseDTO(savedLeaveRequest);
    }


    @Override
    public LeaveResponseDTO updateLeaveRequest(Integer leaveId, LeaveRequestDTO leaveRequestDTO) {
        LeaveRequest existingLeaveRequest = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new LeaveRequestNotFoundException("Leave request not found for ID: " + leaveId));

        // Map updated values from DTO to the existing entity
        LeaveRequest updatedLeaveRequest = leaveRequestMapper.toEntity(leaveRequestDTO);
        updatedLeaveRequest.setLeaveId(leaveId); // Ensure the ID remains consistent

        logger.info("Updating leave request with ID: {}", leaveId);
        LeaveRequest savedLeaveRequest = leaveRequestRepository.save(updatedLeaveRequest);

        // Map Entity to Response DTO
        return leaveRequestMapper.toResponseDTO(savedLeaveRequest);
    }


    @Override
    public void deleteLeaveRequest(Integer leaveId) {
        if (leaveRequestRepository.existsById(leaveId)) {
            leaveRequestRepository.deleteById(leaveId);
            logger.info("Deleted leave request with ID: {}", leaveId);
        } else {
            logger.warn("Leave request with ID {} not found for deletion.", leaveId);
            throw new LeaveRequestNotFoundException("Leave request not found for ID: " + leaveId);
        }
    }


}
