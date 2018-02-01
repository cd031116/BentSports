package com.cn.bent.sports.view.activity;

import android.os.Bundle;
import android.view.View;

import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseActivity;

import butterknife.OnClick;

public class RuleActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_rule;
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
    void onclik(View v){
        switch (v.getId()){
            case R.id.back:
                RuleActivity.this.finish();
                break;
        }
    }
}
