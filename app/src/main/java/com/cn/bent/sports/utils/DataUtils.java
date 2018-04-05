package com.cn.bent.sports.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.cn.bent.sports.bean.GamePotins;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by lyj on 2018/1/31 0031.
 * description
 */

public class DataUtils {

    //获取第二天零点时间搓
    public static long getlongs() {
        Date date = new Date();
        date.setDate(date.getDate() + 1);
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
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static void compareDaXiao(List<GamePotins> history) {
        Collections.sort(history, new Comparator<GamePotins>() {
            @Override
            public int compare(GamePotins user1, GamePotins user2) {
                Integer id1 = user1.getOrderNo();
                Integer id2 = user2.getOrderNo();
                return id1.compareTo(id2);
            }
        });
    }

    //时间戳转换成字符窜
    public static String getDateToString(long milSecond) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        return format.format(date);
    }


    public static String getDateToTime(long milSecond) {
        long secoond = milSecond / 1000;
        if (secoond >= 2 * 60 * 60) {
            return "02.00.00";
        } else {
            long hour = (secoond % (24 * 60 * 60)) / (60 * 60);
            long min = ((secoond % (24 * 60 * 60)) % (60 * 60)) / 60;
            long sec = ((secoond % (24 * 60 * 60)) % (60 * 60)) % 60;
            return (hour >= 10 ? (hour + "") : ("0" + hour)) + ":" + (min >= 10 ? (min + "") : ("0" + min)) + ":" + (sec >= 10 ? (sec + "") : ("0" + sec));
        }
    }


    public static void copyFilesFassets(Context context, String oldPath, String newPath) {
        try {
            String fileNames[] = context.getAssets().list(oldPath);//获取assets目录下的所有文件及目录名
            if (fileNames.length > 0) {//如果是目录
                File file = new File(newPath);
                file.mkdirs();//如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                    copyFilesFassets(context, oldPath + "/" + fileName, newPath + "/" + fileName);
                }
            } else {//如果是文件
                InputStream is = context.getAssets().open(oldPath);
                FileOutputStream fos = new FileOutputStream(new File(newPath));
                Log.i("tttt", "fos=" + fos);
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = is.read(buffer)) != -1) {//循环从输入流读取 buffer字节
                    fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
                }
                fos.flush();//刷新缓冲区
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //如果捕捉到错误则通知UI线程
        }
    }

    public static void copyAssetsToDst(Context context, String srcPath, String dstPath) {
        try {
            String fileNames[] = context.getAssets().list(srcPath);
            if (fileNames.length > 0) {
                File file = new File(context.getFilesDir(), dstPath);
                if (!file.exists()) file.mkdirs();
                for (String fileName : fileNames) {
                    if (!srcPath.equals("")) { // assets 文件夹下的目录
                        copyAssetsToDst(context, srcPath + File.separator + fileName, dstPath + File.separator + fileName);
                    } else { // assets 文件夹
                        copyAssetsToDst(context, fileName, dstPath + File.separator + fileName);
                    }
                }
            } else {
                File outFile = new File(context.getFilesDir(), dstPath);
                outFile.getParentFile().mkdirs();

                InputStream is = context.getAssets().open(srcPath);
                FileOutputStream fos = new FileOutputStream(outFile);
                byte[] buffer = new byte[1024];
                int byteCount;
                while ((byteCount = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, byteCount);
                }
                fos.flush();
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static BluetoothAdapter getDefaultAdapter(Context context) {
        BluetoothAdapter adapter = null;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            adapter = BluetoothAdapter.getDefaultAdapter();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            final BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            adapter = bluetoothManager.getAdapter();
        }
        return adapter;
    }

    public static boolean isBlue(Context context) {
        if (getDefaultAdapter(context).isEnabled()) {
            return true;
        } else {
            return false;//打开蓝牙
        }
    }
}
