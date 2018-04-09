package com.cn.bent.sports.base;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.cn.bent.sports.database.PlayPointManager;
import com.cn.bent.sports.database.PlayUserManager;
import com.cn.bent.sports.database.QueueManager;
import com.cn.bent.sports.database.TaskCationManager;
import com.danikula.videocache.HttpProxyCacheServer;
import com.mob.MobSDK;
import com.tencent.bugly.crashreport.CrashReport;
import com.vondear.rxtools.RxTool;

import org.aisen.android.common.context.GlobalContext;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


/**
 * Created by lyj on 17/8/2.
 */

public class MyApplication extends GlobalContext{
    public static MyApplication instance;
    private ActivityManagerd activityManager = null;
    private HttpProxyCacheServer proxy;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
// 初始化Bugly
        CrashReport.initCrashReport(getApplicationContext(), "edd3b05320", false);
        activityManager = ActivityManagerd.getScreenManager();
        TaskCationManager.setup(this);
        PlayUserManager.setup(this);
        QueueManager.setup(this);
        PlayPointManager.setup(this);
        RxTool.init(this);
        MobSDK.init(this);
    }

    public static HttpProxyCacheServer getProxy(Context context) {
        MyApplication app = (MyApplication) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer.Builder(this)
                .maxCacheSize(1024 * 1024 * 1024)       // 1 Gb for cache
                .maxCacheFilesCount(50)
                .build();
    }
    public ActivityManagerd getActivityManager() {
        return activityManager;
    }

    public static MyApplication getInstance() {
        return instance;
    }

    /**
     * d
     * 获取App安装包信息
     *
     * @return
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

    @Override
     protected void attachBaseContext(Context base) {
                 super.attachBaseContext(base);
                 MultiDex.install(this) ;
            }

    @Override
    public void onTerminate() {
        // TODO Auto-generated method stub
        super.onTerminate();
    }

}