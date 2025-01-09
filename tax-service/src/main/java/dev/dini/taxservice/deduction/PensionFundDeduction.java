package dev.dini.taxservice.deduction;

public class PensionFundDeduction {

    private static final double PENSION_FUND_RATE = 0.075;  // Assume 7.5% of income

    public static Double calculatePensionFundDeduction(Double income) {
        return income * PENSION_FUND_RATE;
    }
}
