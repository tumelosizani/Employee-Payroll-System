package dev.dini.Employee_Payroll_System.allowances;

import dev.dini.Employee_Payroll_System.employees.Employee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "allowances")
public class Allowance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer allowanceId;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "allowance_type", nullable = false)
    private String allowanceType;  // E.g., "Housing", "Transport", "Meal"

    @Column(name = "amount", nullable = false)
    private Double amount;  // Fixed amount for allowance

    @Column(name = "percentage", nullable = true)
    private Double percentage;  // Percentage-based allowances, nullable if not applicable

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;  // Start date for this allowance

    @Column(name = "end_date", nullable = true)
    private LocalDate endDate;  // End date for this allowance (nullable if ongoing)

    // Default constructor
    public Allowance() {}

    public Double getPercentage() {
        return percentage;
    }

    public Double getAmount() {
        return amount;
    }

    public String getAllowanceType() {
        return allowanceType;
    }


    // Constructor for all fields
    public Allowance(Employee employee, String allowanceType, Double amount, Double percentage, LocalDate startDate, LocalDate endDate) {
        this.employee = employee;
        this.allowanceType = allowanceType;
        this.amount = amount;
        this.percentage = percentage;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
