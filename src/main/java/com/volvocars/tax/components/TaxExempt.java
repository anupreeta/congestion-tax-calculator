package com.volvocars.tax.components;

import java.util.Set;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "tax-exempt")
public class TaxExempt {

    private Set<String> vehicles;

}
