package dev.dini.Employee_Payroll_System.exception;

public class BankTransactionNotFoundException extends RuntimeException {
    public BankTransactionNotFoundException(String message) {
        super(message);
    }
}
