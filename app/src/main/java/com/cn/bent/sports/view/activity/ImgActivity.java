package com.cn.bent.sports.view.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.widget.MyZoomImageView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by dawn on 2018/2/1.
 */

public class ImgActivity extends BaseActivity {

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
        return R.layout.img_layout;
    }

    @Override
    public void initView() {
        super.initView();
    }


    @Override
    public void initData() {
        super.initData();
    }

    @OnClick(R.id.back)
    void onClick(View view) {
        finish();
    }
}
