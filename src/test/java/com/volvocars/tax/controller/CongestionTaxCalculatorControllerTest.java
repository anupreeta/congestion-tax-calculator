package com.volvocars.tax.controller;

import com.volvocars.tax.models.TaxCalculatorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CongestionTaxCalculatorControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    //Tax-free time entry - 2013-01-14 19:00:00
    @Test
    public void shouldReturnZeroTaxWhenTaxFree() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"vehicleType\": \"Car\",\"dates\": [\"2013-01-14 19:00:00\"]}", headers);
        ResponseEntity<TaxCalculatorResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/tax/calculator").toString(), request, TaxCalculatorResponse.class);
        assertEquals(0, response.getBody().getTax());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    //Public holiday time entry - 2013-06-06
    @Test
    public void shouldReturnZeroTaxOnHoliday() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"vehicleType\": \"Car\",\"dates\": [\"2013-06-06 08:10:00\"]}", headers);
        ResponseEntity<TaxCalculatorResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/tax/calculator").toString(), request, TaxCalculatorResponse.class);
        assertEquals(0, response.getBody().getTax());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    //Day Before Public holiday time entry - 2013-06-05
    @Test
    public void shouldReturnZeroTaxDayBeforeHoliday() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"vehicleType\": \"Car\",\"dates\": [\"2013-06-05 12:30:00\"]}", headers);
        ResponseEntity<TaxCalculatorResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/tax/calculator").toString(), request, TaxCalculatorResponse.class);
        assertEquals(0, response.getBody().getTax());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    //Saturday time entry - 2013-04-13
    @Test
    public void shouldReturnZeroTaxOnSaturday() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"vehicleType\": \"Car\",\"dates\": [\"2013-04-13 12:30:00\"]}", headers);
        ResponseEntity<TaxCalculatorResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/tax/calculator").toString(), request, TaxCalculatorResponse.class);
        assertEquals(0, response.getBody().getTax());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    //Sunday time entry - 2013-04-14
    @Test
    public void shouldReturnZeroTaxOnSunday() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"vehicleType\": \"Car\",\"dates\": [\"2013-04-14 12:30:00\"]}", headers);
        ResponseEntity<TaxCalculatorResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/tax/calculator").toString(), request, TaxCalculatorResponse.class);
        assertEquals(0, response.getBody().getTax());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    //Holiday Month July - 2013-07-15 09:00:00
    @Test
    public void shouldReturnZeroTaxInJuly() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"vehicleType\": \"Car\",\"dates\": [\"2013-07-15 09:00:00\"]}", headers);
        ResponseEntity<TaxCalculatorResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/tax/calculator").toString(), request, TaxCalculatorResponse.class);
        assertEquals(0, response.getBody().getTax());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void shoudlReturnZeroTaxForExemptedVehicle() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"vehicleType\": \"Motorcycles\",\"dates\": [\"2013-01-07 10:00:00\"]}", headers);
        ResponseEntity<TaxCalculatorResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/tax/calculator").toString(), request, TaxCalculatorResponse.class);
        assertEquals(0, response.getBody().getTax());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    //One working day time entry - 2013-01-02 09:15:00 Tuesday
    @Test
    public void shouldCalculateTaxForOneWorkingDayEntry() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"vehicleType\": \"Car\",\"dates\": [\"2013-01-02 09:15:00\"]}", headers);
        ResponseEntity<TaxCalculatorResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/tax/calculator").toString(), request, TaxCalculatorResponse.class);
        assertEquals(8, response.getBody().getTax());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void shouldReturnMaxTax60SEK() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"vehicleType\": \"Car\",\"dates\": ["
                + "\"2013-05-13 15:30:59\",\"2013-05-13 16:59:59\","
                + "\"2013-05-14 15:30:59\",\"2013-05-14 16:59:59\","
                + "\"2013-05-15 15:30:59\",\"2013-05-15 16:59:59\"]}", headers);
        ResponseEntity<TaxCalculatorResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/tax/calculator").toString(), request, TaxCalculatorResponse.class);
        assertEquals(60, response.getBody().getTax());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    //Two entries in 60min 2013-01-10 16:30:59, 2013-01-10 16:59:59
    @Test
    public void shouldCalculateTaxForTwoEntriesWithin60Min() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"vehicleType\": \"Car\",\"dates\": [\"2013-01-07 16:30:59\",\"2013-01-07 16:59:59\"]}", headers);
        ResponseEntity<TaxCalculatorResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/tax/calculator").toString(), request, TaxCalculatorResponse.class);
        assertEquals(18, response.getBody().getTax());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    //Two entries in 60min 2013-03-22 15:30:59, 2013-03-22 16:59:59
    @Test
    public void shouldCalculateTaxForTwoEntriesNotIn60Min() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"vehicleType\": \"Car\",\"dates\": [\"2013-03-22 15:30:59\",\"2013-03-22 16:59:59\"]}", headers);
        ResponseEntity<TaxCalculatorResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/tax/calculator").toString(), request, TaxCalculatorResponse.class);
        assertEquals(36, response.getBody().getTax());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    //Invalid Vehicle - Cycle
    @Test
    public void shouldThrowErrorWhenInvalidVehicle() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"vehicleType\": \"Cycle\",\"dates\": [\"2013-02-01 10:00:00\"]}", headers);
        ResponseEntity<TaxCalculatorResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/tax/calculator").toString(), request, TaxCalculatorResponse.class);
        assertEquals(0, response.getBody().getTax());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    //INotSupportedYear - 2018
    @Test
    public void shouldThrowErrorOnNonSupportedYear() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"vehicleType\": \"Car\",\"dates\": [\"2018-02-04 10:00:00\"]}", headers);
        ResponseEntity<TaxCalculatorResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/tax/calculator").toString(), request, TaxCalculatorResponse.class);
        assertEquals(0, response.getBody().getTax());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}
