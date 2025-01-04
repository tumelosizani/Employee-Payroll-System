package dev.dini.Employee_Payroll_System.exception;

public class PayrollProcessingException extends RuntimeException {
    public PayrollProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}