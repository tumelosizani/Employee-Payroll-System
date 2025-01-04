package dev.dini.employee.payroll.system.leave;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Integer> {
    List<LeaveRequest> findByEmployeeEmployeeId(Integer employeeId);
}
