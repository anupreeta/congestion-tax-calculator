package com.volvocars.tax.components;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "tax-price")
@Getter
@Setter
public class TaxPrice {

    private List<TimeBasedPricing> timings;
}
