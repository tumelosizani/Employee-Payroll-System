package dev.dini.employee.payroll.system.exception;

public class PayrollProcessingException extends RuntimeException {
    public PayrollProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}