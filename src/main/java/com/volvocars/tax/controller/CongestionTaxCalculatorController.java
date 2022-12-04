package com.volvocars.tax.controller;

import com.volvocars.tax.models.TaxCalculatorRequest;
import com.volvocars.tax.models.TaxCalculatorResponse;
import com.volvocars.tax.service.CongestionTaxCalculator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tax")
@Api(value = "Tax Calculator Service", description = "Service to calculate the tax in Gothenburg")
public class CongestionTaxCalculatorController {

    @Autowired
    CongestionTaxCalculator congestionTaxCalculator;


    @RequestMapping(value = "/calculator", method = RequestMethod.POST)
    @Operation(summary = "Calculate congestion tax", description = "Service for calculating congestion tax for Gothengurg")
    public ResponseEntity<TaxCalculatorResponse> taxCalculate(
            @ApiParam(value = "Request for Tax Calculation, vehicle type and list of dates and Date format is : yyyy-MM-dd HH:mm:ss", required = true) @RequestBody TaxCalculatorRequest request) {
        TaxCalculatorResponse response;
        try {
            int tax = congestionTaxCalculator.getTax(request.getVehicleType(), request.getDates());

            response = new TaxCalculatorResponse(tax,
                    "Tax calculated successfully for vehicle :" + request.getVehicleType().name() + " Amount : " + tax, null);
            return new ResponseEntity<TaxCalculatorResponse>(response, HttpStatus.OK);
        } catch (Exception e) {
            response = new TaxCalculatorResponse( 0,
                    "Failed to calculate tax.", e.getLocalizedMessage());
            return new ResponseEntity<TaxCalculatorResponse>(response, HttpStatus.BAD_REQUEST);
        }
    }
}

