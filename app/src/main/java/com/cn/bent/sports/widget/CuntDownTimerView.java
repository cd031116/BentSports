package com.cn.bent.sports.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.bent.sports.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lyj on 2018/1/30 0030.
 * description
 */

public class CuntDownTimerView  extends LinearLayout {


    private TextView tv_hour_decade;
    private TextView tv_hour_unit;
    private TextView tv_min_decade;
    private TextView tv_min_unit;
    private TextView tv_sec_decade;
    private TextView tv_sec_unit;

    private Context context;
    private long hour_decade;
    private long hour_unit;
    private long min_decade;
    private long min_unit;
    private long sec_decade;
    private long sec_unit;
    private Timer timer;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            countDown();
        };
    };

    public CuntDownTimerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.cuntdown_item, this);
        tv_hour_decade = (TextView) view.findViewById(R.id.tv_hour_decade);
        tv_hour_unit = (TextView) view.findViewById(R.id.tv_hour_unit);
        tv_min_decade = (TextView) view.findViewById(R.id.tv_min_decade);
        tv_min_unit = (TextView) view.findViewById(R.id.tv_min_unit);
        tv_sec_decade = (TextView) view.findViewById(R.id.tv_sec_decade);
        tv_sec_unit = (TextView) view.findViewById(R.id.tv_sec_unit);

    }

    public void start() {
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(0);
                }
            }, 0, 1000);
        }
    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void setTime(long time) {
        long timed = time / 1000;
        long day = timed / (24 * 60 * 60);
        long hour = (timed % (24 * 60 * 60)) / (60 * 60);
        long min = ((timed % (24 * 60 * 60)) % (60 * 60)) / 60;
        long sec = ((timed % (24 * 60 * 60)) % (60 * 60)) % 60;

        if (hour >= 60 || min >= 60 || sec >= 60 || hour < 0 || min < 0
                || sec < 0) {
            // throw new
            // RuntimeException("Time format is error,please check out your code");
            return;
        }

        hour_decade = hour / 10;
        hour_unit = hour - hour_decade * 10;

        min_decade = min / 10;
        min_unit = min - min_decade * 10;

        sec_decade = sec / 10;
        sec_unit = sec - sec_decade * 10;
        tv_hour_decade.setText(hour_decade + "");
        tv_hour_unit.setText(hour_unit + "");
        tv_min_decade.setText(min_decade + "");
        tv_min_unit.setText(min_unit + "");
        tv_sec_decade.setText(sec_decade + "");
        tv_sec_unit.setText(sec_unit + "");
    }

    /**
     *
     * @Description: @param
     * @return boolean
     * @throws
     */
    private void countDown() {
        tv_hour_unit.setVisibility(View.VISIBLE);
        tv_min_unit.setVisibility(View.VISIBLE);
        tv_sec_unit.setVisibility(View.VISIBLE);
        if (isCarry4Unit(tv_sec_unit)) {
            if (isCarry4Decade(tv_sec_decade)) {
                if (isCarry4Unit(tv_min_unit)) {
                    if (isCarry4Decade(tv_min_decade)) {
                        if (isCarry4Unit(tv_hour_unit)) {
                            if (isCarry4Decade(tv_hour_decade)){
                                stop();
//                                NotificationCenter.defaultCenter().publish(new CountTimeEvent());
//                                tv_hour_decade.setText("时");
//                                tv_min_decade.setText("间");
//                                tv_sec_decade.setText("到");
                                tv_hour_unit.setVisibility(View.GONE);
                                tv_min_unit.setVisibility(View.GONE);
                                tv_sec_unit.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isCarry4Decade(TextView tv) {
        int time = Integer.valueOf(tv.getText().toString());
        time = time - 1;
        if (time < 0) {
            time = 5;
            tv.setText(time + "");
            return true;
        } else {
            tv.setText(time + "");
            return false;
        }

    }

    private boolean isCarry4Unit(TextView tv) {
        int time = Integer.valueOf(tv.getText().toString());
        time = time - 1;
        if (time < 0) {
            time = 9;
            tv.setText(time + "");
            return true;
        } else {
            tv.setText(time + "");
            return false;
        }

    }




}
