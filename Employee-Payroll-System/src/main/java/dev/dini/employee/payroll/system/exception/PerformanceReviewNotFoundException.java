package dev.dini.employee.payroll.system.exception;

public class PerformanceReviewNotFoundException extends RuntimeException {
    public PerformanceReviewNotFoundException(String message) {
        super(message);
    }
}