package dev.dini.Employee_Payroll_System.exception;

public class PerformanceReviewNotFoundException extends RuntimeException {
    public PerformanceReviewNotFoundException(String message) {
        super(message);
    }
}