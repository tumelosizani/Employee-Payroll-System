package dev.dini.employee.payroll.system.leave;


import dev.dini.employee.payroll.system.employees.Employee;
import dev.dini.employee.payroll.system.employees.EmployeeRepository;
import dev.dini.employee.payroll.system.exception.EmployeeNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LeaveRequestServiceImpl implements LeaveRequestService{

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;
    private static final Logger logger = LoggerFactory.getLogger(LeaveRequestServiceImpl.class);

    public LeaveRequestServiceImpl(LeaveRequestRepository leaveRequestRepository, EmployeeRepository employeeRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<LeaveRequest> getLeaveRequestsByEmployee(Integer employeeId) {
        List<LeaveRequest> leaveRequests = leaveRequestRepository.findByEmployeeEmployeeId(employeeId);
        if (leaveRequests.isEmpty()) {
            logger.warn("No leave requests found for employee with ID: {}", employeeId);
        }
        return leaveRequests;
    }

    @Override
    public LeaveRequest createLeaveRequest(Integer employeeId, LeaveRequest leaveRequest) throws EmployeeNotFoundException {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with ID: " + employeeId));

        // Add validation logic here for the leave request if needed
        leaveRequest.setEmployee(employee);
        leaveRequest.setStatus(LeaveStatus.PENDING);
        logger.info("Creating leave request for employee with ID: {}", employeeId);
        return leaveRequestRepository.save(leaveRequest);
    }

    @Override
    public LeaveRequest updateLeaveRequest(Integer leaveId, LeaveRequest leaveRequest) {
        Optional<LeaveRequest> existingLeaveRequest = leaveRequestRepository.findById(leaveId);
        if (existingLeaveRequest.isPresent()){
            leaveRequest.setLeaveId(leaveId);
            return leaveRequestRepository.save(leaveRequest);
        }
        return null;
    }

    @Override
    public void deleteLeaveRequest(Integer leaveId) {
        leaveRequestRepository.deleteById(leaveId);
        logger.info("Deleted leave request with ID: {}", leaveId);
    }


    @Override
    public LeaveRequest approveRejectLeaveRequest(Integer leaveId, boolean approve) {
        Optional<LeaveRequest> leaveRequestOptional = leaveRequestRepository.findById(leaveId);
        if (leaveRequestOptional.isPresent()){
            LeaveRequest leaveRequest = leaveRequestOptional.get();
            leaveRequest.setStatus(approve ? LeaveStatus.APPROVED : LeaveStatus.REJECTED);
            return leaveRequestRepository.save(leaveRequest);
        }
        return null;
    }

    @Override
    public LeaveRequest changeLeaveRequestStatus(Integer leaveRequestId, boolean approve) {
        Optional<LeaveRequest> leaveRequestOptional = leaveRequestRepository.findById(leaveRequestId);
        if (leaveRequestOptional.isPresent()) {
            LeaveRequest leaveRequest = leaveRequestOptional.get();
            LeaveStatus status = approve ? LeaveStatus.APPROVED : LeaveStatus.REJECTED;
            leaveRequest.setStatus(status);
            logger.info("Updated status of leave request with ID: {} to {}", leaveRequestId, status);
            return leaveRequestRepository.save(leaveRequest);
        }
        logger.error("Leave request with ID: {} not found", leaveRequestId);
        return null;  // Or throw a custom exception
    }

}
