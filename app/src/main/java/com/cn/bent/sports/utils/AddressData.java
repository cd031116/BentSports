package com.cn.bent.sports.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;

/**
*aunthor lyj
* create 2018/4/6/006 15:16   定位点  10m上传一次
**/

public class AddressData {

    private static AddressData cm = null;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String key="location_data";

    private String key_mLatitude="mLatitude_data";
    private String key_mLongitude="mLongitude_data";
    public static AddressData getInstance(Context context) {
        if (cm == null) {
            cm = new AddressData(context);
        }
        return cm;
    }

    public AddressData(Context context) {
        String packageName = context.getApplicationInfo().packageName;
        sharedPreferences = context.getSharedPreferences(
                String.format("sd_%s", packageName), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }


    public void setStepDataValue(double mLatitude,double mLongitude) {
        if (mLatitude == 0||mLongitude==0) {
            editor.remove(key_mLatitude);
            editor.remove(key_mLongitude);
        } else {
            editor.putString(key_mLatitude, String.valueOf(mLatitude));
            editor.putString(key_mLongitude, String.valueOf(mLongitude));
        }
        editor.commit();
    }



    public float getStepDataValue(LatLng nowPosition) {
        String mlatitude=sharedPreferences.getString(key_mLatitude, "0");
        String mlongitude= sharedPreferences.getString(key_mLongitude, "0");
        if(Double.parseDouble(mlatitude)<=0||Double.parseDouble(mlongitude)<=0){
          return   120;
        }
        LatLng  startLatlng = new LatLng(Double.parseDouble(mlatitude),Double.parseDouble(mlongitude));
        float lineDistance = AMapUtils.calculateLineDistance(startLatlng,nowPosition);
        return lineDistance;
    }
    //是否大于 10m
    public boolean isThan10(LatLng nowPosition){
        if(getStepDataValue(nowPosition)>=10){
            return true;
        }
        return false;
    }

    public void removeValue() {
        editor.remove(key_mLatitude);
        editor.remove(key_mLongitude);
        editor.commit();
    }

}
