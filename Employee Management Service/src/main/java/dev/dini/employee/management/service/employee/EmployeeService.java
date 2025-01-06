package dev.dini.employee.management.service.employee;

import dev.dini.employee.management.service.dto.EmployeeRequestDTO;
import dev.dini.employee.management.service.dto.EmployeeResponseDTO;

import java.util.List;

public interface EmployeeService {

    // Basic employee management methods
    List<EmployeeResponseDTO> getAllEmployees();
    EmployeeResponseDTO getEmployeeById(Integer id);

    EmployeeResponseDTO createEmployee(EmployeeRequestDTO employeeRequestDTO);

    EmployeeResponseDTO updateEmployee(Integer employeeId, EmployeeRequestDTO employeeRequestDTO);

    boolean deleteEmployee(Integer id);
}
