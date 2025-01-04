package dev.dini.employee.management.service.employee;

import java.util.List;

public interface EmployeeService {

    // Basic employee management methods
    List<Employee> getAllEmployees();
    Employee getEmployeeById(Integer id);
    Employee createEmployee(Employee employee);
    Employee updateEmployee(Integer id, Employee employee);
    boolean deleteEmployee(Integer id);
}
