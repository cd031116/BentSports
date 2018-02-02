package com.cn.bent.sports.scan;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseActivity;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.Bind;
import butterknife.OnClick;


/*
* create 2017-7-29    lyj
* 二维码扫描
* */


public class CaptureActivity extends BaseActivity {
    @Bind(R.id.top_left)
    LinearLayout top_left;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_qr_scan;
    }

    @Override
    public void initView() {
        super.initView();
        /**
         * 执行扫面Fragment的初始化操作
         */
        CaptureFragment captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
//        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera);

        captureFragment.setAnalyzeCallback(analyzeCallback);
        /**
         * 替换我们的扫描控件
         */ getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();
    }

    @Override
    public void initData() {
        super.initData();
    }




    @SuppressWarnings("deprecation")
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
           setResult(RESULT_OK);
        }

        @Override
        public void onAnalyzeFailed() {
        }
    };



    @OnClick({R.id.top_left})
    void onclick(View v) {
        switch (v.getId()) {
            case R.id.top_left:
                finish();
                break;
        }
    }


}