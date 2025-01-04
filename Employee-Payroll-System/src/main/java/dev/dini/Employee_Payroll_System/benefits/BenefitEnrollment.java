package dev.dini.Employee_Payroll_System.benefits;

import dev.dini.Employee_Payroll_System.employees.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "benefit_enrollments")
public class BenefitEnrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer benefitEnrollmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;  // Employee enrolled in the benefit

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "benefit_id", nullable = false)
    private Benefit benefit;  // The benefit the employee has enrolled in

    @Column(name = "enrollment_date")
    private LocalDate enrollmentDate;  // Date of enrollment

    @Column(name = "status", nullable = false)
    private String status;  // Enrollment status (e.g., Active, Pending, Expired)

    @Column(name = "coverage_start_date")
    private LocalDate coverageStartDate;  // Coverage start date

    @Column(name = "coverage_end_date")
    private LocalDate coverageEndDate;  // Coverage end date

    // Default constructor required by JPA
    public BenefitEnrollment() {}

    // Constructor with all fields
    public BenefitEnrollment(Employee employee, Benefit benefit, LocalDate enrollmentDate,
                             String status, LocalDate coverageStartDate, LocalDate coverageEndDate) {
        this.employee = employee;
        this.benefit = benefit;
        this.enrollmentDate = enrollmentDate;
        this.status = status;
        this.coverageStartDate = coverageStartDate;
        this.coverageEndDate = coverageEndDate;
    }

    @Override
    public String toString() {
        return "BenefitEnrollment{" +
                "enrollmentId=" + benefitEnrollmentId +
                ", employee=" + employee +
                ", benefit=" + benefit +
                ", enrollmentDate=" + enrollmentDate +
                ", status='" + status + '\'' +
                ", coverageStartDate=" + coverageStartDate +
                ", coverageEndDate=" + coverageEndDate +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BenefitEnrollment that = (BenefitEnrollment) obj;
        return benefitEnrollmentId.equals(that.benefitEnrollmentId);
    }

    @Override
    public int hashCode() {
        return benefitEnrollmentId != null ? benefitEnrollmentId.hashCode() : 0;
    }

    public void setBenefit(Benefit benefit) {
        this.benefit = benefit;
    }
}
