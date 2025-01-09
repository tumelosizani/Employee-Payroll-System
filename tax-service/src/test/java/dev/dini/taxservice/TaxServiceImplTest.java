package dev.dini.taxservice;

import dev.dini.taxservice.bracket.TaxBracket;
import dev.dini.taxservice.bracket.TaxBracketRepository;
import dev.dini.taxservice.tax.TaxServiceImpl;
import dev.dini.taxservice.calculation.TaxCalculationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaxServiceImplTest {

    @Mock
    private TaxBracketRepository taxBracketRepository;

    @Mock
    private TaxCalculationRepository taxCalculationRepository;

    @InjectMocks
    private TaxServiceImpl taxService;

    private TaxBracket taxBracket;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Mock the TaxBracket for testing
        taxBracket = new TaxBracket();
        taxBracket.setLowerLimit(370500.0);
        taxBracket.setUpperLimit(512800.0);
        taxBracket.setBaseTax(77362.0);
        taxBracket.setTaxRate(0.31);

        // Mock the repository method to return this tax bracket when called with an income
        when(taxBracketRepository.findByLowerLimitLessThanEqualAndUpperLimitGreaterThanEqual(500000.0, 500000.0))
                .thenReturn(taxBracket);
    }

    @Test
    public void testCalculateTax_ValidIncome() {
        // Given an income of R500,000, the system should find the appropriate tax bracket
        Double income = 500000.0;

        // Expected tax calculation for this income
        Double expectedTax = 77362.0 + (income - 370500.0) * 0.31;

        // When calling the calculateTax method
        Double actualTax = taxService.calculateTax(income);

        // Then the actual tax should be the expected tax
        assertEquals(expectedTax, actualTax, 0.01);  // Allowing a small margin of error
    }

    @Test
    public void testCalculateTax_LowerIncome() {
        // Given an income of R200,000, the tax should be calculated with the first tax bracket
        taxBracket = new TaxBracket();
        taxBracket.setLowerLimit(0.0);
        taxBracket.setUpperLimit(237100.0);
        taxBracket.setBaseTax(0.0);
        taxBracket.setTaxRate(0.18);

        when(taxBracketRepository.findByLowerLimitLessThanEqualAndUpperLimitGreaterThanEqual(200000.0, 200000.0))
                .thenReturn(taxBracket);

        Double income = 200000.0;
        Double expectedTax = income * 0.18;

        // When calling the calculateTax method
        Double actualTax = taxService.calculateTax(income);

        // Then the actual tax should be the expected tax
        assertEquals(expectedTax, actualTax, 0.01);  // Allowing a small margin of error
    }

    @Test
    public void testCalculateTax_UpperIncome() {
        // Given an income of R2,000,000, the tax should be calculated using the highest tax bracket
        taxBracket = new TaxBracket();
        taxBracket.setLowerLimit(1817001.0);
        taxBracket.setUpperLimit(null); // The upper limit is NULL for the highest bracket
        taxBracket.setBaseTax(644489.0);
        taxBracket.setTaxRate(0.45);

        when(taxBracketRepository.findByLowerLimitLessThanEqualAndUpperLimitGreaterThanEqual(2000000.0, 2000000.0))
                .thenReturn(taxBracket);

        Double income = 2000000.0;
        Double expectedTax = 644489.0 + (income - 1817001.0) * 0.45;

        // When calling the calculateTax method
        Double actualTax = taxService.calculateTax(income);

        // Then the actual tax should be the expected tax
        assertEquals(expectedTax, actualTax, 0.01);  // Allowing a small margin of error
    }

    @Test
    public void testCalculateTax_InvalidIncome() {
        // Given an invalid income, it should throw an exception
        Double income = -10000.0;

        // When calling the calculateTax method
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            taxService.calculateTax(income);
        });

        // Then the exception message should be as expected
        assertEquals("No tax bracket found for the given income", exception.getMessage());
    }

    @Test
    public void testCalculateTax_ExactBracketEdge() {
        // Test with income that exactly matches the boundary of a tax bracket
        taxBracket = new TaxBracket();
        taxBracket.setLowerLimit(237100.0);
        taxBracket.setUpperLimit(370500.0);
        taxBracket.setBaseTax(42678.0);
        taxBracket.setTaxRate(0.26);

        when(taxBracketRepository.findByLowerLimitLessThanEqualAndUpperLimitGreaterThanEqual(237100.0, 237100.0))
                .thenReturn(taxBracket);

        Double income = 237100.0;
        Double expectedTax = 42678.0;  // Income is exactly at the lower boundary, tax is just the base tax

        // When calling the calculateTax method
        Double actualTax = taxService.calculateTax(income);

        // Then the actual tax should be the expected tax
        assertEquals(expectedTax, actualTax, 0.01);  // Allowing a small margin of error
    }
}
