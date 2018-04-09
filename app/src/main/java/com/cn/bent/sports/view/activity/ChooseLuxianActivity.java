package com.cn.bent.sports.view.activity;

import android.os.Bundle;
import android.view.View;

import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.base.BaseConfig;
import com.cn.bent.sports.utils.Constants;

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
        BaseConfig bg = BaseConfig.getInstance(ChooseLuxianActivity.this);
        switch (view.getId()) {
            case R.id.shiyanhu:
              bg.setIntValue(Constants.LU_XIAN, 1);
                break;
            case R.id.jinmao:
                bg.setIntValue(Constants.LU_XIAN, 2);
                break;
        }
    }



}
