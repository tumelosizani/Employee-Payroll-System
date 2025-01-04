package dev.dini.employee.payroll.system.exception;

public class PayrollNotFoundException extends RuntimeException {

    public PayrollNotFoundException(String message) {
        super(message);
    }
}
