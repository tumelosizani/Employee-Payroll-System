package dev.dini.taxservice.deduction;

public class UifDeduction {

    private static final double UIF_RATE = 0.01;  // 1% of income
    private static final double UIF_MAX = 177.12; // Max UIF contribution per month per party

    public static Double calculateUifDeduction(Double income) {
        Double employeeContribution = Math.min(income * UIF_RATE, UIF_MAX);
        Double employerContribution = Math.min(income * UIF_RATE, UIF_MAX);
        return employeeContribution + employerContribution;
    }
}