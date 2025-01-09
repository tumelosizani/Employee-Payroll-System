package dev.dini.taxservice.deduction;

public class VoluntaryDeduction {

    public static Double calculateGroupLifeInsuranceDeduction(Double premium) {
        return premium; // Premium is a direct deduction.
    }

    public static Double calculateUnionMembershipDeduction(Double unionFee) {
        return unionFee; // Union fees as specified.
    }

    public static Double calculateSavingsPlanDeduction(Double contribution) {
        return contribution; // Savings plan contributions as specified.
    }
}
