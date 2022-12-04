package com.volvocars.tax.exceptions;

public class NotSupportedTaxYearException extends RuntimeException {
    public NotSupportedTaxYearException(String message) {
        super(message);
    }

    public NotSupportedTaxYearException() {
        this("Only tax year 2013 is supported.");
    }
}
