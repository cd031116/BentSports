package com.cn.bent.sports.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.cn.bent.sports.MainActivity;
import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.bean.InfoEvent;
import com.cn.bent.sports.bean.LoginBase;
import com.cn.bent.sports.bean.ReFreshEvent;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.utils.ToastUtils;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.HuiquRxFunction;
import com.zhl.network.huiqu.HuiquRxTBFunction;
import com.zhl.network.huiqu.HuiquTBResult;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;

public class ChangeNameActivity extends BaseActivity {

    @Bind(R.id.name)
    EditText name_t;

    private LoginBase user;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_name;
    }

    @Override
    public void initView() {
        user = SaveObjectUtils.getInstance(ChangeNameActivity.this).getObject(Constants.USER_INFO, null);
        super.initView();
    }

    @Override
    public void initData() {
        super.initData();
        name_t.setText(user.getNickname());
    }


    @OnClick({R.id.top_left, R.id.top_image, R.id.top_right_text})
    void conlick(View view) {
        switch (view.getId()) {
            case R.id.top_right_text:
                String nickmane = name_t.getText().toString();
                if (TextUtils.isEmpty(nickmane)) {
                    ToastUtils.showShortToast(ChangeNameActivity.this, "请输入昵称");
                    return;
                }
                login(nickmane);
                break;
            case R.id.top_image:
            case R.id.top_left:
                ChangeNameActivity.this.finish();
                break;
        }
    }

    private void login(final String nickname) {
        showAlert("正在提交...", true);
        BaseApi.getDefaultService(this).modifyUserMsg(user.getMember_id(), "1", nickname)
                .map(new HuiquRxTBFunction<HuiquTBResult>())
                .compose(RxSchedulers.<HuiquTBResult>io_main())
                .subscribe(new RxObserver<HuiquTBResult>(ChangeNameActivity.this, "changeName", 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, HuiquTBResult info) {
                        dismissAlert();
                        user.setNickname(nickname);
                        SaveObjectUtils.getInstance(ChangeNameActivity.this).setObject(Constants.USER_INFO, user);
                        ToastUtils.showLongToast(ChangeNameActivity.this, info.getMsg());
                        EventBus.getDefault().post(new InfoEvent());
                        finish();
                    }

                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        dismissAlert();
                        ToastUtils.showLongToast(ChangeNameActivity.this, e.getMessage());
                    }
                });
    }

}
