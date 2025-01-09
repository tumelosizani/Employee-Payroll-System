package dev.dini.taxservice.tax;

import dev.dini.taxservice.bracket.TaxBracket;
import dev.dini.taxservice.bracket.TaxBracketRepository;
import dev.dini.taxservice.calculation.TaxCalculation;
import dev.dini.taxservice.calculation.TaxCalculationRepository;
import dev.dini.taxservice.calculation.TaxCalculator;
import dev.dini.taxservice.deduction.MedicalAidTaxCredit;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TaxServiceImpl implements TaxService {

    private static final Logger logger = LoggerFactory.getLogger(TaxServiceImpl.class);

    private final TaxCalculator taxCalculator;
    private final TaxRepository taxRepository;
    private final TaxBracketRepository taxBracketRepository;
    private final TaxCalculationRepository taxCalculationRepository;

    @Override
    public Double calculateTax(Double income) {
        if (income < 0) {
            throw new IllegalArgumentException("Income must be positive");
        }

        int currentYear = LocalDate.now().getYear();

        // Retrieve tax data for the current year
        Tax tax = taxRepository.findByYear(currentYear);
        if (tax == null) {
            logger.error("Tax data for the year {} not found.", currentYear);
            throw new TaxDataNotFoundException("Tax data for the year " + currentYear + " not found.");
        }
        logger.info("Tax data for the year {} found.", currentYear);

        // Retrieve tax bracket based on income
        TaxBracket taxBracket = taxBracketRepository.findByLowerLimitLessThanEqualAndUpperLimitGreaterThanEqual(income, income);
        if (taxBracket == null) {
            // Fallback to the highest tax bracket if not found
            taxBracket = taxBracketRepository.findTopByOrderByUpperLimitDesc();
            if (income > taxBracket.getUpperLimit()) {
                logger.warn("Income {} exceeds the highest tax bracket. Using the highest tax bracket for calculation.", income);
                Double taxAmount = calculateTaxForBracket(income, taxBracket);
                taxAmount -= MedicalAidTaxCredit.calculateMedicalAidTaxCredit(1); // Assuming 1 dependant for simplicity
                saveTaxCalculation(income, taxAmount, taxBracket, tax);
                return taxAmount;
            }
        }

        // Calculate tax based on the found tax bracket
        Double taxAmount = calculateTaxForBracket(income, taxBracket);
        taxAmount -= MedicalAidTaxCredit.calculateMedicalAidTaxCredit(1); // Assuming 1 dependant for simplicity
        logger.info("Calculated tax for income {}: {}", income, taxAmount);
        saveTaxCalculation(income, taxAmount, taxBracket, tax);

        return taxAmount;
    }

    private Double calculateTaxForBracket(Double income, TaxBracket taxBracket) {
        return taxBracket.getBaseTax() + (income - taxBracket.getLowerLimit()) * taxBracket.getTaxRate();
    }

    private void saveTaxCalculation(Double income, Double taxAmount, TaxBracket taxBracket, Tax tax) {
        TaxCalculation taxCalculation = new TaxCalculation();
        taxCalculation.setGrossSalary(income);
        taxCalculation.setTaxAmount(taxAmount);
        taxCalculation.setNetSalary(income - taxAmount);
        taxCalculation.setTax(tax);
        taxCalculation.setTaxBracket(taxBracket);
        taxCalculation.setDateProcessed(LocalDate.now());
        taxCalculationRepository.save(taxCalculation);
    }
}