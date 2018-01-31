package com.cn.bent.sports.utils;

import android.os.Environment;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
            date.setDate(date.getDate());
            date.setHours(0);
            date.setMinutes(0);
            date.setSeconds(0);
            DateFormat format = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
            String dateto = format.format(date);
            return getStringToDate(dateto);
        }
     //将字符串转为时间戳
    public static long getStringToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
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
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        return format.format(date);
    }

    public static void writeToLocal(InputStream input)
            throws IOException {
        String path= Environment.getExternalStorageDirectory()+"paly/asset";
        int index;
        byte[] bytes = new byte[1024];
        FileOutputStream downloadFile = new FileOutputStream(path);
        while ((index = input.read(bytes)) != -1) {
            downloadFile.write(bytes, 0, index);
            downloadFile.flush();
        }
        downloadFile.close();
        input.close();
    }
}
