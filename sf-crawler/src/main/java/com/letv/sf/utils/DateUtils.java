package com.letv.sf.utils;

import weibo4j.model.WeiboException;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yangyong3 on 2016/12/12.
 */
public class DateUtils {

    public static Date parseDate(String dateString, String format) throws ParseException {
        DateFormat df = new SimpleDateFormat(format);
        return df.parse(dateString);
    }

    public static String dateString(Date date,String format){
        DateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }



    public static void main(String[] args) throws WeiboException, ParseException {
       //System.out.println(DateUtils.parseDate("07:46","yyyy-MM-dd HH:mm"));
      //2016-12-14 07:46
        System.out.println(DateUtils.parseDate("1-2","MM-dd"));
    }
}
