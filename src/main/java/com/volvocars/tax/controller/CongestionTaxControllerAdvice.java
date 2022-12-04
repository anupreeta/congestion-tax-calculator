package com.volvocars.tax.controller;

import com.volvocars.tax.exceptions.NotSupportedTaxYearException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class CongestionTaxControllerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler({ NotSupportedTaxYearException.class})
    public ResponseEntity<Object> handleCustomException(
            RuntimeException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("error", ex.getMessage());
        body.put("status", HttpStatus.BAD_REQUEST);
        body.put("path", request.getContextPath());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
