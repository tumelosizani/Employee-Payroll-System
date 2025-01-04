package dev.dini.Employee_Payroll_System.exception;

public class InvalidPayrollDataException extends RuntimeException {
    public InvalidPayrollDataException(String message) {
        super(message);
    }
}