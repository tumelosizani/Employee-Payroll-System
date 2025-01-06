package dev.dini.payrollservice.payroll;

import dev.dini.payrollservice.dto.PayrollRequestDTO;
import dev.dini.payrollservice.dto.PayrollResponseDTO;

import java.util.List;

public interface PayrollService {
    // Fetch all payroll records and return a list of PayrollResponseDTOs
    List<PayrollResponseDTO> getAllPayrolls();

    // Fetch a payroll by its ID and return it as a PayrollResponseDTO
    PayrollResponseDTO getPayrollById(Integer payrollId);

    // Create a new payroll from the provided PayrollRequestDTO
    PayrollResponseDTO createPayroll(PayrollRequestDTO payrollRequestDTO);

    // Update an existing payroll by its ID using the provided PayrollRequestDTO
    PayrollResponseDTO updatePayroll(Integer payrollId, PayrollRequestDTO payrollRequestDTO);

    // Delete a payroll by its ID
    void deletePayroll(Integer payrollId);

}
