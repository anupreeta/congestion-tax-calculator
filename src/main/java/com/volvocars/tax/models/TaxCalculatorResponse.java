package com.volvocars.tax.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
public class TaxCalculatorResponse {

    private int tax;
    private String message;
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getTax() {
        return tax;
    }

    public void setTax(int tax) {
        this.tax = tax;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TaxCalculatorResponse(int tax, String message, String error) {
        super();
        this.tax = tax;
        this.message = message;
        this.error = error;
    }

    /*
     * returns the current timestamp
     */
    public Long getTimestamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp.getTime();
    }

}
