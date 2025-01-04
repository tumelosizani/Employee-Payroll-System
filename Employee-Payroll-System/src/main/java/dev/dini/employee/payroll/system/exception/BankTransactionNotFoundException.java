package dev.dini.employee.payroll.system.exception;

public class BankTransactionNotFoundException extends RuntimeException {
    public BankTransactionNotFoundException(String message) {
        super(message);
    }
}
