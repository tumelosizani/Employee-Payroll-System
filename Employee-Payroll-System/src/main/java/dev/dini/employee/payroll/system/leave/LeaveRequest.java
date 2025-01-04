package dev.dini.employee.payroll.system.leave;


import dev.dini.employee.payroll.system.employees.Employee;
import jakarta.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private LeaveStatus status;

    private LocalDate startDate;
    private LocalDate endDate;

    private String reason;

    private Integer daysTaken;  // Days taken for leave

    // Removed 'balance' field since it's redundant

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public void setLeaveId(Integer leaveId) {
        this.leaveId = leaveId;
    }

    public void setStatus(LeaveStatus status) {
        this.status = status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setDaysTaken(Integer daysTaken) {
        this.daysTaken = daysTaken;
    }


}
