package com.assured.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

//final -> We do not want any class to extend this class
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class DateUtils {

    private DateUtils() {
        super();
    }

    /**
     * @return lấy ra ngày tháng năm hiện tại của máy theo cấu trúc mặc định
     */
    public static String getCurrentDate() {
        Date date = new Date();
        return date.toString().replace(":", "_").replace(" ", "_");
    }

    /**
     * @return lấy ra ngày tháng năm và giờ phút giây hiện tại của máy theo cấu trúc dd/MM/yyyy HH:mm:ss
     */
    public static String getCurrentDateTime() {
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return formatter.format(now);
    }

    public static String getCurrentDateTimeCustom(String separator_Character) {
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String timeStamp = formatter.format(now).replace("/", separator_Character);
        timeStamp = timeStamp.replace(" ", separator_Character);
        timeStamp = timeStamp.replace(":", separator_Character);
        return timeStamp;
    }

    /**
     * @return lấy ra ngày tháng năm 10 ngày trước ngày hiện tại theo cấu trúc dd/MM/yyyy HH:mm:ss
     */
    public static String getTenDaysAgo() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -10);  // Subtract 10 days
        Date tenDaysAgo = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return formatter.format(tenDaysAgo);
    }

    /**
     * @return lấy ra ngày tháng năm 10 ngày sau ngày hiện tại theo cấu trúc dd/MM/yyyy HH:mm:ss
     */
    public static String getTenDaysFromNow() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 10);  // Add 10 days
        Date tenDaysFromNow = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return formatter.format(tenDaysFromNow);
    }
}
