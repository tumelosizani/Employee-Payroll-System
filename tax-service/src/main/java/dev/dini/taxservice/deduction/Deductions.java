package dev.dini.taxservice.deduction;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "deductions")
public class Deductions {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer deductionId;
    private Integer employeeId;
    private Integer payrollId;
    private Double pensionContribution;
    private Double medicalAid;
    private Double charitableDonations;
    private Double otherDeductions;
    private Double totalDeductions;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
}
