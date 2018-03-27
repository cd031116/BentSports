package com.cn.bent.sports.view.activity.youle.play;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.view.activity.youle.PlayMultActivity;

import butterknife.OnClick;

/**
 * aunthor lyj
 * create 2018/3/27/027 15:41   准备页面  根据队员和队长区分界面
 **/
public class PrepareActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_prepare;
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void initData() {
        super.initData();
    }

    @OnClick({R.id.start_t})
    void onclick(View v) {
        switch (v.getId()) {
            case R.id.start_t:
                startActivity(new Intent(PrepareActivity.this, PlayMultActivity.class));
                break;
        }
    }
}
