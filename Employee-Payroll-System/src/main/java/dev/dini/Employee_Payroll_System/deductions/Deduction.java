package dev.dini.Employee_Payroll_System.deductions;


import dev.dini.Employee_Payroll_System.employees.Employee;
import dev.dini.Employee_Payroll_System.payroll.Payroll;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "deductions")
public class Deduction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer deductionId;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;


    @ManyToOne
    @JoinColumn(name = "payroll_id", nullable = false)
    private Payroll payroll;

    @Column(name = "deduction_type", nullable = false)
    private String deductionType;

    @Column(name = "amount", nullable = false)
    private Double amount;

    private Double percentage;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    // Default constructor (required by JPA)
    public Deduction() {}

    // Constructor with all fields
    public Deduction(Integer deductionId, Payroll payroll, String deductionType, Double amount) {
        this.deductionId = deductionId;
        this.payroll = payroll;
        this.deductionType = deductionType;
        this.amount = amount;
    }

    public Double getPercentage() {
        return percentage;
    }

    public Double getAmount() {
        return amount;
    }

    public String getDeductionType() {
        return deductionType;
    }



    // Optionally, override toString(), equals(), and hashCode() methods if needed
    @Override
    public String toString() {
        return "Deduction{" +
                "deductionId=" + deductionId +
                ", payroll=" + payroll +
                ", deductionType='" + deductionType + '\'' +
                ", amount=" + amount +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Deduction deduction = (Deduction) obj;
        return deductionId.equals(deduction.deductionId);
    }

    @Override
    public int hashCode() {
        return deductionId != null ? deductionId.hashCode() : 0;
    }


}