package com.example.melaAttendance.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateFactory {
    public static DateFactory dateFactory;
    private Locale locale;

    private DateFactory() {
        locale = Locale.US; // set locale
    }

    public static DateFactory getInstance() {
        if (dateFactory == null) {
            dateFactory = new DateFactory();
        }
        return dateFactory;
    }
    public String getDateTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = df.format(Calendar.getInstance().getTime());

        return date;
    }
    public String getTodayDate()
    {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
       // String dateoftodayis = day + "-" + (month + 1) + "-" + year;
        String dateoftodayis = year + "-" + (month + 1) + "-" + day;
        return dateoftodayis;
    }
    public String getDateInOtherFormate()
    {
        //SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        String dateoftodayis = year + "-" + (month + 1) + "-" + day;
        return dateoftodayis;


    }

    public String getCurrentTime(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
        return sdf.format(Calendar.getInstance().getTime());

    }

    public String getPreviousDate(String dateString, int numOfDays, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
        Date myDate = null;
        try {
            myDate = sdf.parse(dateString);
        }
        catch (ParseException e) {

        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(myDate);
        calendar.add(Calendar.DAY_OF_YEAR, -numOfDays);
        Date previousDate = calendar.getTime();
        return sdf.format(previousDate);
    }

    public String getNextDate(String dateString, int noOfDays, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
        Date myDate = null;
        try {
            myDate = sdf.parse(dateString);
        }
        catch (ParseException e) {

        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(myDate);
        calendar.add(Calendar.DAY_OF_YEAR, +noOfDays);
        Date previousDate = calendar.getTime();
        return sdf.format(previousDate);
    }

    public long getStartTimeOfTodayInMillis() {
        Calendar today = Calendar.getInstance();
        today.setTimeZone(TimeZone.getTimeZone("UTC"));
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        return today.getTimeInMillis();
    }

    public long getStartTimeOfGivenDay(long millis) {
        Calendar day = Calendar.getInstance();
        day.setTimeInMillis(millis);
        day.set(Calendar.HOUR_OF_DAY, 0);
        day.set(Calendar.MINUTE, 0);
        day.set(Calendar.SECOND, 0);
        day.set(Calendar.MILLISECOND, 0);

        return day.getTimeInMillis();
    }

    public long getEndTimeOfTodayInMillis() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTimeInMillis();
    }

    public long getEndTimeOfGivenDay(long millis) {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.setTimeInMillis(millis);
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTimeInMillis();
    }
}
