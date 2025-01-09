package dev.dini.taxservice.calculation;

import dev.dini.taxservice.bracket.TaxBracket;
import dev.dini.taxservice.tax.Tax;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tax_calculation")
public class TaxCalculation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer calculationId; // Unique ID for this calculation

    private Integer employeeId;    // Link to the employee
    private Double grossSalary; // Gross salary of the employee
    private Double taxAmount;   // Calculated tax amount
    private Double netSalary;   // Net salary after tax
    private LocalDate dateProcessed;  // Date of calculation

    // Many-to-one relationship: Each tax calculation is linked to a specific tax
    @ManyToOne
    @JoinColumn(name = "tax_id")
    private Tax tax;

    // Many-to-one relationship: Each tax calculation is linked to a specific tax bracket
    @ManyToOne
    @JoinColumn(name = "tax_bracket_id")
    private TaxBracket taxBracket; // Link to the tax bracket used for calculation

}
