package dev.dini.taxservice.calculation;

public class TaxThreshold {

    // Threshold values for 2025 tax year
    private static final double UNDER_65_THRESHOLD = 95750.0;
    private static final double OVER_65_THRESHOLD = 148217.0;
    private static final double OVER_75_THRESHOLD = 165689.0;

    // Method to check if income exceeds the tax threshold based on age
    public static boolean isIncomeTaxable(Double income, int age) {
        if (age >= 75) {
            return income >= OVER_75_THRESHOLD;
        } else if (age >= 65) {
            return income >= OVER_65_THRESHOLD;
        } else {
            return income >= UNDER_65_THRESHOLD;
        }
    }
}
