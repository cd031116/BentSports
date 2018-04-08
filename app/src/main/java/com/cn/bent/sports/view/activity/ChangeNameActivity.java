package com.cn.bent.sports.view.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.bean.GameDetail;
import com.cn.bent.sports.bean.InfoEvent;
import com.cn.bent.sports.bean.LoginBase;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.view.activity.youle.bean.UserInfo;
import com.cn.bent.sports.view.activity.youle.play.OrderDetailActivity;
import com.kennyc.view.MultiStateView;
import com.vondear.rxtools.view.RxToast;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.HuiquRxTBFunction;
import com.zhl.network.huiqu.HuiquTBResult;
import com.zhl.network.huiqu.JavaRxFunction;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;

public class ChangeNameActivity extends BaseActivity {

    @Bind(R.id.name)
    EditText name_t;

    private UserInfo user;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_name;
    }

    @Override
    public void initView() {
        user = SaveObjectUtils.getInstance(ChangeNameActivity.this).getObject(Constants.USER_BASE, null);
        super.initView();
    }

    @Override
    public void initData() {
        super.initData();
        name_t.setText(user.getNickname()!=null?user.getNickname():"");
    }


    @OnClick({R.id.top_left, R.id.top_image, R.id.top_right_text})
    void conlick(View view) {
        switch (view.getId()) {
            case R.id.top_right_text:
                String nickmane = name_t.getText().toString();
                if (TextUtils.isEmpty(nickmane)) {
                    RxToast.warning("请输入昵称");
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
    private void login(final String nickmane) {
        showAlert("正在提交...", true);
        UserInfo mysuer=new UserInfo();
        mysuer.setId(user.getId());
        mysuer.setNickname(nickmane);
        BaseApi.getJavaLoginDefaultService(ChangeNameActivity.this).exchangeName(mysuer)
                .map(new HuiquRxTBFunction<Boolean>())
                .compose(RxSchedulers.<Boolean>io_main())
                .subscribe(new RxObserver<Boolean>(ChangeNameActivity.this, TAG, 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, Boolean bean) {
                        dismissAlert();
                        user.setNickname(nickmane);
                        SaveObjectUtils.getInstance(ChangeNameActivity.this).setObject(Constants.USER_BASE,user);
                        RxToast.success("修改成功");
                    }
                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        dismissAlert();

                    }
                });
    }


}
