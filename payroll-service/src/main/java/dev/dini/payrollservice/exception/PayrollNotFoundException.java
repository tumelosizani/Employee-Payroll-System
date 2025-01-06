package dev.dini.payrollservice.exception;

public class PayrollNotFoundException extends RuntimeException{
    public PayrollNotFoundException(String message) {
        super(message);
    }
}
