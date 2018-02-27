package com.cn.bent.sports.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.widget.BaseDragZoomImageView;


import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by dawn on 2018/1/31.
 */

public class ZoomActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        //隐藏状态栏
        //定义全屏参数
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.zoom_layout;
    }

    @Override
    public void initView() {
        super.initView();
    }


    @Override
    public void initData() {
        super.initData();
    }

    @OnClick({R.id.img_sy,R.id.back})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.img_sy:
//                startActivity(new Intent(this, ImgActivity.class));
//                startActivity(new Intent(this, ArActivity.class));
                startActivity(new Intent(this, OfflineActivity.class));
                break;
            case R.id.back:
                finish();
                break;
        }
    }
}
