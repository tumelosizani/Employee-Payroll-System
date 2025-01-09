package dev.dini.taxservice.deduction;

public class BargainingCouncilContribution {

    public static Double calculateBargainingCouncilDeduction(Double salary, Double contributionRate) {
        // Calculate deduction based on a fixed rate
        return salary * (contributionRate / 100);
    }
}
