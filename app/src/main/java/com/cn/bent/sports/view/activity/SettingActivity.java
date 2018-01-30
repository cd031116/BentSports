package com.cn.bent.sports.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.ibeacon.ScanActivity;
import com.cn.bent.sports.widget.ToastDialog;

import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void initData() {
        super.initData();
    }
    @OnClick({R.id.top_left,R.id.top_image,R.id.ni_ceng,R.id.login})
    void conlick(View view){
        switch (view.getId()){
            case R.id.ni_ceng:
                startActivity(new Intent(SettingActivity.this,ChangeNameActivity.class));
                break;
            case R.id.top_image:
            case R.id.top_left:
                SettingActivity.this.finish();
                break;
            case R.id.login:
                showDialogMsg("确定退出当前账号？");
                break;
        }
    }


    private void showDialogMsg(String names) {
        new ToastDialog(this, R.style.dialog, names, new ToastDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {
                if (confirm) {
                    startActivity(new Intent(SettingActivity.this,LoginActivity.class));
                } else {

                }
                dialog.dismiss();
            }
        }).setTitle("提示").show();
    }

}
