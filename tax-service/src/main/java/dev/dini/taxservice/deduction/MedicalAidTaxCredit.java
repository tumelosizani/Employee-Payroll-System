package dev.dini.taxservice.deduction;

public class MedicalAidTaxCredit {

    private static final double TAXPAYER_CREDIT = 364.0;
    private static final double FIRST_DEPENDANT_CREDIT = 364.0;
    private static final double ADDITIONAL_DEPENDANT_CREDIT = 246.0;

    public static Double calculateMedicalAidTaxCredit(int numDependants) {
        double totalCredit = TAXPAYER_CREDIT;
        totalCredit += FIRST_DEPENDANT_CREDIT;

        if (numDependants > 1) {
            totalCredit += (numDependants - 1) * ADDITIONAL_DEPENDANT_CREDIT;
        }

        return totalCredit;
    }
}
