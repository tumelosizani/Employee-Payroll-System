package dev.dini.payrollservice.payroll;

import dev.dini.payrollservice.dto.PayrollRequestDTO;
import dev.dini.payrollservice.dto.PayrollResponseDTO;
import dev.dini.payrollservice.employee.EmployeeDTO;
import dev.dini.payrollservice.employee.EmployeeServiceClient;
import dev.dini.payrollservice.exception.PayrollNotFoundException;
import dev.dini.payrollservice.mapper.PayrollMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PayrollServiceImpl implements PayrollService {

    private final PayrollRepository payrollRepository;
    private final EmployeeServiceClient employeeServiceClient;
    private final PayrollMapper payrollMapper;  // Mapper added
    private static final Logger logger = LoggerFactory.getLogger(PayrollServiceImpl.class);

    @Override
    public List<PayrollResponseDTO> getAllPayrolls() {
        logger.info("Fetching all payroll records.");
        List<Payroll> payrolls = payrollRepository.findAll();  // Fetch all payroll records
        return payrollMapper.payrollListToPayrollResponseDTOList(payrolls);  // Map the list of payrolls to DTOs
    }

    @Override
    public PayrollResponseDTO getPayrollById(Integer payrollId) {
        logger.info("Fetching payroll record with ID: {}", payrollId);
        Optional<Payroll> payroll = payrollRepository.findById(payrollId);
        if (payroll.isPresent()) {
            return payrollMapper.payrollToPayrollResponseDTO(payroll.get());  // Map to DTO
        } else {
            logger.warn("Payroll with ID {} not found.", payrollId);
            throw new PayrollNotFoundException("Payroll record not found for ID " + payrollId);  // Throw custom exception
        }
    }

    @Override
    public PayrollResponseDTO createPayroll(PayrollRequestDTO payrollRequestDTO) {
        if (payrollRequestDTO == null) {
            logger.error("PayrollRequestDTO object is null. Cannot create payroll.");
            throw new IllegalArgumentException("Payroll object must not be null.");
        }

        if (payrollRequestDTO.getEmployeeId() == null) {
            logger.error("Employee ID is missing for payroll creation. Payroll details: {}", payrollRequestDTO);
            throw new IllegalArgumentException("Employee ID is required to create payroll.");
        }

        // Fetch employee details from Employee Service for validation or logging
        EmployeeDTO employeeDTO;
        try {
            employeeDTO = employeeServiceClient.getEmployeeById(payrollRequestDTO.getEmployeeId());
            logger.info("Fetched employee details for ID: {}", payrollRequestDTO.getEmployeeId());
        } catch (Exception ex) {
            logger.error("Failed to fetch employee details for ID: {}", payrollRequestDTO.getEmployeeId(), ex);
            throw new RuntimeException("Unable to fetch employee details. Please try again later.");
        }

        // Log employee details (optional, useful for audit purposes)
        logger.debug("Employee details fetched: {} {}", employeeDTO.getFirstName(), employeeDTO.getLastName());

        // Map the DTO to the Payroll entity
        Payroll payroll = payrollMapper.payrollRequestDTOToPayroll(payrollRequestDTO);

        // Calculate net salary if not already calculated
        if (payroll.getNetSalary() == null) {
            Double netSalary = payroll.calculateNetSalary();
            payroll.setNetSalary(netSalary);
            logger.debug("Calculated net salary for payroll: {}", netSalary);
        }

        // Save and return the payroll record
        Payroll savedPayroll = payrollRepository.save(payroll);
        logger.info("Payroll successfully created with ID: {}", savedPayroll.getPayrollId());

        // Return response DTO
        return payrollMapper.payrollToPayrollResponseDTO(savedPayroll);
    }

    @Override
    public PayrollResponseDTO updatePayroll(Integer payrollId, PayrollRequestDTO payrollRequestDTO) {
        if (payrollRepository.existsById(payrollId)) {
            Payroll payroll = payrollMapper.payrollRequestDTOToPayroll(payrollRequestDTO);  // Map DTO to entity
            payroll.setPayrollId(payrollId);  // Set the existing payroll ID
            logger.info("Updating payroll record with ID: {}", payrollId);
            logger.debug("Updated payroll details: {}", payroll);  // Log updated payroll details in debug level
            Payroll updatedPayroll = payrollRepository.save(payroll);  // Update payroll record
            return payrollMapper.payrollToPayrollResponseDTO(updatedPayroll);  // Map to response DTO
        }
        logger.warn("Payroll with ID {} not found for update.", payrollId);
        throw new PayrollNotFoundException("Payroll record not found for ID " + payrollId);  // Throw custom exception
    }

    @Override
    public void deletePayroll(Integer payrollId) {
        if (payrollRepository.existsById(payrollId)) {
            logger.info("Deleting payroll record with ID: {}", payrollId);
            payrollRepository.deleteById(payrollId);  // Delete the payroll record
        } else {
            logger.warn("Payroll with ID {} not found for deletion.", payrollId);
            throw new PayrollNotFoundException("Payroll record not found for ID " + payrollId);  // Throw custom exception
        }
    }
}
