package dev.dini.Employee_Payroll_System.taxes;


import dev.dini.Employee_Payroll_System.employees.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "taxes")
public class Tax {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer taxId;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "tax_rate", nullable = false)
    private Double taxRate;

    @Column(name = "tax_deduction", nullable = false)
    private Double taxDeduction;

    @Column(name = "tax_code", nullable = false)
    private String taxCode;

    @Column(name = "effective_date", nullable = false)
    private Instant effectiveDate;  // Add effective date field

    // Default constructor (required by JPA)
    public Tax() {}

    // Constructor with all fields
    public Tax(Integer taxId, Employee employee, Double taxRate, Double taxDeduction, String taxCode, Instant effectiveDate) {
        this.taxId = taxId;
        this.employee = employee;
        this.taxRate = taxRate;
        this.taxDeduction = taxDeduction;
        this.taxCode = taxCode;
        this.effectiveDate = effectiveDate;  // Initialize the effective date
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public Double getTaxDeduction() {
        return taxDeduction;
    }

    // Optionally, override toString(), equals(), and hashCode() methods if needed
    @Override
    public String toString() {
        return "Tax{" +
                "taxId=" + taxId +
                ", employee=" + employee +
                ", taxRate=" + taxRate +
                ", taxDeduction=" + taxDeduction +
                ", taxCode='" + taxCode + '\'' +
                ", effectiveDate=" + effectiveDate +  // Include effective date in toString
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Tax tax = (Tax) obj;
        return taxId.equals(tax.taxId);
    }

    @Override
    public int hashCode() {
        return taxId != null ? taxId.hashCode() : 0;
    }


}