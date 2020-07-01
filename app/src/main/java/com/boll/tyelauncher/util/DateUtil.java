package com.boll.tyelauncher.util;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static String getDateFromMillis(StringBuffer selectedBirthday, String millis) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
            long m = Long.parseLong(millis);
            Calendar c = Calendar.getInstance();
            c.setTime(new Date(m));
            selectedBirthday.delete(0, selectedBirthday.length());
            selectedBirthday.append(c.get(1) + "-" + (c.get(2) + 1) + "-" + c.get(5));
            return sdf.format(new Date(m));
        } catch (Exception e) {
            return "";
        }
    }
}
