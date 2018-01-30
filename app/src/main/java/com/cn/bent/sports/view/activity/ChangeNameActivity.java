package com.cn.bent.sports.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseActivity;

import butterknife.OnClick;

public class ChangeNameActivity extends BaseActivity {



    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_name;
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void initData() {
        super.initData();
    }


    @OnClick({R.id.top_left,R.id.top_image,R.id.top_right_text})
    void conlick(View view){
        switch (view.getId()){
            case R.id.top_right_text:

                break;
            case R.id.top_image:
            case R.id.top_left:
                ChangeNameActivity.this.finish();
                break;
        }
    }
}
