package com.app.wx.donation_app.util;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Random;

/**
 * Author: wx
 * Date: 2019/3/16 8:55
 */
public class KeyUtil {
    public static synchronized String genUniqueKey() {
        Random random = new Random();
        Integer number = random.nextInt(900000) + 100000;

        return System.currentTimeMillis() + String.valueOf(number);
    }

    public static synchronized String orderId() {
        Random random = new Random();
        Integer number = random.nextInt(900000) + 100000;
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmssms");
        String currentDate = sdf.format(date);
        return currentDate+number;
    }
}
