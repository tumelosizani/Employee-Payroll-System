package dev.dini.Employee_Payroll_System.reports;


import dev.dini.Employee_Payroll_System.payroll.Payroll;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "payroll_reports")
public class PayrollReport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer reportId;

    private LocalDate startDate;
    private LocalDate endDate;
    private Double totalGrossSalary;
    private Double totalDeductions;
    private Double totalNetSalary;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "payroll_report_id")
    private List<Payroll> payrolls;

    // Constructor for creating the report with payroll data
    public PayrollReport(LocalDate startDate, LocalDate endDate, Double totalGrossSalary, Double totalDeductions, Double totalNetSalary, List<Payroll> payrolls) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalGrossSalary = totalGrossSalary;
        this.totalDeductions = totalDeductions;
        this.totalNetSalary = totalNetSalary;
        this.payrolls = payrolls;
    }

    // Default constructor (required by JPA)
    public PayrollReport() {
    }

    public PayrollReport(List<Payroll> payrolls) {
    }

    // Method to calculate the total payroll amounts from the list of Payrolls
    public static PayrollReport generateReport(List<Payroll> payrolls, LocalDate startDate, LocalDate endDate) {
        Double totalGrossSalary = payrolls.stream().mapToDouble(Payroll::getBasicSalary).sum();
        Double totalDeductions = payrolls.stream().mapToDouble(Payroll::getDeductions).sum();
        Double totalNetSalary = payrolls.stream().mapToDouble(Payroll::getNetSalary).sum();

        return new PayrollReport(startDate, endDate, totalGrossSalary, totalDeductions, totalNetSalary, payrolls);
    }

    // Optional: You can add other methods for specific report features, e.g., by department, position, etc.
}
