package com.cn.bent.sports.view.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.cn.bent.sports.R;
import com.cn.bent.sports.api.BaseApi;
import com.cn.bent.sports.base.BaseActivity;
import com.cn.bent.sports.bean.AddScoreEntity;
import com.cn.bent.sports.bean.AllFinishEntity;
import com.cn.bent.sports.bean.GameEntity;
import com.cn.bent.sports.bean.InfoEvent;
import com.cn.bent.sports.bean.LoginBase;
import com.cn.bent.sports.bean.ReFreshEvent;
import com.cn.bent.sports.database.TaskCationManager;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.DataUtils;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.utils.ToastUtils;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.HuiquRxTBFunction;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by dawn on 2018/2/26.
 */

public class LastActivity extends BaseActivity {

    private LoginBase user;

    private int MAX_REQUEST = 2;
    private int isRequestNum = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.last_finish_layout;
    }

    @Override
    public void initView() {
        super.initView();
        user = (LoginBase) SaveObjectUtils.getInstance(this).getObject(Constants.USER_INFO, null);
    }

    @Override
    public void initData() {
        super.initData();
    }

    @OnClick(R.id.go_ahead)
    void onClick(View view) {
        commitScore();
    }

    private void commitScore() {
        BaseApi.getDefaultService(this).addScore(user.getMember_id(), 100, 8)
                .map(new HuiquRxTBFunction<AddScoreEntity>())
                .compose(RxSchedulers.<AddScoreEntity>io_main())
                .subscribe(new RxObserver<AddScoreEntity>(this, "addScore", 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, AddScoreEntity addScoreEntity) {
                        if (addScoreEntity.getBody().getAddStatus() == 1) {
                            dismissAlert();
                            LoginBase user = (LoginBase) SaveObjectUtils.getInstance(LastActivity.this).getObject(Constants.USER_INFO, null);
                            setScore(user);
                            EventBus.getDefault().post(new InfoEvent());
                            EventBus.getDefault().post(new ReFreshEvent());
                            startActivity(new Intent(LastActivity.this, AllFinishEntity.class));
                        } else {
                            ToastUtils.showShortToast(LastActivity.this, addScoreEntity.getMsg());
                            dismissAlert();
                        }
                        isRequestNum = 1;
                    }

                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        dismissAlert();
                        if (isRequestNum < MAX_REQUEST) {
                            isRequestNum++;
                            ToastUtils.showShortToast(LastActivity.this, "积分上传失败,正在重新上传积分");
                            commitScore();
                        } else {
                            ToastUtils.showShortToast(LastActivity.this, "网络异常，积分上传失败，请重新玩此游戏");
                            startActivity(new Intent(LastActivity.this, AllFinishEntity.class));
                        }
                    }
                });
    }

    private void setScore(LoginBase user) {
        if (user.getScore() != null)
            user.setScore(String.valueOf(Integer.parseInt(user.getScore()) + 100));
        else
            user.setScore(String.valueOf(100));
        SaveObjectUtils.getInstance(LastActivity.this).setObject(Constants.USER_INFO, user);
    }
}
