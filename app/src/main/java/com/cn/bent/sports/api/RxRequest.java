package com.cn.bent.sports.api;

import android.content.Context;
import android.content.Intent;

import com.cn.bent.sports.base.MyApplication;
import com.cn.bent.sports.bean.LoginResult;
import com.cn.bent.sports.bean.TeamGame;
import com.cn.bent.sports.utils.Constants;
import com.cn.bent.sports.utils.SaveObjectUtils;
import com.cn.bent.sports.view.activity.LoginActivity;
import com.cn.bent.sports.view.activity.SettingActivity;
import com.vondear.rxtools.view.RxToast;
import com.zhl.network.RxObserver;
import com.zhl.network.RxSchedulers;
import com.zhl.network.huiqu.HuiquRxTBFunction;
import com.zhl.network.huiqu.JavaRxFunction;

/**
 * Created by lyj on 2018/4/13/013.
 */

public  class RxRequest<T> extends RxObserver<T> {
    public  RequestLisler<T> requestLister;
    private Context context;
    public RxRequest(Context context, String key, int whichRequest,RequestLisler<T> requestLister) {
        super(context, key, whichRequest, false);
        this.requestLister=requestLister;
        this.context=context;
    }

    @Override
    public void onSuccess(int whichRequest, T t) {
        requestLister.onSucess(whichRequest,t);
    }

    @Override
    public void onError(int whichRequest, Throwable e) {
        if(whichRequest==401){
            getResfreToken();
        }else
        requestLister.on_error(whichRequest, e);
    }



    private void getResfreToken() {
        LoginResult user= SaveObjectUtils.getInstance(context).getObject(Constants.USER_INFO, null);
        BaseApi.getJavaLoginService(context).loginWithRefreshToken("refresh_token",user.getRefresh_token())
                .map(new HuiquRxTBFunction<LoginResult>())
                .compose(RxSchedulers.<LoginResult>io_main())
                .subscribe(new RxObserver<LoginResult>(context, "", 1, false) {
                    @Override
                    public void onSuccess(int whichRequest, LoginResult info) {
                        SaveObjectUtils.getInstance(context).setObject(Constants.USER_INFO, info);
                    }
                    @Override
                    public void onError(int whichRequest, Throwable e) {
                        RxToast.warning("请从新登陆");
                        SaveObjectUtils.getInstance(context).setObject(Constants.USER_INFO, null);
                        MyApplication.instance.getActivityManager().popAllActivity();
                        context.startActivity(new Intent(context, LoginActivity.class));
                    }
                });
    }



}
