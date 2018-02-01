package com.cn.bent.sports.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cn.bent.sports.R;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.bean.LoginBase;
import com.cn.bent.sports.ibeacon.ScanActivity;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.widget.ToastDialog;

import butterknife.Bind;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {
    @Bind(R.id.name_t)
    TextView name_t;
    @Bind(R.id.phone_t)
    TextView phone_t;
    @Bind(R.id.user_photo)
    ImageView user_photo;

    private LoginBase user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        user = SaveObjectUtils.getInstance(SettingActivity.this).getObject(Constants.USER_INFO, null);
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
    public void onResume() {
        super.onResume();
        if(TextUtils.isEmpty(user.getNickname())){
            name_t.setText("未设置");
            name_t.setTextColor(Color.parseColor("#999999"));
        }else {
            name_t.setText(user.getNickname());
            name_t.setTextColor(Color.parseColor("#333333"));
            phone_t.setText(user.getMobile());
            RequestOptions requestOptions = RequestOptions.circleCropTransform();
            Glide.with(SettingActivity.this).load(user.getHeadimg())
                    .apply(requestOptions)
                    .into(user_photo);
        }
    }

    @Override
    public void initData() {
        super.initData();
    }

    @OnClick({R.id.top_left, R.id.top_image, R.id.ni_ceng, R.id.login})
    void conlick(View view) {
        switch (view.getId()) {
            case R.id.ni_ceng:
                startActivity(new Intent(SettingActivity.this, ChangeNameActivity.class));
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
                    startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                } else {

                }
                dialog.dismiss();
            }
        }).setTitle("提示").show();
    }

}
