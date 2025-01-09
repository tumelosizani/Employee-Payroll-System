package dev.dini.taxservice.deduction;

public class FringeBenefitTax {

    // Example for a company car
    public static Double calculateCompanyCarBenefit(Double determinedValue, Double usagePercentage) {
        // Taxable benefit based on determined value and usage percentage
        return determinedValue * (usagePercentage / 100);
    }

    // Example for housing allowance
    public static Double calculateHousingAllowanceBenefit(Double allowance) {
        return allowance; // This can be refined based on SARS guidelines.
    }
}
