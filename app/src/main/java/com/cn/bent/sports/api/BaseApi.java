package com.cn.bent.sports.api;

import android.content.Context;

import com.zhl.network.RxRetrofit;

/**
 * Created by Administrator on 2017/9/14.
 */

public class BaseApi {
    public static ApiService apiService;


    public static ApiService getDefaultService(Context context) {
        if (apiService == null) {
            apiService = RxRetrofit.getRetrofit(ConstantValues.BASE_URL,context).create(ApiService.class);
        }
        return apiService;
    }
    public static ApiService getJavaDefaultService(Context context) {
        if (apiService == null) {
            apiService = RxRetrofit.getRetrofit(ConstantValues.JAVA_URL,context).create(ApiService.class);
        }
        return apiService;
    }
    public static ApiService getJavaLoginService(Context context) {
        if (apiService == null) {
            apiService = RxRetrofit.getLoginRetrofit(ConstantValues.JAVA_URL,context).create(ApiService.class);
        }
        return apiService;
    }
    public static ApiService getJavaLoginDefaultService(Context context,String access_token) {
        if (apiService == null) {
            apiService = RxRetrofit.getLoginRetrofit(ConstantValues.JAVA_URL,context,access_token).create(ApiService.class);
        }
        return apiService;
    }
}
