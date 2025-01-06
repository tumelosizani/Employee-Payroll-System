package dev.dini.employee.management.service.employee;

import dev.dini.employee.management.service.dto.EmployeeRequestDTO;
import dev.dini.employee.management.service.dto.EmployeeResponseDTO;
import dev.dini.employee.management.service.exception.EmployeeNotFoundException;
import dev.dini.employee.management.service.mapper.EmployeeMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Override
    public List<EmployeeResponseDTO> getAllEmployees() {
        logger.info("Fetching all employees");
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(employeeMapper::toEmployeeResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeResponseDTO getEmployeeById(Integer employeeId) {
        logger.info("Fetching employee with id: {}", employeeId);
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> {
                    logger.error("Employee not found with id: {}", employeeId);
                    return new EmployeeNotFoundException("Employee not found with id: " + employeeId);
                });
        return employeeMapper.toEmployeeResponseDTO(employee);
    }

    @Override
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO employeeRequestDTO) {
        logger.info("Creating employee with details: {}", employeeRequestDTO);
        Employee employee = employeeMapper.toEmployee(employeeRequestDTO);
        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.toEmployeeResponseDTO(savedEmployee);
    }

    @Override
    public EmployeeResponseDTO updateEmployee(Integer employeeId, EmployeeRequestDTO employeeRequestDTO) {
        logger.info("Updating employee with id: {}", employeeId);
        if (!employeeRepository.existsById(employeeId)) {
            throw new EmployeeNotFoundException("Employee not found with id: " + employeeId);
        }
        Employee employee = employeeMapper.toEmployee(employeeRequestDTO);
        employee.setEmployeeId(employeeId);
        Employee updatedEmployee = employeeRepository.save(employee);
        return employeeMapper.toEmployeeResponseDTO(updatedEmployee);
    }

    @Override
    public boolean deleteEmployee(Integer employeeId) {
        logger.info("Deleting employee with id: {}", employeeId);
        if (!employeeRepository.existsById(employeeId)) {
            throw new EmployeeNotFoundException("Employee record not found for ID " + employeeId);
        }
        employeeRepository.deleteById(employeeId);
        return true;
    }
}
