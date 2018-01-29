package com.zhl.network;

import android.content.Context;

import java.io.File;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by necer on 2017/6/29.
 */

public class RxRetrofit {

    private static final int READ_TIME_OUT = 20;
    private static final int CONNECT_TIME_OUT = 20;
    private static Retrofit retrofit;

    public static Retrofit getRetrofit(String baseUrl, Context context) {
        //开启Log
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {

                MyLog.d("message::" + message);
            }
        });
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //缓存
        File cacheFile = new File("", "cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb

        CookieHandler cookieHandler = new CookieManager(new PersistentCookieStore(context),
                CookiePolicy.ACCEPT_ALL);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(new JavaNetCookieJar(cookieHandler))
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor(logInterceptor)
                .cache(cache)
                .build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(CustomGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();

        return retrofit;
    }


}
