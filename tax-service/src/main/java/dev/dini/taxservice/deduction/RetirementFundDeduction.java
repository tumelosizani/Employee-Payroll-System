package dev.dini.taxservice.deduction;

public class RetirementFundDeduction {
    private static final double MAX_PERCENTAGE = 27.5; // Max percentage of remuneration or taxable income
    private static final double MAX_CAP = 350000.0;   // Annual cap

    public static Double calculateRetirementFundDeduction(Double income, Double remuneration) {
        // Calculate 27.5% of remuneration or taxable income
        Double deduction = Math.min(income, remuneration) * (MAX_PERCENTAGE / 100);

        // Apply the annual cap of R350,000
        return Math.min(deduction, MAX_CAP);
    }
}
