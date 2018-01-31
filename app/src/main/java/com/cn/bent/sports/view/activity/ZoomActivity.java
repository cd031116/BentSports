package com.cn.bent.sports.view.activity;

import android.os.Bundle;

import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.widget.MyZoomImageView;

import butterknife.Bind;

/**
 * Created by dawn on 2018/1/31.
 */

public class ZoomActivity extends BaseActivity {
//    @Bind(R.id.img_syh)
//    MyZoomImageView img_syh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zoom_layout);
        MyZoomImageView img_syh=new MyZoomImageView(this);
        img_syh.setBackground(getResources().getDrawable(R.drawable.syh_map));
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void initData() {
        super.initData();
    }
}
