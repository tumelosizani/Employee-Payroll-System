package dev.dini.leavemanagementservice.leave;

import dev.dini.leavemanagementservice.dto.LeaveRequestDTO;
import dev.dini.leavemanagementservice.dto.LeaveResponseDTO;
import dev.dini.leavemanagementservice.exception.LeaveRequestNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/leave-requests")
@RequiredArgsConstructor
public class LeaveRequestController {

    private static final Logger logger = LoggerFactory.getLogger(LeaveRequestController.class);

    private final LeaveRequestService leaveRequestService;

    /**
     * Endpoint to create a leave request for an employee.
     *
     * @param leaveRequestDTO DTO containing the leave request details.
     * @return ResponseEntity with the created leave request details.
     */
    @PostMapping
    public ResponseEntity<LeaveResponseDTO> createLeaveRequest(@RequestBody LeaveRequestDTO leaveRequestDTO) {
        try {
            LeaveResponseDTO leaveResponseDTO = leaveRequestService.createLeaveRequest(leaveRequestDTO);
            return new ResponseEntity<>(leaveResponseDTO, HttpStatus.CREATED);
        } catch (LeaveRequestNotFoundException ex) {
            logger.error("Error creating leave request: {}", ex.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            logger.error("Error creating leave request: {}", ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint to get all leave requests for an employee.
     *
     * @param employeeId Employee ID for fetching leave requests.
     * @return List of LeaveResponseDTO for the employee.
     */
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<LeaveResponseDTO>> getLeaveRequestsByEmployee(@PathVariable Integer employeeId) {
        List<LeaveResponseDTO> leaveRequests = leaveRequestService.getLeaveRequestsByEmployee(employeeId);
        if (leaveRequests.isEmpty()) {
            logger.warn("No leave requests found for employee with ID: {}", employeeId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(leaveRequests, HttpStatus.OK);
    }

    /**
     * Endpoint to update a leave request.
     *
     * @param leaveId Leave request ID.
     * @param leaveRequestDTO DTO containing the updated leave request data.
     * @return ResponseEntity with the updated leave request details.
     */
    @PutMapping("/{leaveId}")
    public ResponseEntity<LeaveResponseDTO> updateLeaveRequest(@PathVariable Integer leaveId,
                                                               @RequestBody LeaveRequestDTO leaveRequestDTO) {
        try {
            LeaveResponseDTO updatedLeaveRequest = leaveRequestService.updateLeaveRequest(leaveId, leaveRequestDTO);
            if (updatedLeaveRequest == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(updatedLeaveRequest, HttpStatus.OK);
        } catch (LeaveRequestNotFoundException ex) {
            logger.error("Error updating leave request: {}", ex.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            logger.error("Error updating leave request: {}", ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint to delete a leave request.
     *
     * @param leaveId Leave request ID.
     * @return ResponseEntity indicating success or failure.
     */
    @DeleteMapping("/{leaveId}")
    public ResponseEntity<Void> deleteLeaveRequest(@PathVariable Integer leaveId) {
        try {
            leaveRequestService.deleteLeaveRequest(leaveId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (LeaveRequestNotFoundException ex) {
            logger.error("Error deleting leave request: {}", ex.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            logger.error("Error deleting leave request: {}", ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
