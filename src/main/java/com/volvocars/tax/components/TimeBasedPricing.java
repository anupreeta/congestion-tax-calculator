package com.volvocars.tax.components;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
@Getter
@Setter
public class TimeBasedPricing {
    private String startTime;
    private String endTime;
    private int price;

    final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

    public boolean isTimeInBetween(String travelTime) {
        try {
            Date start = this.dateFormat.parse(this.startTime);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(start);
            calendar1.add(Calendar.DATE, 1);

            Date end = this.dateFormat.parse(this.endTime);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(end);
            calendar2.add(Calendar.DATE, 1);

            Date d = this.dateFormat.parse(travelTime);
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(d);
            calendar3.add(Calendar.DATE, 1);

            Date x = calendar3.getTime();
            if ((x.after(calendar1.getTime()) || x.equals(calendar1.getTime())) && (x.before(calendar2.getTime())) || x.equals(calendar2.getTime())) {
                return true;
            }
        } catch(ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

}
