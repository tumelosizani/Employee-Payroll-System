package dev.dini.employee.payroll.system.banks;

import dev.dini.employee.payroll.system.employees.Employee;
import dev.dini.employee.payroll.system.payroll.Payroll;

import java.time.LocalDate;


public class TransactionSummary {

    private Payroll payroll;
    private Employee employee;
    private LocalDate startDate;
    private LocalDate endDate;
    private TransactionType transactionType;
    private Double totalAmount;
    private Long transactionCount;

    public TransactionSummary(Payroll payroll,
                              Employee employee,
                              LocalDate startDate,
                              LocalDate endDate,
                              TransactionType transactionType,
                              Double totalAmount,
                              Long transactionCount) {
        this.payroll = payroll;
        this.employee = employee;
        this.startDate = startDate;
        this.endDate = endDate;
        this.transactionType = transactionType;
        this.totalAmount = totalAmount;
        this.transactionCount = transactionCount;
    }

    // Getters and Setters
    public Payroll getPayroll() {
        return payroll;
    }

    public void setPayroll(Payroll payroll) {
        this.payroll = payroll;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(Long transactionCount) {
        this.transactionCount = transactionCount;
    }

    // toString Method
    @Override
    public String toString() {
        return "TransactionSummary{" +
                "payroll=" + payroll +
                ", employee=" + employee +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", transactionType=" + transactionType +
                ", totalAmount=" + totalAmount +
                ", transactionCount=" + transactionCount +
                '}';
    }
}
