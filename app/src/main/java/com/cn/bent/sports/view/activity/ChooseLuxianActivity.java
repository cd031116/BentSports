package com.cn.bent.sports.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by dawn on 2018/2/5.
 */

public class ChooseLuxianActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_choose;
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void initData() {
        super.initData();
    }

    @OnClick({R.id.shiyanhu, R.id.jinmao})
    void OnClick(View view) {
        switch (view.getId()) {
            case R.id.shiyanhu:
                break;
            case R.id.jinmao:
                break;
        }
    }
}
