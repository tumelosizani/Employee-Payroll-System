package dev.dini.taxservice.calculation;

public class TaxRebate {

    // Tax rebate values for 2025
    private static final double PRIMARY_REBATE_2025 = 17235.0;
    private static final double SECONDARY_REBATE_2025 = 9444.0;
    private static final double TERTIARY_REBATE_2025 = 3145.0;

    // Method to apply tax rebates based on age
    public static Double applyTaxRebate(Double netTax, int age) {
        if (age >= 75) {
            netTax -= TERTIARY_REBATE_2025; // Tertiary rebate for 75 and older
        } else if (age >= 65) {
            netTax -= SECONDARY_REBATE_2025; // Secondary rebate for 65 and older
        } else {
            netTax -= PRIMARY_REBATE_2025; // Primary rebate for others
        }

        // Ensure that the tax amount doesn't go negative
        return Math.max(netTax, 0.0);
    }
}
