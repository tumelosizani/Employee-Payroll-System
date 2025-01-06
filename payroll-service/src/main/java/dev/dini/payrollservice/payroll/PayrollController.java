package dev.dini.payrollservice.payroll;

import dev.dini.payrollservice.dto.PayrollRequestDTO;
import dev.dini.payrollservice.dto.PayrollResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payrolls")
@RequiredArgsConstructor
public class PayrollController {

    private final PayrollService payrollService;

    // Endpoint to fetch all payroll records
    @GetMapping
    public ResponseEntity<List<PayrollResponseDTO>> getAllPayrolls() {
        List<PayrollResponseDTO> payrolls = payrollService.getAllPayrolls();
        return new ResponseEntity<>(payrolls, HttpStatus.OK);
    }

    // Endpoint to fetch a specific payroll by ID
    @GetMapping("/{payrollId}")
    public ResponseEntity<PayrollResponseDTO> getPayrollById(@PathVariable Integer payrollId) {
        PayrollResponseDTO payrollResponseDTO = payrollService.getPayrollById(payrollId);
        return new ResponseEntity<>(payrollResponseDTO, HttpStatus.OK);
    }

    // Endpoint to create a new payroll
    @PostMapping
    public ResponseEntity<PayrollResponseDTO> createPayroll(@RequestBody PayrollRequestDTO payrollRequestDTO) {
        PayrollResponseDTO createdPayroll = payrollService.createPayroll(payrollRequestDTO);
        return new ResponseEntity<>(createdPayroll, HttpStatus.CREATED);
    }

    // Endpoint to update an existing payroll by ID
    @PutMapping("/{payrollId}")
    public ResponseEntity<PayrollResponseDTO> updatePayroll(
            @PathVariable Integer payrollId,
            @RequestBody PayrollRequestDTO payrollRequestDTO) {
        PayrollResponseDTO updatedPayroll = payrollService.updatePayroll(payrollId, payrollRequestDTO);
        return new ResponseEntity<>(updatedPayroll, HttpStatus.OK);
    }

    // Endpoint to delete a payroll by ID
    @DeleteMapping("/{payrollId}")
    public ResponseEntity<Void> deletePayroll(@PathVariable Integer payrollId) {
        payrollService.deletePayroll(payrollId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
