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
