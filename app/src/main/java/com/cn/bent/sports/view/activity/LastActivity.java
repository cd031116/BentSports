package com.cn.bent.sports.view.activity;

import android.content.Intent;
import android.view.View;

import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseActivity;

import butterknife.OnClick;

/**
 * Created by dawn on 2018/2/26.
 */

public class LastActivity extends BaseActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.last_finish_layout;
    }

    @Override
    public void initView() {
        super.initView();

    }

    @Override
    public void initData() {
        super.initData();
    }

    @OnClick(R.id.go_ahead)
    void onClick(View view) {
        startActivity(new Intent(this, AllFinishActivity.class));
    }

}
