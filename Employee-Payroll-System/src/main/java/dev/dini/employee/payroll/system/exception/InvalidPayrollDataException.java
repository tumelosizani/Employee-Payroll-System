package dev.dini.employee.payroll.system.exception;

public class InvalidPayrollDataException extends RuntimeException {
    public InvalidPayrollDataException(String message) {
        super(message);
    }
}