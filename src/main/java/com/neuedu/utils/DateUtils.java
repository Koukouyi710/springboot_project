package com.neuedu.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class DateUtils {

    public static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * Date-->string
     */
    public static String dateToString(Date date,String formate){

        DateTime dateTime = new DateTime(date);
        return dateTime.toString(formate);
    }
    public static String dateToString(Date date){

        DateTime dateTime = new DateTime(date);
        return dateTime.toString(STANDARD_FORMAT);
    }

    /**
     * string-->Date
     */
    public static Date stringToDate(String string){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_FORMAT);
        DateTime dateTime = dateTimeFormatter.parseDateTime(string);
        return dateTime.toDate();
    }
    public static Date stringToDate(String string,String formate){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formate);
        DateTime dateTime = dateTimeFormatter.parseDateTime(string);
        return dateTime.toDate();
    }

}
