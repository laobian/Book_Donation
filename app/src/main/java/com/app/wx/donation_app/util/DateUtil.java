package com.app.wx.donation_app.util;


import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * @Author:wx
 * @Date:2019/3/11
 */
public class DateUtil {

    /*将形如“2012-10-30”的日期型字符串转换成date类型*/
    public static Date stringToDate(String date) throws ParseException {
        Date result = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        return result;
    }

    /**
     *
     * @param date
     * @return:返回形如"yyyy-MM-dd"的日期字符串
     * @throws ParseException
     */
    public static String date2String(Date date) throws ParseException {
        String result = new SimpleDateFormat("yyyy-MM-dd").format(date);
        return result;
    }
}
