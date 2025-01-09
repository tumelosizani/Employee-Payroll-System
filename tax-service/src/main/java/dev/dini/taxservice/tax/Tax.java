package dev.dini.taxservice.tax;

import dev.dini.taxservice.bracket.TaxBracket;
import dev.dini.taxservice.calculation.TaxCalculation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tax")
public class Tax {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer taxId;

    private String name; // Name of the tax (e.g., "2024 Income Tax")
    private Integer year; // Tax year, e.g., 2024

    @OneToMany(mappedBy = "tax")  // mappedBy refers to the 'tax' field in the TaxBracket class
    private List<TaxBracket> taxBrackets; // List of tax brackets for this tax category

    @OneToMany(mappedBy = "tax", cascade = CascadeType.ALL)
    private List<TaxCalculation> taxCalculations;
}
