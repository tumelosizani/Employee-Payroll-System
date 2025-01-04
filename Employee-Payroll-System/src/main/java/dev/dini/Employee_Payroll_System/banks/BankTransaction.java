package dev.dini.Employee_Payroll_System.banks;


import dev.dini.Employee_Payroll_System.employees.Employee;
import dev.dini.Employee_Payroll_System.payroll.Payroll;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "bank_transactions")
public class BankTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer transactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payroll_id", nullable = false)
    private Payroll payroll;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "transaction_status", nullable = false)
    private TransactionStatus transactionStatus;

    @Column(name = "reference_number", unique = true)
    private String referenceNumber;



    // Default constructor (required by JPA)
    public BankTransaction() {}

    // Constructor with all fields
    public BankTransaction(Integer transactionId,
                           Payroll payroll,
                           Employee employee,
                           LocalDate transactionDate,
                           TransactionType transactionType,
                           Double amount,
                           TransactionStatus transactionStatus,
                           String referenceNumber) {
        this.transactionId = transactionId;
        this.payroll = payroll;
        this.employee = employee;
        this.transactionDate = transactionDate;
        this.transactionType = transactionType;
        this.amount = amount;
        this.transactionStatus = transactionStatus;
        this.referenceNumber = referenceNumber;
    }

    // Optionally, override toString(), equals(), and hashCode() methods if needed
    @Override
    public String toString() {
        return "BankTransaction{" +
                "transactionId=" + transactionId +
                ", payroll=" + payroll +
                ", employee=" + employee +
                ", transactionDate=" + transactionDate +
                ", transactionType='" + transactionType + '\'' +
                ", amount=" + amount +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BankTransaction that = (BankTransaction) obj;
        return transactionId.equals(that.transactionId);
    }

    @Override
    public int hashCode() {
        return transactionId != null ? transactionId.hashCode() : 0;
    }

    public void setStatus(TransactionStatus status) {
        this.transactionStatus = status;
    }


    public Payroll getPayroll() {
        return payroll;
    }

    public Employee getEmployee() {
        return employee;
    }


    public Double getAmount() {
        return amount;
    }


    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public void setPayroll(Payroll payroll) {
        this.payroll = payroll;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

}