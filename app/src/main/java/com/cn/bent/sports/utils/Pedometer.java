package com.cn.bent.sports.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by lyj on 2018/3/7 0007.
 * description
 */

public class Pedometer implements SensorEventListener {
    private static Pedometer instance;
    private SensorManager mSensorManager;
    private Sensor mStepCount;
    private Sensor mStepDetector;
    private float mCount;//步行总数
    private float mDetector;//步行探测器
    private Context context;
    private static final int sensorTypeD=Sensor.TYPE_STEP_DETECTOR;
    private static final int sensorTypeC=Sensor.TYPE_STEP_COUNTER;

    public Pedometer() {

    }
    public static Pedometer getInstance(Context context) {
        if (instance == null) {
            instance = new Pedometer(context);
        }
        return instance;
    }

    public Pedometer(Context context) {
        this.context = context;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mStepCount = mSensorManager.getDefaultSensor(sensorTypeC);
        mStepDetector = mSensorManager.getDefaultSensor(sensorTypeD);
    }

    public void register(){
        register(mStepCount, SensorManager.SENSOR_DELAY_FASTEST);
        register(mStepDetector, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void unRegister(){
        mSensorManager.unregisterListener(this);
    }

    private void register(Sensor sensor,int rateUs) {
        mSensorManager.registerListener(this, sensor, rateUs);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType()==sensorTypeC) {
            setStepCount(event.values[0]);
        }
        if (event.sensor.getType()==sensorTypeD) {
            if (event.values[0]==1.0) {
                mDetector++;
            }
        }
    }

    public float getStepCount() {
        return mCount;
    }

    private void setStepCount(float count) {
        this.mCount = count;
    }

    public float getmDetector() {
        return mDetector;
    }
}
