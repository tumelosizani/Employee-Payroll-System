package dev.dini.Employee_Payroll_System.leave;


import dev.dini.Employee_Payroll_System.exception.EmployeeNotFoundException;

import java.util.List;

public interface LeaveRequestService {
    List<LeaveRequest> getLeaveRequestsByEmployee(Integer employeeId);
    LeaveRequest createLeaveRequest(Integer employeeId, LeaveRequest leaveRequest) throws EmployeeNotFoundException;
    LeaveRequest updateLeaveRequest(Integer leaveId, LeaveRequest leaveRequest);
    void deleteLeaveRequest(Integer leaveId);
    LeaveRequest approveRejectLeaveRequest(Integer leaveId, boolean approve);
    LeaveRequest changeLeaveRequestStatus(Integer leaveRequestId, boolean approve);
}
