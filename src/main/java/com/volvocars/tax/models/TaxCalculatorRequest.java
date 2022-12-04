package com.volvocars.tax.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.volvocars.tax.components.Vehicle;
import io.swagger.annotations.ApiModel;
import java.util.Date;

@ApiModel(description = "All details about the request")
public class TaxCalculatorRequest {

    private Vehicle vehicleType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Amsterdam")
    private Date[] dates;

    public Vehicle getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(Vehicle vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Date[] getDates() {
        return dates;
    }

    public void setDates(Date[] dates) {
        this.dates = dates;
    }


}
