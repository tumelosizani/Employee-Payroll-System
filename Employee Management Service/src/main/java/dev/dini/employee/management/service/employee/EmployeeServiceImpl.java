package dev.dini.employee.management.service.employee;


import dev.dini.employee.management.service.exception.EmployeeNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);


    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Employee> getAllEmployees() {
        logger.info("Fetching all employees");
        return employeeRepository.findAll();
    }

    @Override
    public Employee getEmployeeById(Integer employeeId) {
        logger.info("Fetching employee with id: {}", employeeId);
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> {logger.error("Employee not found with id: {}", employeeId);
                    return new EmployeeNotFoundException("Employee not found with id: " + employeeId);
                });
    }

    @Override
    public Employee createEmployee(Employee employee) {
        logger.info("Creating employee with details: {}", employee);
        return employeeRepository.save(employee);
    }

    @Override
    public Employee updateEmployee(Integer employeeId, Employee employee) {
        logger.info("Updating employee with id: {}", employeeId);
        if (!employeeRepository.existsById(employeeId)) {
            throw new EmployeeNotFoundException("Employee not found with id: " + employeeId);
        }
        employee.setEmployeeId(employeeId);
        return employeeRepository.save(employee);
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
