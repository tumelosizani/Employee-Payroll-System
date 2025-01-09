package dev.dini.taxservice.tax;

public class TaxDataNotFoundException extends RuntimeException {
    public TaxDataNotFoundException(String message) {
        super(message);
    }
}