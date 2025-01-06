package dev.dini.payrollservice.employee;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "employee-service", url = "${application.config.employee-service-url}")
public interface EmployeeServiceClient {
    @GetMapping("/api/v1/employees/{employeeId}")
    EmployeeDTO getEmployeeById(@PathVariable("employeeId") Integer employeeId);
}

