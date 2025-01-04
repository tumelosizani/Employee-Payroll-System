package dev.dini.Employee_Payroll_System.benefits;


import dev.dini.Employee_Payroll_System.employees.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "benefits")
public class Benefit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer benefitId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "benefit_type", nullable = false)
    private String benefitType;

    @Column(name = "benefit_amount", nullable = false)
    private Double benefitAmount;

    @Column(name = "employee_share", nullable = false)
    private Double employeeShare;

    @Column(name = "enrollment_date")
    private LocalDate enrollmentDate;

    // Default constructor (required by JPA)
    public Benefit() {}

    // Constructor with all fields
    public Benefit(Integer benefitId, Employee employee, String benefitType, Double benefitAmount, Double employeeShare, LocalDate enrollmentDate) {
        this.benefitId = benefitId;
        this.employee = employee;
        this.benefitType = benefitType;
        this.benefitAmount = benefitAmount;
        this.employeeShare = employeeShare;
        this.enrollmentDate = enrollmentDate;
    }

    @Override
    public String toString() {
        return "Benefit{" +
                "benefitId=" + benefitId +
                ", employee=" + employee +
                ", benefitType='" + benefitType + '\'' +
                ", benefitAmount=" + benefitAmount +
                ", employeeShare=" + employeeShare +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Benefit benefit = (Benefit) obj;
        return benefitId.equals(benefit.benefitId);
    }

    @Override
    public int hashCode() {
        return benefitId != null ? benefitId.hashCode() : 0;
    }

    public Integer getBenefitId() {
        return benefitId;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }
}
