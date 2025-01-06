package dev.dini.payrollservice.mapper;

import dev.dini.payrollservice.dto.PayrollRequestDTO;
import dev.dini.payrollservice.dto.PayrollResponseDTO;
import dev.dini.payrollservice.payroll.Payroll;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")  // This makes it a Spring Bean automatically
public interface PayrollMapper {

    // Create an instance of the mapper
    PayrollMapper INSTANCE = Mappers.getMapper(PayrollMapper.class);

    // Map Payroll entity to PayrollResponseDTO
    @Mapping(source = "basicSalary", target = "basicSalary")
    @Mapping(source = "bonus", target = "bonus")
    @Mapping(source = "deductions", target = "deductions")
    @Mapping(source = "payDate", target = "payDate")
    @Mapping(expression = "java(payroll.calculateNetSalary())", target = "netSalary")  // Custom logic for netSalary calculation
    PayrollResponseDTO payrollToPayrollResponseDTO(Payroll payroll);

    // Map PayrollRequestDTO to Payroll entity
    @Mapping(source = "basicSalary", target = "basicSalary")
    @Mapping(source = "bonus", target = "bonus")
    @Mapping(source = "deductions", target = "deductions")
    @Mapping(source = "payDate", target = "payDate")
    Payroll payrollRequestDTOToPayroll(PayrollRequestDTO payrollRequestDTO);

    List<PayrollResponseDTO> payrollListToPayrollResponseDTOList(List<Payroll> payrolls);
}

