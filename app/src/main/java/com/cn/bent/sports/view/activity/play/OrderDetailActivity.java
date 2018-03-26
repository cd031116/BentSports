package com.cn.bent.sports.view.activity.play;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.widget.MyScroview;
import com.kennyc.view.MultiStateView;

import butterknife.Bind;

public class OrderDetailActivity extends BaseActivity implements MyScroview.OnScrollListener {
    @Bind(R.id.myscroview)
    MyScroview myscroview;
    @Bind(R.id.search02)
    LinearLayout search02;// 在MyScrollView里面的购买布局
    @Bind(R.id.search01)
    LinearLayout search01;
    @Bind(R.id.tab_mian)
    LinearLayout tab_mian;
    @Bind(R.id.tab1_t)
    TextView tab1_t;
    @Bind(R.id.tab1_v)
    TextView tab1_v;
    @Bind(R.id.tab2_t)
    TextView tab2_t;
    @Bind(R.id.tab2_v)
    TextView tab2_v;
    @Bind(R.id.tab3_t)
    TextView tab3_t;
    @Bind(R.id.tab3_v)
    TextView tab3_v;
    @Bind(R.id.multiStateView)
    MultiStateView multiStateView;
    private int select = 1;
    private int topHeight;
    private int journeyHeight;
    private int payknowHeight;
    private String spot_team_id;
    private String deviceId;
    private String memberId;
    private String title;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_detail;
    }

    @Override
    public void initView() {
        super.initView();
        myscroview.setOnScrollListener(this);
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        changeview(select);
    }


    private void changeview(int index) {
        tab1_t.setTextColor(Color.parseColor("#333333"));
        tab2_t.setTextColor(Color.parseColor("#333333"));
        tab3_t.setTextColor(Color.parseColor("#333333"));
        tab1_v.setSelected(false);
        tab2_v.setSelected(false);
        tab3_v.setSelected(false);
        if (index == 1) {
            tab1_t.setTextColor(Color.parseColor("#e11818"));
            tab1_v.setSelected(true);
        } else if (index == 2) {
            tab2_t.setTextColor(Color.parseColor("#e11818"));
            tab2_v.setSelected(true);
        } else {
            tab3_t.setTextColor(Color.parseColor("#e11818"));
            tab3_v.setSelected(true);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            topHeight = search02.getBottom() - search02.getHeight();
            new Handler().postDelayed(new Runnable() {
                public void run() {
//                    journeyHeight = recycleview.getBottom() - 120;
//                    payknowHeight = payKnowLayout.getBottom() - 120;
                }

            }, 500);
        }
    }

    @Override
    public void onScroll(int scrollY) {
        if (scrollY >= topHeight) {
            if (tab_mian.getParent() != search01) {
                search02.removeView(tab_mian);
                search01.addView(tab_mian);
            }
            changeview(1);
            if (scrollY >= journeyHeight && scrollY <= payknowHeight) {
                changeview(2);
            }
            if (scrollY >= payknowHeight) {
                changeview(3);
            }
        } else {
            if (tab_mian.getParent() != search02) {
                search01.removeView(tab_mian);
                search02.addView(tab_mian);
            }

        }
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
    }
}
