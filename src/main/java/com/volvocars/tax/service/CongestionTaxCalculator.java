package com.volvocars.tax.service;

import com.volvocars.tax.components.*;
import com.volvocars.tax.exceptions.NotSupportedTaxYearException;
import com.volvocars.tax.utils.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;


@Service
public class CongestionTaxCalculator {

    @Value("${maxcharge.time}")
    private int maxChargeTime;

    @Autowired
    TaxPrice taxPrice;

    @Autowired
    Holidays holidays;

    @Autowired
    TaxExempt taxExempt;

    private static final int YEAR_2013 = 2013;

    public int getTax(Vehicle vehicle, Date[] dates) {
        int totalFee = 0;
        int tollFee = 0;
        int maxTollFee = 0;

        List<Date> datesList = Arrays.asList(dates);
        DateTimeUtil.sortDateByAsc(datesList);

        // Tax calc for single entry, directly calculate tax and no need to check exceed 60 mins
        if(datesList.size() == 1) {
            isSupportedYear(dates[0]);
            tollFee = getTollFee(dates[0], vehicle.name());
            return tollFee;
        }
        for (int i = 0; i < datesList.size(); i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dates[i]);

            isSupportedYear(dates[i]);

            calendar.add(Calendar.HOUR_OF_DAY, 1);
            Date oneHourAhead = calendar.getTime();
            int tollFeeFirst = 0;
            int tollFeeSecond = 0;

            // Check first entry - special case since it doesn't have previous entry
            if(i== 0) {
                if (hasExceededMaxChargeTime(dates[i], dates[i + 1])) {
                    tollFee = getTollFee(dates[i], vehicle.name());
                } else {
                    tollFeeFirst = getTollFee(dates[i], vehicle.name());
                    tollFeeSecond = getTollFee(dates[i + 1], vehicle.name());
                    maxTollFee = Math.max(tollFeeFirst, tollFeeSecond);
                }

            } else {
                if(!(hasExceededMaxChargeTime(dates[i - 1], dates[i])) &&  dates[i-1].before(oneHourAhead)) {
                    tollFeeFirst = getTollFee(dates[i -1], vehicle.name());
                    tollFeeSecond = getTollFee(dates[i], vehicle.name());
                    maxTollFee = Math.max(tollFeeFirst, tollFeeSecond);
                } else {
                    tollFee = getTollFee(dates[i], vehicle.name());
                }
            }
            totalFee += tollFee;

        }

        totalFee += maxTollFee;

        if (totalFee > maxChargeTime)
            totalFee = maxChargeTime;
        return totalFee;
    }

    private boolean hasExceededMaxChargeTime(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.MILLISECONDS.toMinutes(diff) > maxChargeTime;
    }

    private boolean isTollFreeVehicle(String vehicleType) {
        if (vehicleType == null)
            return false;
        return taxExempt.getVehicles().contains(vehicleType);
    }

    public int getTollFee(Date date, String vehicle) {
        if (isTollFreeDate(date) || isTollFreeVehicle(vehicle))
            return 0;
        String hour = date.getHours() + ":" + date.getMinutes();
        return taxPrice.getTimings().stream().filter(obj -> obj.isTimeInBetween(hour))
                .findFirst()
                .orElse(new TimeBasedPricing()).getPrice();
    }

    private Boolean isTollFreeDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        if (day == Calendar.SATURDAY || day == Calendar.SUNDAY)
            return true;

        for (TripYear tripYear : holidays.getYears()) {
            if (tripYear.getYear() == year) {
                for (TripMonth tripMonth : tripYear.getMonths()) {
                    if (tripMonth.getMonth() == month)
                        if (tripMonth.getDates().isEmpty())
                            return true;
                        else if (tripMonth.getDates().contains(dayOfMonth) || tripMonth.getDates().contains(dayOfMonth + 1))
                            return true;
                }
            }
        }
        return false;
    }

    private Boolean isSupportedYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (calendar.get(Calendar.YEAR) != YEAR_2013) {
            throw new NotSupportedTaxYearException();
        }
        return true;
    }
}
