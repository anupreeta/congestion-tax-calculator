package com.volvocars.tax.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class DateTimeUtil {
    public static void sortDateByAsc(List<Date> dates) {
        Collections.sort(dates, new Comparator<Date>() {
            @Override
            public int compare(Date object1, Date object2) {
                return object1.compareTo(object2);
            }
        });
    }

}
