package dev.dini.taxservice.calculation;

import dev.dini.taxservice.deduction.*;
import dev.dini.taxservice.bracket.TaxBracket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class TaxCalculator {

    private final TaxCalculationRepository taxCalculationRepository;

    // Method to calculate deductions based on income, remuneration, dependants, company car, housing allowance, union fee, savings contribution, and medical aid members
    public Double calculateDeductions(Double income, Double remuneration, int numDependants, Double companyCarValue, Double housingAllowance, Double unionFee, Double savingsContribution, int totalMedicalMembers) {
        // Step 1: Calculate individual deductions
        Double retirementDeduction = RetirementFundDeduction.calculateRetirementFundDeduction(income, remuneration);
        Double medicalAidContribution = MedicalAidTaxCredit.calculateMedicalAidTaxCredit(totalMedicalMembers);
        Double companyCarBenefit = FringeBenefitTax.calculateCompanyCarBenefit(companyCarValue, 20.0); // Assuming 20% private usage
        Double housingBenefit = FringeBenefitTax.calculateHousingAllowanceBenefit(housingAllowance);
        Double unionDeduction = VoluntaryDeduction.calculateUnionMembershipDeduction(unionFee);
        Double savingsDeduction = VoluntaryDeduction.calculateSavingsPlanDeduction(savingsContribution);

        // Step 2: Sum up all deductions
        return retirementDeduction + medicalAidContribution + companyCarBenefit + housingBenefit + unionDeduction + savingsDeduction;
    }

    // Method to calculate final tax, applying deductions, rebates, and thresholds
    public Double calculateFinalTax(Double income, TaxBracket taxBracket, int numDependants, int age) {
        if (income < 0) {
            throw new IllegalArgumentException("Income must be positive");
        }

        // Step 1: Check if income exceeds the tax threshold for the given age
        if (!TaxThreshold.isIncomeTaxable(income, age)) {
            return 0.0;  // If income is below the threshold, no tax is applicable
        }

        // Step 2: Calculate base tax based on tax bracket
        Double taxAmount = calculateTaxForBracket(income, taxBracket);

        // Step 3: Apply deductions (Medical aid, UIF, pension fund, etc.)
        Double medicalAidCredit = MedicalAidTaxCredit.calculateMedicalAidTaxCredit(numDependants);
        Double uifDeduction = UifDeduction.calculateUifDeduction(income);
        Double pensionFundDeduction = PensionFundDeduction.calculatePensionFundDeduction(income);

        // Step 4: Subtract deductions from tax amount
        Double totalDeductions = medicalAidCredit + uifDeduction + pensionFundDeduction;
        Double netTax = taxAmount - totalDeductions;

        // Step 5: Apply tax rebates based on age
        netTax = TaxRebate.applyTaxRebate(netTax, age);

        // Ensure that the tax amount does not go below zero
        netTax = Math.max(netTax, 0.0);

        // Step 6: Save tax calculation to database
        saveTaxCalculation(income, netTax, taxBracket);

        return netTax;
    }

    private Double calculateTaxForBracket(Double income, TaxBracket taxBracket) {
        return taxBracket.getBaseTax() + (income - taxBracket.getLowerLimit()) * taxBracket.getTaxRate();
    }

    private void saveTaxCalculation(Double income, Double taxAmount, TaxBracket taxBracket) {
        TaxCalculation taxCalculation = new TaxCalculation();
        taxCalculation.setGrossSalary(income);
        taxCalculation.setTaxAmount(taxAmount);
        taxCalculation.setNetSalary(income - taxAmount);
        taxCalculation.setTaxBracket(taxBracket);
        taxCalculation.setDateProcessed(LocalDate.now());
        taxCalculationRepository.save(taxCalculation);
    }
}
