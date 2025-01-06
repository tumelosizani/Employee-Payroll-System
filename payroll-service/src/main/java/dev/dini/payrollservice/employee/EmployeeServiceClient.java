package dev.dini.payrollservice.employee;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "employee-service", url = "http://employee-service")
public interface EmployeeServiceClient {
    @GetMapping("/api/employees/{id}")
    EmployeeDTO getEmployeeById(@PathVariable("id") Integer id);
}

