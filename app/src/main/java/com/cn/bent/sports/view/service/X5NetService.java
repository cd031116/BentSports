package com.cn.bent.sports.view.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.tencent.smtt.sdk.QbSdk;

/**
 * Created by Administrator on 2018/4/20.
 */

public class X5NetService extends IntentService {

    public X5NetService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        initX5Web();
    }

    private void initX5Web() {
        if (!QbSdk.isTbsCoreInited())
            QbSdk.preInit(getApplicationContext(),null);
        QbSdk.initX5Environment(getApplicationContext(),cb);
    }
    QbSdk.PreInitCallback cb=new QbSdk.PreInitCallback() {
        @Override
        public void onCoreInitFinished() {
        }

        @Override
        public void onViewInitFinished(boolean b) {
            Log.d("bentsports", " onViewInitFinished is " + b);
        }
    };
}
