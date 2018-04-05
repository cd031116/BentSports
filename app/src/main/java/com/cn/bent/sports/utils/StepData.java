package com.cn.bent.sports.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.cn.bent.sports.base.BaseConfig;

/**
 * Created by Administrator on 2018/4/5/005.
 */

public class StepData{
    private static StepData cm = null;
    private SharedPreferences sharedPreferences;
    private Editor editor;
    private String key="step_data";
    public static StepData getInstance(Context context) {
        if (cm == null) {
            cm = new StepData(context);
        }
        return cm;
    }

    public StepData(Context context) {
        String packageName = context.getApplicationInfo().packageName;
        sharedPreferences = context.getSharedPreferences(
                String.format("sd_%s", packageName), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }


    public void setStepDataValue(long value) {
        if (value == 0) {
            editor.remove(key);
        } else {
            editor.putLong(key, value);
        }
        editor.commit();
    }

    public long getStepDataValue(long def) {
        return sharedPreferences.getLong(key, def);
    }
    //是否大于 timeSpace 分钟
    public boolean isThanNow(long timeSpace){
        long nowTime=System.currentTimeMillis();
        if(getStepDataValue(0)<=0){
            return true;
        }
        if((nowTime-getStepDataValue(0))>(timeSpace*60*1000)){
            return true;
        }
        return false;
    }





}
