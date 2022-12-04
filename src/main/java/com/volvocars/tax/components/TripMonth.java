package com.volvocars.tax.components;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@Setter
public class TripMonth {

    private int month;
    List<Integer> dates;
}
