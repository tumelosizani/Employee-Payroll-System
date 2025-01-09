package dev.dini.taxservice.bracket;

import dev.dini.taxservice.calculation.TaxCalculation;
import dev.dini.taxservice.tax.Tax;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tax_brackets")
public class TaxBracket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer bracketId;

    private Double lowerLimit;  // Lower limit of the salary range
    private Double upperLimit;  // Upper limit of the salary range
    private Double baseTax;     // Base tax for this bracket
    private Double taxRate;     // Tax rate for this bracket (e.g., 0.1 for 10%)

    @ManyToOne
    @JoinColumn(name = "tax_id")  // Foreign key reference to the Tax entity
    private Tax tax; // The owning Tax entity for this TaxBracket

    // One-to-many relationship: A tax bracket can be associated with multiple tax calculations
    @OneToMany(mappedBy = "taxBracket", cascade = CascadeType.ALL)
    private List<TaxCalculation> taxCalculations;
}
