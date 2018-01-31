package com.cn.bent.sports.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by lyj on 2018/1/31 0031.
 * description
 */

public class DataUtils {

        //获取第二天零点时间搓
        public static long getlongs(){
            Date date = new Date();
            date.setDate(date.getDate()+1);
            date.setHours(0);
            date.setMinutes(0);
            date.setSeconds(0);
            DateFormat format = new SimpleDateFormat("yyyyMMdd hh:mm:ss");
            String dateto = format.format(date);
            return getStringToDate(dateto);
        }
     //将字符串转为时间戳
    public static long getStringToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd hh:mm:ss");
        Date date = new Date();
        try{
            date = dateFormat.parse(dateString);
        } catch(ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }

//时间戳转换成字符窜
    public static String getDateToString(long milSecond) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd hh:mm:ss");
        return format.format(date);
    }
}
