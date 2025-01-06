package dev.dini.employee.management.service.mapper;

import dev.dini.employee.management.service.dto.EmployeeRequestDTO;
import dev.dini.employee.management.service.dto.EmployeeResponseDTO;
import dev.dini.employee.management.service.employee.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    // Map EmployeeRequestDTO to Employee entity
    Employee toEmployee(EmployeeRequestDTO employeeRequestDTO);

    // Map Employee entity to EmployeeResponseDTO
    EmployeeResponseDTO toEmployeeResponseDTO(Employee employee);
}
