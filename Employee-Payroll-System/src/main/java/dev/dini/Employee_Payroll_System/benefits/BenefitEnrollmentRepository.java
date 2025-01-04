package dev.dini.Employee_Payroll_System.benefits;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BenefitEnrollmentRepository extends JpaRepository<BenefitEnrollment, Integer> {
    BenefitEnrollment findByEmployeeEmployeeIdAndBenefitBenefitId(Integer employeeId, Integer benefitId);
}
