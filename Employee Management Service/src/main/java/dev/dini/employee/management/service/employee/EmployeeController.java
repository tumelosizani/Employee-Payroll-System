package dev.dini.employee.management.service.employee;

import dev.dini.employee.management.service.dto.EmployeeRequestDTO;
import dev.dini.employee.management.service.dto.EmployeeResponseDTO;
import dev.dini.employee.management.service.exception.EmployeeNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Get all employees
    @GetMapping
    public ResponseEntity<List<EmployeeResponseDTO>> getAllEmployees() {
        List<EmployeeResponseDTO> employees = employeeService.getAllEmployees();
        if (employees.isEmpty()) {
            return ResponseEntity.noContent().build();  // Returns 204 if no employees are found
        }
        return ResponseEntity.ok(employees);  // Returns 200 with the list of employees
    }

    // Get employee by ID
    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable Integer employeeId) {
        try {
            EmployeeResponseDTO employee = employeeService.getEmployeeById(employeeId);
            return ResponseEntity.ok(employee);  // Returns 200 with the employee object
        } catch (EmployeeNotFoundException exception) {
            return ResponseEntity.notFound().build();  // Returns 404 if employee is not found
        }
    }

    // Create new employee
    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> createEmployee(@RequestBody @Valid EmployeeRequestDTO employeeRequestDTO) {
        try {
            EmployeeResponseDTO createdEmployee = employeeService.createEmployee(employeeRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);  // Returns 201 Created
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();  // Returns 400 Bad Request if validation fails
        }
    }

    // Update employee
    @PutMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(@PathVariable Integer employeeId, @RequestBody @Valid EmployeeRequestDTO employeeRequestDTO) {
        try {
            EmployeeResponseDTO updatedEmployee = employeeService.updateEmployee(employeeId, employeeRequestDTO);
            return ResponseEntity.ok(updatedEmployee);  // Returns 200 with the updated employee
        } catch (EmployeeNotFoundException exception) {
            return ResponseEntity.notFound().build();  // Returns 404 if employee is not found
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();  // Returns 400 if validation fails
        }
    }

    // Delete employee
    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Integer employeeId) {
        try {
            boolean isDeleted = employeeService.deleteEmployee(employeeId);
            if (!isDeleted) {
                return ResponseEntity.notFound().build();  // Returns 404 if employee is not found
            }
            return ResponseEntity.noContent().build();  // Returns 204 No Content for successful deletion
        } catch (EmployeeNotFoundException exception) {
            return ResponseEntity.notFound().build();  // Returns 404 if employee is not found
        }
    }
}
